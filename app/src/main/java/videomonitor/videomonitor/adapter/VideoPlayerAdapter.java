package videomonitor.videomonitor.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import videomonitor.videomonitor.entity.VideoInfo;
import videomonitor.videomonitor.fragment.VideoPlayerFragment;

/**
 * Created by Administrator on 2017-06-23.
 */

public class VideoPlayerAdapter extends FragmentStatePagerAdapter {
    private List<VideoInfo> titles;
    private List<VideoPlayerFragment> fragments;

    public VideoPlayerAdapter(FragmentManager fm, List<VideoInfo> titles, List<VideoPlayerFragment> fragments) {
        super(fm);
        this.titles = titles;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "视频-" + titles.get(position).displayName;
    }
}