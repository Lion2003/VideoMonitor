package videomonitor.videomonitor.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import videomonitor.videomonitor.R;
import videomonitor.videomonitor.entity.VideoInfo;
import videomonitor.videomonitor.VideoUtils;
import videomonitor.videomonitor.adapter.VideoPlayerAdapter3;

/**
 * Created by Administrator on 2017-06-27.
 */

public class VideoViewPagerFragment extends Fragment {
    private View view;
    private TabLayout tablayout;
    private ViewPager viewPager;
    private List<VideoInfo> list;
    private List<VideoPlayerFragment3> fragmentList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_video_viewpager, container, false);

        tablayout = (TabLayout) view.findViewById(R.id.tabs);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        list =  VideoUtils.getVideoFile(new ArrayList<VideoInfo>(), new File(Environment.getExternalStorageDirectory() + "/Movies/1"));//  /DCIM/Video   /Movies/1
        if(list == null || list.size() == 0) {
            list =  VideoUtils.getVideoFile(new ArrayList<VideoInfo>(), new File(Environment.getExternalStorageDirectory() + "/DCIM/Video"));
        }
        Log.e("as", "list");
        viewPager.setOffscreenPageLimit(list.size());

        fragmentList = new ArrayList<VideoPlayerFragment3>();
        for (int i = 0; i < list.size(); i++) {
            tablayout.addTab(tablayout.newTab().setText(list.get(i).displayName));
            VideoPlayerFragment3 fragment = new VideoPlayerFragment3();
            Bundle bundle = new Bundle();
            bundle.putString("filePath", list.get(i).filePath);
            if(i == 0) {
                bundle.putBoolean("isFirstInPlay", true);
            } else {
                bundle.putBoolean("isFirstInPlay", false);
            }
            fragment.setArguments(bundle);
            fragmentList.add(fragment);
        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for(int i = 0; i < list.size(); i++) {
                    if (i != position) {
                        fragmentList.get(i).stop();
                    } else {
                        fragmentList.get(i).play(list.get(i).filePath);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        VideoPlayerAdapter3 adapter = new VideoPlayerAdapter3(getChildFragmentManager(), list, fragmentList);
        viewPager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewPager);
        tablayout.setTabsFromPagerAdapter(adapter);

        return view;
    }

    public void pauseAllVideo(){
        for(int i = 0; i < list.size(); i++) {
            fragmentList.get(i).onMyPause();
        }
    }

    public void stopAllVideo() {
        for(int i = 0; i < list.size(); i++) {
            fragmentList.get(i).stop();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
