package videomonitor.videomonitor.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Request;
import videomonitor.videomonitor.R;
import videomonitor.videomonitor.VideoUtils;
import videomonitor.videomonitor.adapter.SiteAdapter;
import videomonitor.videomonitor.constant.Constant;
import videomonitor.videomonitor.db.ShareUtils;
import videomonitor.videomonitor.entity.ProductOrderInfoEntity;
import videomonitor.videomonitor.entity.SewingInfoEntity;
import videomonitor.videomonitor.entity.SiteEntity;
import videomonitor.videomonitor.entity.SiteInfoEntity;
import videomonitor.videomonitor.entity.VideoInfo;

/**
 * Created by Administrator on 2017-08-31.
 */

public class HomePagerFragment extends Fragment implements SiteInfoFragment.VideoBookListener {
    private View view;
    private ViewPager mViewPager;
    private List<SiteInfoFragment> siteFgList = new ArrayList<SiteInfoFragment>();
//    private List<SiteEntity> siteEntityList = new ArrayList<SiteEntity>();

    private List<VideoInfo> list;

    private TextView currentId;

    //生产单信息
    private TextView poCode; //生产单
    private TextView patternNo; //款号
    private TextView customer; //客户
    private TextView color; //颜色
    private TextView size; //尺码
    private TextView amount; //数量

    //缝纫机信息
    private TextView code; //设备编号
    private TextView modelNo; //型号
    private TextView thisBootTime; //本次开机时间
    private TextView sumBootTime; //累计开机时间
    private TextView pinNum; //本次开机针数
    private TextView sumPinNum; //累计开机针数
    private TextView cutLineNum; //本次切线数
    private TextView sumCutLineNum; //累计切线数
    private TextView presserNum; //本次压脚抬起数
    private TextView sumPresserNum; //累计压脚抬起数
    private TextView speed; //当前转速

    private Timer timer;
    private TimerTask timerTask;
    public static int position;
    //==自动轮播
    private boolean isAuto = true;//默认情况下我们是开启自动轮播
    private List<ImageView> imgList;

    private FrameLayout flayout;
    private LinearLayout redDotLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_homepager, container, false);

        list =  VideoUtils.getVideoFile(new ArrayList<VideoInfo>(), new File(Environment.getExternalStorageDirectory() + "/Movies"));//  /DCIM/Video   /Movies/1
        if(list == null || list.size() == 0) {
            list =  VideoUtils.getVideoFile(new ArrayList<VideoInfo>(), new File(Environment.getExternalStorageDirectory() + "/DCIM/Video"));
        }
//        initDate();

        currentId = (TextView) view.findViewById(R.id.current_id);
        String id =  getArguments().getString("CURRENT_ID", "2017001023");
        currentId.setText("工号 " + id);

        //实例化生产单信息控件
        poCode = (TextView) view.findViewById(R.id.poCode); //生产单
        patternNo = (TextView) view.findViewById(R.id.patternNo); //款号
        customer = (TextView) view.findViewById(R.id.customer); //客户
        color = (TextView) view.findViewById(R.id.color); //颜色
        size = (TextView) view.findViewById(R.id.size); //尺码
        amount = (TextView) view.findViewById(R.id.amount); //数量

        //实例化缝纫机信息控件
        code = (TextView) view.findViewById(R.id.code); //设备编号
        modelNo = (TextView) view.findViewById(R.id.modelNo); //型号
        thisBootTime = (TextView) view.findViewById(R.id.thisBootTime); //本次开机时间
        sumBootTime = (TextView) view.findViewById(R.id.sumBootTime); //累计开机时间
        pinNum = (TextView) view.findViewById(R.id.pinNum); //本次开机针数
        sumPinNum = (TextView) view.findViewById(R.id.sumPinNum); //累计开机针数
        cutLineNum = (TextView) view.findViewById(R.id.cutLineNum); //本次切线数
        sumCutLineNum = (TextView) view.findViewById(R.id.sumCutLineNum); //累计切线数
        presserNum = (TextView) view.findViewById(R.id.presserNum); //本次压脚抬起数
        sumPresserNum = (TextView) view.findViewById(R.id.sumPresserNum); //累计压脚抬起数
        speed = (TextView) view.findViewById(R.id.speed); //当前转速


        flayout = (FrameLayout) view.findViewById(R.id.fh_frameLayout);
        redDotLayout = (LinearLayout) view.findViewById(R.id.fh_redDot_layout);
        mViewPager = (ViewPager) view.findViewById(R.id.fh_viewPager);

        getProductionOrderInfo(Constant.productOrderInfoUrl, ShareUtils.getProductOrderId(getActivity()));
        getSweingOrderInfo(Constant.sweingInfoUrl, ShareUtils.getSewingId(getActivity()));
        getSiteInfo(Constant.siteInfoUrl, ShareUtils.getSiteId(getActivity()));

        return view;
    }

    private void setAutoViewPager() {
        flayout.setVisibility(View.VISIBLE);
        imgList = new ArrayList<ImageView>();
        ImageView iv = null;
        for(int i = 0; i < siteFgList.size(); i++) {
            if(i == 0) {
                iv = new ImageView(getActivity());
                iv.setImageResource(R.mipmap.icon_dot_select);
                iv.setPadding(0, 0, 10, 0);
                imgList.add(iv);
            } else {
                iv = new ImageView(getActivity());
                iv.setImageResource(R.mipmap.icon_dot_normal);
                iv.setPadding(0, 0, 10, 0);
                imgList.add(iv);
            }
            redDotLayout.addView(iv);
        }

        if(siteFgList.size() > 1) {
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    if(isAuto) {
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    }
                }
            };
            timer.schedule(timerTask, 4000, 6000);

            mViewPager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch(event.getAction()) {
                        case MotionEvent.ACTION_DOWN://表示的是用户按下的一瞬间
                            stopAuto();
                            break;
                        case MotionEvent.ACTION_MOVE://表示的是用户按下之后，在屏幕上移动的过程

                            break;
                        case MotionEvent.ACTION_UP://表示的是用户抬起的一瞬间
                            startAuto();
                            break;
                        default:

                            break;
                    }
                    return false;//返回true的目的是告诉我们该ViewGroup容器的父View，我们已经处理好了该事件
                }
            });
        }
    }

    private void startAuto() {
        isAuto = true;
    }

    private void stopAuto() {
        isAuto = false;
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if(position < siteFgList.size() - 1) {
                        mViewPager.setCurrentItem(position + 1);

                    } else if(position == siteFgList.size() - 1) {
                        mViewPager.setCurrentItem(0);

                    }
                    break;
            }

        }
    };

