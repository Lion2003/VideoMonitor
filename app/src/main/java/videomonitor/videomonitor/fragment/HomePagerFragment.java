package videomonitor.videomonitor.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import videomonitor.videomonitor.R;
import videomonitor.videomonitor.VideoUtils;
import videomonitor.videomonitor.adapter.SiteAdapter;
import videomonitor.videomonitor.entity.SiteEntity;
import videomonitor.videomonitor.entity.VideoInfo;

/**
 * Created by Administrator on 2017-08-31.
 */

public class HomePagerFragment extends Fragment implements SiteInfoFragment.VideoBookListener {
    private View view;
    private ViewPager mViewPager;
    private List<SiteInfoFragment> siteFgList = new ArrayList<SiteInfoFragment>();
    private List<SiteEntity> siteEntityList = new ArrayList<SiteEntity>();

    private List<VideoInfo> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_homepager, container, false);

        list =  VideoUtils.getVideoFile(new ArrayList<VideoInfo>(), new File(Environment.getExternalStorageDirectory() + "/Movies/1"));//  /DCIM/Video   /Movies/1
        if(list == null || list.size() == 0) {
            list =  VideoUtils.getVideoFile(new ArrayList<VideoInfo>(), new File(Environment.getExternalStorageDirectory() + "/DCIM/Video"));
        }
        initDate();

        mViewPager = (ViewPager) view.findViewById(R.id.fh_viewPager);
        mViewPager.setOffscreenPageLimit(siteEntityList.size());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        SiteAdapter adapter = new SiteAdapter(getChildFragmentManager(), siteFgList);
        mViewPager.setAdapter(adapter);
        return view;
    }

    private void initDate() {
        SiteEntity entity = new SiteEntity();
        entity.setSiteNum("站点  13"); //站点
        entity.setProcessNumber("工序编号 70045"); //工序编号
        entity.setProcessName("工序名称 暗合门襟"); //工序名称
        entity.setStandardHour("标准工时 45秒"); //标准工时
        entity.setProcessPrice("0.35元/件"); //工序单价
        entity.setTodayPlanNumber("850件"); //今日计划数
        entity.setTodayCompanyNumber("650件"); //今日完成数
        entity.setTodayPlanSalary("297.5元"); //今日计划工资
        entity.setTodayGotSalary("227.5元"); //今日已拿工资
        entity.setBookUrl(R.mipmap.icon_zyzds);
        entity.setVideoUrl(list.get(0).filePath);
        siteEntityList.add(entity);
        SiteInfoFragment fragment = new SiteInfoFragment();
        fragment.setOnVideoBookListener(this);
        Bundle bundle = new Bundle();
        bundle.putSerializable("entity", entity);
        bundle.putInt("position", 0);
        fragment.setArguments(bundle);
        siteFgList.add(fragment);

        SiteEntity entity1 = new SiteEntity();
        entity1.setSiteNum("站点  14"); //站点
        entity1.setProcessNumber("工序编号 70046"); //工序编号
        entity1.setProcessName("工序名称 挂钩垫布"); //工序名称
        entity1.setStandardHour("标准工时 40秒"); //标准工时
        entity1.setProcessPrice("0.4元/件"); //工序单价
        entity1.setTodayPlanNumber("750件"); //今日计划数
        entity1.setTodayCompanyNumber("650件"); //今日完成数
        entity1.setTodayPlanSalary("300.5元"); //今日计划工资
        entity1.setTodayGotSalary("270.5元"); //今日已拿工资
        entity1.setBookUrl(R.mipmap.icon_zyzds1);
        entity1.setVideoUrl(list.get(1).filePath);
        siteEntityList.add(entity1);
        SiteInfoFragment fragment1 = new SiteInfoFragment();
        fragment1.setOnVideoBookListener(this);
        Bundle bundle1 = new Bundle();
        bundle1.putSerializable("entity", entity1);
        bundle1.putInt("position", 1);
        fragment1.setArguments(bundle1);
        siteFgList.add(fragment1);
    }

    VideoPlayerJCFragment videoFragment;
    @Override
    public void videoListener(String videoUrl, int position) {
//        Toast.makeText(getActivity(), videoUrl, Toast.LENGTH_SHORT).show();
        if(videoFragment != null) {
            videoFragment.getBDView().onStop();
            videoFragment.getBDView().onDestroy();
            videoFragment = null;
        }
        videoFragment = new VideoPlayerJCFragment();
        Bundle bundle = new Bundle();
        bundle.putString("videoUrl", videoUrl);
        videoFragment.setArguments(bundle);
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fh_linearlayout, videoFragment)
                .commit();
    }

    InstructBookOnePageFragment bookFragment;
    @Override
    public void bookListener(int bookUrl, int position) {
//        Toast.makeText(getActivity(), bookUrl + "", Toast.LENGTH_SHORT).show();
        if(videoFragment != null) {
            videoFragment.getBDView().onStop();
            videoFragment.getBDView().onDestroy();
            videoFragment = null;
        }
        bookFragment = new InstructBookOnePageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("source", bookUrl);
        bookFragment.setArguments(bundle);
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fh_linearlayout, bookFragment)
                .commit();

    }

}