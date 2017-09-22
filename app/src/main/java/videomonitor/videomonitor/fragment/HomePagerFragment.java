package videomonitor.videomonitor.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Request;
import videomonitor.videomonitor.ImageUtils;
import videomonitor.videomonitor.MyApplication;
import videomonitor.videomonitor.R;
import videomonitor.videomonitor.VideoUtils;
import videomonitor.videomonitor.activity.FullScreenBookActivity;
import videomonitor.videomonitor.adapter.SiteAdapter;
import videomonitor.videomonitor.constant.Constant;
import videomonitor.videomonitor.db.ShareUtils;
import videomonitor.videomonitor.entity.EmpInfoEntity;
import videomonitor.videomonitor.entity.ImageInfo;
import videomonitor.videomonitor.entity.LockState;
import videomonitor.videomonitor.entity.Machine;
import videomonitor.videomonitor.entity.ProductOrderInfoEntity;
import videomonitor.videomonitor.entity.SewingMachineEntity;
import videomonitor.videomonitor.entity.SiteInfoEntity;
import videomonitor.videomonitor.entity.VideoInfo;
import videomonitor.videomonitor.utils.ACache;
import videomonitor.videomonitor.utils.StringUtil;

/**
 * Created by Administrator on 2017-08-31.
 */

public class HomePagerFragment extends Fragment implements SiteInfoFragment.VideoBookListener {
    private View view;
    private TabLayout tablayout;
    private ViewPager mViewPager;
    private SiteAdapter adapter;
    private List<SiteInfoFragment> siteFgList = new ArrayList<SiteInfoFragment>();
    private List<String> titles = new ArrayList<String>();

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
    private SewingMachineEntity sewingEntity;
    private SewingMachineEntity sewingUnlockEntity;

    //站点信息
    private SiteInfoEntity siteInfoEntity;
    private TextView siteNum; //站点

