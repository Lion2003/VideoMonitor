package videomonitor.videomonitor.fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import videomonitor.videomonitor.R;
import videomonitor.videomonitor.activity.VideoStandardDetailActivity;
import videomonitor.videomonitor.adapter.InstructionBookAdapter;
import videomonitor.videomonitor.entity.InstructionBookEntity;
import videomonitor.videomonitor.entity.VideoDetailInfo;
import videomonitor.videomonitor.entity.VideoEntity;

/**
 * Created by Administrator on 2017-09-05.
 */

public class InstructionBookListFragment extends Fragment {
    private View view;
    private AppBarLayout appBarLayout;
    private SwipeRefreshLayout srl;
    private RecyclerView mRecyclerView;
    private GridLayoutManager gridLayoutManager;
    private InstructionBookAdapter adapter;
    private boolean isShowTitle = true;

    private List<InstructionBookEntity> mList = new ArrayList<InstructionBookEntity>();
    private List<VideoEntity> mVideoList = new ArrayList<VideoEntity>();

    private ListView popListView;
    private List<Map<String, String>> statusData1;
    private PopupWindow popMenu;
    private SimpleAdapter menuAdapter1;
    private LinearLayout product;
    private TextView tvStatus;
    private int position = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_instruction_book_list, container, false);
        appBarLayout = (AppBarLayout) view.findViewById(R.id.fibl_title);
        isShowTitle = getArguments().getBoolean("isShowTitle", true);
        if(isShowTitle) {
            appBarLayout.setVisibility(View.VISIBLE);
        } else {
            appBarLayout.setVisibility(View.GONE);
        }
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
        getInstructionBook();
        gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new InstructionBookAdapter(getActivity(), mList);
        mRecyclerView.setAdapter(adapter);

        //添加点击事件
        adapter.setOnItemClickListener(new InstructionBookAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d("test", "item position = " + position);
                VideoDetailInfo info = new VideoDetailInfo();
                info.videoPath = mVideoList.get(position).filePath;
                info.title = mVideoList.get(position).title;
                Intent it = new Intent(getActivity(), VideoStandardDetailActivity.class);
                it.putExtra(VideoDetailInfo.class.getSimpleName(), info); //
                it.putExtra("currentState", 1); //0:video, 1:image
                startActivity(it);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        initpopupWin(view);
        return view;
    }

    private void initpopupWin(View view) {
        product = (LinearLayout) view.findViewById(R.id.supplier_list_activity);
        tvStatus = (TextView) view.findViewById(R.id.supplier_list_activity_tv);

        product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = 0;
                initPopMenu();

                tvStatus.setTextColor(Color.parseColor("#39ac69"));
                popListView.setAdapter(menuAdapter1);
                popMenu.showAsDropDown(product, 0, 2);
            }
        });
    }

    private void initMenuData() {
        statusData1 = new ArrayList<Map<String, String>>();
        String[] menuStr1 = new String[] { "全部", "上衣", "裤子", "鞋帽"};
        Map<String, String> map1;
        switch(position) {
            case 0:
                for (int i = 0, len = menuStr1.length; i < len; ++i) {
                    map1 = new HashMap<String, String>();
                    map1.put("name", menuStr1[i]);
                    statusData1.add(map1);
                }
                break;
        }
    }

    private void initPopMenu() {
        initMenuData();
        View contentView = View.inflate(getActivity(), R.layout.popwin_supplier_list,
                null);
        popMenu = new PopupWindow(contentView,
                product.getWidth(), LinearLayout.LayoutParams.WRAP_CONTENT);
        popMenu.setOutsideTouchable(true);
        popMenu.setBackgroundDrawable(new BitmapDrawable());
        popMenu.setFocusable(true);
        popMenu.setAnimationStyle(R.style.popwin_anim_style);
        popMenu.setOnDismissListener(new PopupWindow.OnDismissListener() {
            public void onDismiss() {
                switch(position) {
                    case 0:
                        tvStatus.setTextColor(Color.parseColor("#5a5959"));
                        break;
                }
            }
        });

        popListView = (ListView) contentView.findViewById(R.id.popwin_supplier_list_lv);
        contentView.findViewById(R.id.popwin_supplier_list_bottom)
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        popMenu.dismiss();
                    }
                });
        menuAdapter1 = new SimpleAdapter(getActivity(), statusData1,
                R.layout.item_listview_popwin, new String[] { "name" },
                new int[] { R.id.listview_popwind_tv });

        popListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
                                    long arg3) {
                popMenu.dismiss();
                switch(position) {
                    case 0:
                        tvStatus.setText(statusData1.get(pos).get("name"));
                        break;
                }
            }
        });
    }

    private void getInstructionBook() {
        InstructionBookEntity entity;
        for(int i = 0; i < mVideoList.size(); i++) {
            entity = new InstructionBookEntity();
            entity.setTechnologyId("70045");
            entity.setTechnologyName("暗合门襟腰头挂钩");
            entity.setSrc(R.mipmap.icon_zyzds1);
            mList.add(entity);
        }
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
                mVideoList.add(entty);
            } while (cursor.moveToNext());

        }
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
    }
}
