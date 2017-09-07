package videomonitor.videomonitor.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import videomonitor.videomonitor.R;
import videomonitor.videomonitor.adapter.MyCollectionAdapter;

/**
 * 我的收藏
 * Created by Administrator on 2017-09-06.
 */

public class MyCollectionFragment extends Fragment {
    private View view;

    private TabLayout tablayout;
    private ViewPager viewPager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_collection, container, false);

        String[] titles = {"企业指导书", "标准视频"};
        List<Fragment> fragmentList = new ArrayList<Fragment>();
        Fragment fragment3 = new InstructionBookListFragment();
        Bundle bundle3 = new Bundle();
        bundle3.putBoolean("isShowTitle", false);
        fragment3.setArguments(bundle3);
        fragmentList.add(fragment3);

        Fragment fragment4 = new MyCollectVideoStandardFragment();
        fragmentList.add(fragment4);


        tablayout = (TabLayout) view.findViewById(R.id.tabs);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(2);

        MyCollectionAdapter adapter = new MyCollectionAdapter(getChildFragmentManager(), titles, fragmentList);
        viewPager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewPager);
        tablayout.setTabsFromPagerAdapter(adapter);

        return view;
    }
}