//    private void initDate() {
//        SiteEntity entity = new SiteEntity();
//        entity.setSiteNum(" 13"); //站点
//        entity.setProcessNumber("70045"); //工序编号
//        entity.setProcessName("暗合门襟"); //工序名称
//        entity.setStandardHour("45秒"); //标准工时
//        entity.setProcessPrice("0.35元/件"); //工序单价
//        entity.setTodayPlanNumber("850件"); //今日计划数
//        entity.setTodayCompanyNumber("650件"); //今日完成数
//        entity.setTodayPlanSalary("297.5元"); //今日计划工资
//        entity.setTodayGotSalary("227.5元"); //今日已拿工资
//        entity.setBookUrl(R.mipmap.icon_zyzds);
//        entity.setVideoUrl(list.get(0).filePath);
//        siteEntityList.add(entity);
//        SiteInfoFragment fragment = new SiteInfoFragment();
//        fragment.setOnVideoBookListener(this);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("entity", entity);
//        bundle.putInt("position", 0);
//        fragment.setArguments(bundle);
//        siteFgList.add(fragment);
//
//        SiteEntity entity1 = new SiteEntity();
//        entity1.setSiteNum(" 14"); //站点
//        entity1.setProcessNumber("工序编号 70046"); //工序编号
//        entity1.setProcessName("工序名称 挂钩垫布"); //工序名称
//        entity1.setStandardHour("标准工时 40秒"); //标准工时
//        entity1.setProcessPrice("0.4元/件"); //工序单价
//        entity1.setTodayPlanNumber("750件"); //今日计划数
//        entity1.setTodayCompanyNumber("650件"); //今日完成数
//        entity1.setTodayPlanSalary("300.5元"); //今日计划工资
//        entity1.setTodayGotSalary("270.5元"); //今日已拿工资
//        entity1.setBookUrl(R.mipmap.icon_zyzds1);
//        entity1.setVideoUrl(list.get(1).filePath);
//        siteEntityList.add(entity1);
//        SiteInfoFragment fragment1 = new SiteInfoFragment();
//        fragment1.setOnVideoBookListener(this);
//        Bundle bundle1 = new Bundle();
//        bundle1.putSerializable("entity", entity1);
//        bundle1.putInt("position", 1);
//        fragment1.setArguments(bundle1);
//        siteFgList.add(fragment1);
//    }

    VideoPlayerJCFragment videoFragment;
    @Override
    public void videoListener(String videoUrl, int position) {
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


    /**
     * 获取生产单信息
     * @param url
     * @param code
     */
    private void getProductionOrderInfo(String url, String code) {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("code", code)
                .build()
                .execute(new MyStringCallback());
    }

    public class MyStringCallback extends StringCallback {
        @Override
        public void onBefore(Request request, int id) {
            Log.e("reeuest", "" + request + id);
        }

        @Override
        public void onAfter(int id) {

        }

        @Override
        public void onError(Call call, Exception e, int id) {
            e.printStackTrace();
        }

        @Override
        public void onResponse(String response, int id) {
            Gson gson = new Gson();
            ProductOrderInfoEntity entity = gson.fromJson(response, ProductOrderInfoEntity.class);
            poCode.setText("生产单  " + entity.getPoCode()); //生产单
            patternNo.setText("款号  " + entity.getPatternNo()); //款号
            customer.setText("顾客  " + entity.getCustomer()); //客户
            color.setText("颜色  " + entity.getColor()); //颜色
            size.setText("尺码  " + entity.getSize()); //尺码
            amount.setText("数量  " + entity.getAmount()); //数量
        }

        @Override
        public void inProgress(float progress, long total, int id) {
//            mProgressBar.setProgress((int) (100 * progress));
        }
    }

    /**
     * 获取缝纫机信息
     * @param url
     * @param code
     */
    private void getSweingOrderInfo(String url, String code) {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("code", code)
                .build()
                .execute(new MySweingCallback());
    }

    public class MySweingCallback extends StringCallback {
        @Override
        public void onBefore(Request request, int id) {
            Log.e("reeuest", "" + request + id);
        }

        @Override
        public void onAfter(int id) {

        }

        @Override
        public void onError(Call call, Exception e, int id) {
            e.printStackTrace();
        }

        @Override
        public void onResponse(String response, int id) {
            Gson gson = new Gson();
            SewingInfoEntity entity = gson.fromJson(response, SewingInfoEntity.class);
            code.setText(entity.getCode()); //设备编号
            modelNo.setText(entity.getModelNo()); //型号
            thisBootTime.setText(entity.getThisBootTime() +  "分钟"); //本次开机时间
            sumBootTime.setText(entity.getSumBootTime() + "小时"); //累计开机时间
            pinNum.setText(entity.getPinNum() + "针"); //本次开机针数
            sumPinNum.setText(entity.getSumPinNum()); //累计开机针数
            cutLineNum.setText(entity.getCutLineNum() + "次"); //本次切线数
            sumCutLineNum.setText(entity.getSumCutLineNum() + "次"); //累计切线数
            presserNum.setText(entity.getPresserNum() + "次"); //本次压脚抬起数
            sumPresserNum.setText(entity.getSumPresserNum() + "次"); //累计压脚抬起数
            speed.setText(entity.getSpeed() + "RPM"); //当前转速
        }

        @Override
        public void inProgress(float progress, long total, int id) {
//            mProgressBar.setProgress((int) (100 * progress));
        }
    }

    /**
     * 获取站点信息
     * @param url
     * @param code
     */
    private void getSiteInfo(String url, String code) {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("code", code)
                .build()
                .execute(new MySiteCallback());
    }

    public class MySiteCallback extends StringCallback {
        @Override
        public void onBefore(Request request, int id) {
            Log.e("reeuest", "" + request + id);
        }

        @Override
        public void onAfter(int id) {

        }

        @Override
        public void onError(Call call, Exception e, int id) {
            e.printStackTrace();
        }

        @Override
        public void onResponse(String response, int id) {
            SiteInfoEntity entity = new Gson().fromJson(response, SiteInfoEntity.class);
            setData(entity);
        }

        @Override
        public void inProgress(float progress, long total, int id) {
//            mProgressBar.setProgress((int) (100 * progress));
        }
    }

    private void setData(SiteInfoEntity entity) {
        if(entity.getProcessData() != null && entity.getProcessData().size() > 0) {
            for(int i = 0; i < entity.getProcessData().size(); i++) {
                entity.getProcessData().get(i).setBookUrl(R.mipmap.icon_zyzds1);
                entity.getProcessData().get(i).setVideoUrl(list.get(i).filePath);
                SiteInfoFragment fragment1 = new SiteInfoFragment();
                fragment1.setOnVideoBookListener(this);
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("entity", entity.getProcessData().get(i));
                bundle1.putInt("position", i);
                fragment1.setArguments(bundle1);
                siteFgList.add(fragment1);
            }

            mViewPager.setOffscreenPageLimit(entity.getProcessData().size());
            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(final int position) {
                    HomePagerFragment.position = position;

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            for(int i = 0; i < siteFgList.size(); i++) {
                                if(i == position) {
                                    imgList.get(i).setImageResource(R.mipmap.icon_dot_select);
                                } else {
                                    imgList.get(i).setImageResource(R.mipmap.icon_dot_normal);
                                }
                            }
                        }
                    }, 200);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            SiteAdapter adapter = new SiteAdapter(getChildFragmentManager(), siteFgList);
            mViewPager.setAdapter(adapter);

            setAutoViewPager();

            bookListener(entity.getProcessData().get(0).getBookUrl(), 0);

        }

    }

}
