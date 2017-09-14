package videomonitor.videomonitor.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import videomonitor.videomonitor.fragment.SiteInfoFragment;

/**
 * Created by Administrator on 2017-08-31.
 */

public class SiteAdapter extends FragmentStatePagerAdapter {
    private List<String> titles;
    private List<SiteInfoFragment> fragments;

    public SiteAdapter(FragmentManager fm, List<String> titles, List<SiteInfoFragment> fragments) {
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
        return titles.get(position);
    }
}
