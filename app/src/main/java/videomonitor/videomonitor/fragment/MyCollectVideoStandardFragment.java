package videomonitor.videomonitor.fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import videomonitor.videomonitor.R;
import videomonitor.videomonitor.activity.VideoStandardDetailActivity;
import videomonitor.videomonitor.adapter.MyCollectVideoAdapter;
import videomonitor.videomonitor.adapter.VideoStandardAdapter;
import videomonitor.videomonitor.entity.VideoDetailInfo;
import videomonitor.videomonitor.entity.VideoEntity;

/**
 * 我收藏的标准视频
 * Created by Administrator on 2017-09-06.
 */

public class MyCollectVideoStandardFragment extends Fragment {

    private View view;
    private SwipeRefreshLayout srl;
    private RecyclerView mRecyclerView;
    private GridLayoutManager gridLayoutManager;
    private MyCollectVideoAdapter adapter;

    private List<VideoEntity> mList = new ArrayList<VideoEntity>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_collection_video, container, false);
        srl = (SwipeRefreshLayout)view.findViewById(R.id.srl);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.fvs_recyclerView);
        srl.setColorSchemeResources(android.R.color.holo_red_light, android.R.color.holo_blue_light,
                android.R.color.holo_green_light);
//        srl.post(new Runnable() {
//            @Override
//            public void run() {
//                srl.setRefreshing(true);
//            }
//        });
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        srl.setRefreshing(false);
                    }
                }, 1000);
            }
        });
        getVideoFile();
        gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new MyCollectVideoAdapter(getActivity(), mList);
        mRecyclerView.setAdapter(adapter);

        //添加点击事件
        adapter.setOnItemClickListener(new MyCollectVideoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d("test", "item position = " + position);
                VideoDetailInfo info = new VideoDetailInfo();
                info.videoPath = mList.get(position).filePath;
                info.title = mList.get(position).title;
                Intent it = new Intent(getActivity(), VideoStandardDetailActivity.class);
                it.putExtra(VideoDetailInfo.class.getSimpleName(), info); //
                it.putExtra("currentState", 0); //0:video, 1:image
                startActivity(it);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

        return view;
    }

    private void getVideoFile() {
        ContentResolver mContentResolver=getActivity().getContentResolver();
        Cursor cursor=mContentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null,MediaStore.Video.DEFAULT_SORT_ORDER);

        if (cursor != null && cursor.moveToFirst()) {
            do {

                // ID:MediaStore.Audio.Media._ID
                int id = cursor.getInt(cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media._ID));

                // title：MediaStore.Audio.Media.TITLE
                String title = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                // path：MediaStore.Audio.Media.DATA
                String url = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.DATA));

                // duration：MediaStore.Audio.Media.DURATION
                int duration = cursor
                        .getInt(cursor
                                .getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));

                // 大小：MediaStore.Audio.Media.SIZE
                int size = (int) cursor.getLong(cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));

                VideoEntity entty = new VideoEntity();
                entty.ID = id;
                entty.title = title;
                entty.filePath = url;
                entty.duration = duration;
                entty.size = size;
                mList.add(entty);
            } while (cursor.moveToNext());

        }
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
    }
}
