package videomonitor.videomonitor.fragment;

import android.content.Intent;
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
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Request;
import videomonitor.videomonitor.ImageUtils;
import videomonitor.videomonitor.R;
import videomonitor.videomonitor.VideoUtils;
import videomonitor.videomonitor.activity.FullScreenBookActivity;
import videomonitor.videomonitor.adapter.SiteAdapter;
import videomonitor.videomonitor.constant.Constant;
import videomonitor.videomonitor.db.ShareUtils;
import videomonitor.videomonitor.entity.EmpInfoEntity;
import videomonitor.videomonitor.entity.ImageInfo;
import videomonitor.videomonitor.entity.ProductOrderInfoEntity;
import videomonitor.videomonitor.entity.SewingInfoEntity;
import videomonitor.videomonitor.entity.SiteInfoEntity;
import videomonitor.videomonitor.entity.VideoInfo;
import videomonitor.videomonitor.utils.ACache;
import videomonitor.videomonitor.utils.StringUtil;

/**
 * Created by Administrator on 2017-08-31.
 */

public class HomePagerFragment extends Fragment implements SiteInfoFragment.VideoBookListener {
    private View view;
    private ViewPager mViewPager;
    private boolean isTimerStart = false;//防止获取缓存的时候启动一次，调用接口再启动一次。
    private List<SiteInfoFragment> siteFgList = new ArrayList<SiteInfoFragment>();
//    private List<SiteEntity> siteEntityList = new ArrayList<SiteEntity>();

    private List<VideoInfo> list;
    private List<ImageInfo> imageList;

    //用户信息
    private TextView userName;
    private TextView currentId;

    private ACache mCache;

    //生产单信息
    private ProductOrderInfoEntity productOrderEntity;
    private TextView poCode; //生产单
    private TextView patternNo; //款号
    private TextView customer; //客户
    private TextView color; //颜色
    private TextView size; //尺码
    private TextView amount; //数量

    //缝纫机信息
    private SewingInfoEntity sewingEntity;
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

    //站点信息
    private SiteInfoEntity siteInfoEntity;


    private Timer timer;
    private TimerTask timerTask;
    public static int position;
    //==自动轮播
    private boolean isAuto = true;//默认情况下我们是开启自动轮播
    private List<ImageView> imgList = new ArrayList<ImageView>();