    private Timer timer;
    private TimerTask task;
    private EmpInfoEntity en;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_homepager, container, false);
        en = (EmpInfoEntity)getArguments().getSerializable(EmpInfoEntity.class.getSimpleName());

        list =  VideoUtils.getVideoFile(new ArrayList<VideoInfo>(), new File(Environment.getExternalStorageDirectory() + "/aps"));//  /DCIM/Video   /Movies/1
        imageList = ImageUtils.getImageFile(new ArrayList<ImageInfo>(), new File(Environment.getExternalStorageDirectory() + "/aps"));//  /DCIM/Video   /Movies/1

        userName = (TextView) view.findViewById(R.id.fh_userName);
        if(en != null) {
            userName.setText("姓名  " + en.getName());
        }
        currentId = (TextView) view.findViewById(R.id.current_id);
        String id =  getArguments().getString("CURRENT_ID", "2017001023");
        currentId.setText("工号 " + en.getEmpCode());

        //实例化生产单信息控件
        poCode = (TextView) view.findViewById(R.id.poCode); //生产单
        patternNo = (TextView) view.findViewById(R.id.patternNo); //款号
        customer = (TextView) view.findViewById(R.id.customer); //客户
        color = (TextView) view.findViewById(R.id.color); //颜色
        size = (TextView) view.findViewById(R.id.size); //尺码
        amount = (TextView) view.findViewById(R.id.amount); //数量

        //实例化站点信息控件
        tablayout = (TabLayout) view.findViewById(R.id.tabs);
        mViewPager = (ViewPager) view.findViewById(R.id.fh_viewPager);
        siteNum = (TextView) view.findViewById(R.id.fsi_siteNum); //站点

        getCache();

        task = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                message.obj = System.currentTimeMillis();
                handler.sendMessage(message);
            }
        };
        timer = new Timer();
        // 参数：
        // 1000，延时1秒后执行。
        // 60000，每隔60秒执行1次task。
        timer.schedule(task, 1000, ShareUtils.getTime(getActivity()) * 1000);

        getProductionOrderInfo(Constant.productOrderInfoUrl,
                ShareUtils.getProductOrderId(getActivity()),
                ShareUtils.getColor(getActivity()),
                ShareUtils.getSize(getActivity()));

        //解锁操作
        if(ShareUtils.getMachineType(getActivity()) == 1) { //如果是包缝机，那么解锁包缝机
            MyApplication.isLockState = 1;
            getSweingOrderInfo(Constant.sweingInfoUrl1,
                    2,
                    ShareUtils.getSewingId(getActivity()), new Gson().toJson(new LockState(2, 1, ShareUtils.getSewingId(getActivity()))));
        } else if(ShareUtils.getMachineType(getActivity()) == 3) { //如果是平缝机， 那么解锁平缝机
            MyApplication.isLockState = 1;
            getSweingOrderInfo(Constant.sweingInfoUrl1,
                    4,
                    ShareUtils.getSewingId(getActivity()), new Gson().toJson(new LockState(4, 1, ShareUtils.getSewingId(getActivity()))));
        } else if(ShareUtils.getMachineType(getActivity()) == 5) { //如果是下摆机， 那么解锁下摆机
            MyApplication.isLockState = 1;
            getSweingOrderInfo(Constant.sweingInfoUrl1,
                    6,
                    ShareUtils.getSewingId(getActivity()), new Gson().toJson(new LockState(6, 1, ShareUtils.getSewingId(getActivity()))));
        }
        return view;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    getSweingOrderInfo(Constant.sweingInfoUrl1,
                            ShareUtils.getMachineType(getActivity()),
                            ShareUtils.getSewingId(getActivity()),
                            new Gson().toJson(new Machine(ShareUtils.getMachineType(getActivity()),ShareUtils.getSewingId(getActivity()))));
                    getSiteInfo(Constant.siteInfoUrl,
                            ShareUtils.getSiteId(getActivity()));
                    break;
            }
        }
    };

    VideoPlayerJCFragment videoFragment;
    @Override
    public void videoListener(String videoUrl, int position) {
        try{
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
                        .commitAllowingStateLoss();
            } else {
                Toast.makeText(getActivity(), "无此视频", Toast.LENGTH_SHORT).show();
            }
        } catch(Exception e) {

        }
    }

    InstructBookOnePageFragment bookFragment;
    @Override
    public void bookListener(String bookUrl, int position) {
        try {
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
                        .commitAllowingStateLoss();

                Intent it = new Intent(getActivity(), FullScreenBookActivity.class);
                it.putExtra("source", path);
                startActivity(it);
            } else {
                Toast.makeText(getActivity(), "无此作业指导书", Toast.LENGTH_SHORT).show();
            }
        } catch(Exception e) {

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
            try {
                Gson gson = new Gson();
                productOrderEntity = gson.fromJson(response, ProductOrderInfoEntity.class);
                setProductOrderInfo(productOrderEntity);
            mCache.put(Constant.productOrderCache, productOrderEntity);
            } catch(Exception e) {

            }
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

//    public class Machine {
//        private int type;
//        private String deviceNo;
//
//        public Machine(int type, String deviceNo) {
//            this.type = type;
//            this.deviceNo = deviceNo;
//        }
//    }
//
//    public class LockState {
//        private int type;
//        private int isLock;
//        private String deviceNo;
//
//        public LockState(int type, int isLock, String deviceNo) {
//            this.type = type;
//            this.isLock = isLock;
//            this.deviceNo = deviceNo;
//        }
//    }


    /**
     * 获取缝纫机信息
     * @param url 接口url
     * @param type  1:包缝机 2:解锁包缝机 3:平缝机 4:解锁平缝机
     * @param deviceNo  设备编码
     */
    public void getSweingOrderInfo(String url, int type, String deviceNo, String json) {
        OkHttpUtils
                .postString()
                .url(url)
                .content(json)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(new MySweingCallback(type));
    }

    public class MySweingCallback extends StringCallback {
        private int type;

        public MySweingCallback(int type) {
            this.type = type;
        }

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
            try{
                if(type == 1 || type == 3 || type == 5) {
                    Gson gson = new Gson();
                    sewingEntity = gson.fromJson(response, SewingMachineEntity.class);
                    setSewingInfo(sewingEntity);
                    mCache.put(Constant.sewingInfoCache, sewingEntity);
                } else if(type == 2 || type == 4 || type == 6) {
                    Gson gson = new Gson();
                    sewingUnlockEntity = gson.fromJson(response, SewingMachineEntity.class);
                    setSewingInfo(sewingEntity);
                }
            } catch(Exception e) {

            }
        }

        @Override
        public void inProgress(float progress, long total, int id) {
//            mProgressBar.setProgress((int) (100 * progress));
        }
    }

    private void setSewingInfo(SewingMachineEntity sewingEntity) {
        if(ShareUtils.getMachineType(getActivity()) == 3) { //平缝机
            SewingMachineFragment sewingMachineFragment = new SewingMachineFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(SewingMachineEntity.class.getSimpleName(), sewingEntity);
            bundle.putSerializable("lockState", sewingUnlockEntity);
            sewingMachineFragment.setArguments(bundle);

            getChildFragmentManager().beginTransaction()
                    .replace(R.id.fh_sewingMachineLayout, sewingMachineFragment)
                    .commitAllowingStateLoss();
        } else if(ShareUtils.getMachineType(getActivity()) == 1) { //包缝机
            OverlockMachineFragment machineFragment = new OverlockMachineFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(SewingMachineEntity.class.getSimpleName(), sewingEntity);
            bundle.putSerializable("lockState", sewingUnlockEntity);
            machineFragment.setArguments(bundle);

            getChildFragmentManager().beginTransaction()
                    .replace(R.id.fh_sewingMachineLayout, machineFragment)
                    .commitAllowingStateLoss();
        }  else if(ShareUtils.getMachineType(getActivity()) == 5) { //包缝机
            RimMachineFragment machineFragment = new RimMachineFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(SewingMachineEntity.class.getSimpleName(), sewingEntity);
            bundle.putSerializable("lockState", sewingUnlockEntity);
            machineFragment.setArguments(bundle);

            getChildFragmentManager().beginTransaction()
                    .replace(R.id.fh_sewingMachineLayout, machineFragment)
                    .commitAllowingStateLoss();
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
                .addParams("empCode", en.getEmpCode())
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
            try {
                siteInfoEntity = new Gson().fromJson(response, SiteInfoEntity.class);
                setSiteInfo(siteInfoEntity);
            mCache.put(Constant.siteInfoCache, siteInfoEntity);
            } catch(Exception e) {

            }

        }

        @Override
        public void inProgress(float progress, long total, int id) {
//            mProgressBar.setProgress((int) (100 * progress));
        }
    }

    private void setSiteInfo(SiteInfoEntity entity) {
        siteFgList.clear();
        titles.clear();
        if(entity.getProcessData() != null && entity.getProcessData().size() > 0) {
            for(int i = 0; i < entity.getProcessData().size(); i++) {
                SiteInfoFragment fragment1 = new SiteInfoFragment();
                fragment1.setOnVideoBookListener(this);
                Bundle bundle1 = new Bundle();
                bundle1.putString("StationCode", entity.getStationCode());
                bundle1.putSerializable("entity", entity.getProcessData().get(i));
                bundle1.putInt("position", i);
                fragment1.setArguments(bundle1);
                siteFgList.add(fragment1);
                titles.add("工序 " + entity.getProcessData().get(i).getProcessCode());
            }

//            mViewPager.setOffscreenPageLimit(entity.getProcessData().size());
            siteNum.setText(entity.getStationCode());
            if(siteFgList.size() == 1) {
                tablayout.setVisibility(View.GONE);
            } else if(siteFgList.size() >1) {
                tablayout.setVisibility(View.VISIBLE);
            }
            if(adapter == null) {
                adapter = new SiteAdapter(getChildFragmentManager(), titles, siteFgList);
                mViewPager.setAdapter(adapter);
                tablayout.setupWithViewPager(mViewPager);
                tablayout.setTabsFromPagerAdapter(adapter);

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
                            .commitAllowingStateLoss();
                } else {
                    Toast.makeText(getActivity(), "无此作业指导书", Toast.LENGTH_SHORT).show();
                }
            } else {
                adapter.notifyDataSetChanged();
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

        sewingEntity = (SewingMachineEntity) mCache.getAsObject(Constant.sewingInfoCache);
        if(sewingEntity != null) {
            //获取缝纫机信息缓存
            setSewingInfo(sewingEntity);
        } else {
            setSewingInfo(sewingEntity);
        }

        siteInfoEntity = (SiteInfoEntity) mCache.getAsObject(Constant.siteInfoCache);
        if(siteInfoEntity != null) {
            //获取站点信息缓存
            setSiteInfo(siteInfoEntity);
        }
    }

    public void removeCache() {
        timer.cancel();

        mCache.remove(Constant.empInfoCache); //获取员工信息缓存
        mCache.remove(Constant.productOrderCache); //获取生产单信息缓存
        mCache.remove(Constant.sewingInfoCache); //缝纫机信息缓存
        mCache.remove(Constant.siteInfoCache); //站点信息缓存
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