    private FrameLayout flayout;
    private LinearLayout redDotLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_homepager, container, false);
        EmpInfoEntity en = (EmpInfoEntity)getArguments().getSerializable(EmpInfoEntity.class.getSimpleName());

        list =  VideoUtils.getVideoFile(new ArrayList<VideoInfo>(), new File(Environment.getExternalStorageDirectory() + "/aps"));//  /DCIM/Video   /Movies/1
        imageList = ImageUtils.getImageFile(new ArrayList<ImageInfo>(), new File(Environment.getExternalStorageDirectory() + "/aps"));//  /DCIM/Video   /Movies/1

        userName = (TextView) view.findViewById(R.id.fh_userName);
        if(en != null) {
            userName.setText("姓名  " + en.getName());
        }
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

        getCache();

        getProductionOrderInfo(Constant.productOrderInfoUrl,
                ShareUtils.getProductOrderId(getActivity()),
                ShareUtils.getColor(getActivity()),
                ShareUtils.getSize(getActivity()));
        getSweingOrderInfo(Constant.sweingInfoUrl, ShareUtils.getSewingId(getActivity()));
        getSiteInfo(Constant.siteInfoUrl, ShareUtils.getSiteId(getActivity()));

        return view;
    }

    private void setAutoViewPager() {
        flayout.setVisibility(View.VISIBLE);
        redDotLayout.removeAllViews();
        imgList.clear();
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

        if(siteFgList.size() > 1 && isTimerStart == false) {
            isTimerStart = true;
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

    VideoPlayerJCFragment videoFragment;
    @Override
    public void videoListener(String videoUrl, int position) {
        if(videoFragment != null) {
            videoFragment.getBDView().onStop();
            videoFragment.getBDView().onDestroy();
            videoFragment = null;
        }
        videoFragment = new VideoPlayerJCFragment();
        String path = VideoUtils.checkContainVideo(list, videoUrl);
        if(!StringUtil.isEmpty(path)) {
            Bundle bundle = new Bundle();
            bundle.putString("videoUrl", path);
            videoFragment.setArguments(bundle);
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.fh_linearlayout, videoFragment)
                    .commit();
        } else {
            Toast.makeText(getActivity(), "无此视频", Toast.LENGTH_SHORT).show();
        }
    }

    InstructBookOnePageFragment bookFragment;
    @Override
    public void bookListener(String bookUrl, int position) {
        if(videoFragment != null) {
            videoFragment.getBDView().onStop();
            videoFragment.getBDView().onDestroy();
            videoFragment = null;
        }
        bookFragment = new InstructBookOnePageFragment();
        String path = ImageUtils.checkContainImage(imageList, bookUrl);
        if(!StringUtil.isEmpty(path)) {
            Bundle bundle = new Bundle();
            bundle.putString("source", path);
            bookFragment.setArguments(bundle);
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.fh_linearlayout, bookFragment)
                    .commit();

            Intent it = new Intent(getActivity(), FullScreenBookActivity.class);
            it.putExtra("source", path);
            startActivity(it);
        } else {
            Toast.makeText(getActivity(), "无此作业指导书", Toast.LENGTH_SHORT).show();
        }

    }


    /**
     * 获取生产单信息
     * @param url
     * @param code
     */
    private void getProductionOrderInfo(String url, String code, String color, String size) {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("code", code)
                .addParams("color", color)
                .addParams("size", size)
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
            productOrderEntity = gson.fromJson(response, ProductOrderInfoEntity.class);
            setProductOrderInfo(productOrderEntity);
            mCache.put(Constant.productOrderCache, productOrderEntity);
        }

        @Override
        public void inProgress(float progress, long total, int id) {
//            mProgressBar.setProgress((int) (100 * progress));
        }
    }

    private void setProductOrderInfo(ProductOrderInfoEntity productOrderEntity) {
        poCode.setText("生产单  " + productOrderEntity.getPoCode()); //生产单
        patternNo.setText("款号  " + productOrderEntity.getPatternNo()); //款号
        customer.setText("顾客  " + productOrderEntity.getCustomer()); //客户
        color.setText("颜色  " + productOrderEntity.getColor()); //颜色
        size.setText("尺码  " + productOrderEntity.getSize()); //尺码
        amount.setText("数量  " + productOrderEntity.getAmount()); //数量
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
            sewingEntity = gson.fromJson(response, SewingInfoEntity.class);
            setSewingInfo(sewingEntity);
            mCache.put(Constant.sewingInfoCache, sewingEntity);
        }

        @Override
        public void inProgress(float progress, long total, int id) {
//            mProgressBar.setProgress((int) (100 * progress));
        }
    }

    private void setSewingInfo(SewingInfoEntity sewingEntity) {
        code.setText(sewingEntity.getCode()); //设备编号
        modelNo.setText(sewingEntity.getModelNo()); //型号
        thisBootTime.setText(sewingEntity.getThisBootTime() +  "分钟"); //本次开机时间
        sumBootTime.setText(sewingEntity.getSumBootTime() + "小时"); //累计开机时间
        pinNum.setText(sewingEntity.getPinNum() + "针"); //本次开机针数
        sumPinNum.setText(sewingEntity.getSumPinNum()); //累计开机针数
        cutLineNum.setText(sewingEntity.getCutLineNum() + "次"); //本次切线数
        sumCutLineNum.setText(sewingEntity.getSumCutLineNum() + "次"); //累计切线数
        presserNum.setText(sewingEntity.getPresserNum() + "次"); //本次压脚抬起数
        sumPresserNum.setText(sewingEntity.getSumPresserNum() + "次"); //累计压脚抬起数
        speed.setText(sewingEntity.getSpeed() + "RPM"); //当前转速
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
            siteInfoEntity = new Gson().fromJson(response, SiteInfoEntity.class);
            setSiteInfo(siteInfoEntity);
            mCache.put(Constant.siteInfoCache, siteInfoEntity);
        }

        @Override
        public void inProgress(float progress, long total, int id) {
//            mProgressBar.setProgress((int) (100 * progress));
        }
    }

    private void setSiteInfo(SiteInfoEntity entity) {
        siteFgList.clear();
        if(entity.getProcessData() != null && entity.getProcessData().size() > 0) {
            for(int i = 0; i < entity.getProcessData().size(); i++) {
//                entity.getProcessData().get(i).setBookUrl(R.mipmap.icon_zyzds1);
//                entity.getProcessData().get(i).setVideoUrl(list.get(i).filePath);
                SiteInfoFragment fragment1 = new SiteInfoFragment();
                fragment1.setOnVideoBookListener(this);
                Bundle bundle1 = new Bundle();
                bundle1.putString("StationCode", entity.getStationCode());
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

//            bookListener(entity.getProcessData().get(0).getBookUrl(), 0);

//            if(videoFragment != null) {
//                videoFragment.getBDView().onStop();
//                videoFragment.getBDView().onDestroy();
//                videoFragment = null;
//            }
//            bookFragment = new InstructBookOnePageFragment();
//            Bundle bundle = new Bundle();
//            bundle.putInt("source", entity.getProcessData().get(0).getBookUrl());
//            bookFragment.setArguments(bundle);
//            getChildFragmentManager().beginTransaction()
//                    .replace(R.id.fh_linearlayout, bookFragment)
//                    .commit();

            if(videoFragment != null) {
                videoFragment.getBDView().onStop();
                videoFragment.getBDView().onDestroy();
                videoFragment = null;
            }

            bookFragment = new InstructBookOnePageFragment();
            String path = ImageUtils.checkContainImage(imageList, entity.getProcessData().get(0).getProcessCode());
            if(!StringUtil.isEmpty(path)) {
                Bundle bundle = new Bundle();
                bundle.putString("source", path);
                bookFragment.setArguments(bundle);
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.fh_linearlayout, bookFragment)
                        .commit();
            } else {
                Toast.makeText(getActivity(), "无此作业指导书", Toast.LENGTH_SHORT).show();
            }

        }

    }

    private void getCache() {
        mCache = ACache.get(getActivity());
        productOrderEntity = (ProductOrderInfoEntity) mCache.getAsObject(Constant.productOrderCache);
        if(productOrderEntity != null) {
            //获取生产单信息缓存
            setProductOrderInfo(productOrderEntity);
        }

        sewingEntity = (SewingInfoEntity) mCache.getAsObject(Constant.sewingInfoCache);
        if(sewingEntity != null) {
            //获取生产单信息缓存
            setSewingInfo(sewingEntity);
        }

        siteInfoEntity = (SiteInfoEntity) mCache.getAsObject(Constant.siteInfoCache);
        if(siteInfoEntity != null) {
            //获取生产单信息缓存
            setSiteInfo(siteInfoEntity);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        mCache.put(Constant.productOrderCache, productOrderEntity);
//        mCache.put(Constant.sewingInfoCache, sewingEntity);
//        mCache.put(Constant.siteInfoCache, siteInfoEntity);

//        ACache mCache = ACache.get(this);
//        mCache.remove(Constant.showingFilmDB);
    }


}
