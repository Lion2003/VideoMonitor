package videomonitor.videomonitor.activity;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import videomonitor.videomonitor.R;
import videomonitor.videomonitor.constant.Constant;
import videomonitor.videomonitor.db.ShareUtils;
import videomonitor.videomonitor.entity.EmpInfoEntity;
import videomonitor.videomonitor.fragment.HomePagerFragment;
import videomonitor.videomonitor.fragment.InstructBookFragment;
import videomonitor.videomonitor.fragment.InstructionBookListFragment;
import videomonitor.videomonitor.fragment.MineFragment;
import videomonitor.videomonitor.fragment.VideoStandardFragment;
import videomonitor.videomonitor.utils.StringUtil;

@SuppressLint("NewApi")
public class MainActivity4 extends BaseActivity implements View.OnClickListener {

    protected static final String TAG = "MainActivity4";

    private Button[] mTabs;
    private Button btnLogout;
    private HomePagerFragment homePagerFragment;
    private VideoStandardFragment videoStandardFragment;
    private InstructionBookListFragment instructBookFragment;
    private MineFragment mineFragment;
    private List<Fragment> fragments;
    private int index;
    private int currentTabIndex;
    // user logged into another device
    public boolean isConflict = false;
    // user account was removed
    private boolean isCurrentAccountRemoved = false;

    private String CURRENT_ID;
    private EmpInfoEntity empInfoEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main4);

        empInfoEntity= (EmpInfoEntity) getIntent().getSerializableExtra(EmpInfoEntity.class.getSimpleName());
        CURRENT_ID = getIntent().getStringExtra("CURRENT_ID");
        if(StringUtil.isEmpty(CURRENT_ID)) {
            CURRENT_ID = "2007001023";
        }

        initView();

        homePagerFragment = new HomePagerFragment();
        videoStandardFragment = new VideoStandardFragment();
        instructBookFragment = new InstructionBookListFragment();
        mineFragment = new MineFragment();

        Bundle bundle1 = new Bundle();
        bundle1.putString("CURRENT_ID", CURRENT_ID);
        bundle1.putSerializable(EmpInfoEntity.class.getSimpleName(), empInfoEntity);
        homePagerFragment.setArguments(bundle1);

        Bundle bundle3 = new Bundle();
        bundle3.putBoolean("isShowTitle", true);
        instructBookFragment.setArguments(bundle3);

        fragments = new ArrayList<Fragment>(); //{ conversationListFragment, contactListFragment, workbenchFragment, settingFragment};
        fragments.add(homePagerFragment);
        fragments.add(videoStandardFragment);
        fragments.add(instructBookFragment);
        fragments.add(mineFragment);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, homePagerFragment)
                .add(R.id.fragment_container, videoStandardFragment)
                .add(R.id.fragment_container, instructBookFragment)
                .add(R.id.fragment_container, mineFragment)
                .hide(videoStandardFragment)
                .hide(instructBookFragment)
                .hide(mineFragment)
                .show(homePagerFragment)
                .commit();
    }

    /**
     * init views
     */
    private void initView() {
        btnLogout = (Button) findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(this);

        mTabs = new Button[4];
        mTabs[0] = (Button) findViewById(R.id.btn_workbench);
        mTabs[1] = (Button) findViewById(R.id.btn_conversation);
        mTabs[2] = (Button) findViewById(R.id.btn_address_list);
        mTabs[3] = (Button) findViewById(R.id.btn_setting);
        // select first tab
        mTabs[0].setSelected(true);
    }

    /**
     * on tab clicked
     *
     * @param view
     */
    public void onTabClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_workbench:
                index = 0;
                break;
            case R.id.btn_conversation:
                index = 1;
                break;
            case R.id.btn_address_list:
                index = 2;
                break;
            case R.id.btn_setting:
                index = 3;
                break;
        }
//		if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
//			getSupportFragmentManager().popBackStack();
//		}

        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(fragments.get(currentTabIndex));
            if (!fragments.get(index).isAdded()) {
                trx.add(R.id.fragment_container, fragments.get(index));
            }
            trx.show(fragments.get(index)).commit();
        }
        mTabs[currentTabIndex].setSelected(false);
        // set current tab selected
        mTabs[index].setSelected(true);
        currentTabIndex = index;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private long mPressedTime = 0;

    @Override
    public void onBackPressed() {
        long mNowTime = System.currentTimeMillis();//获取第一次按键时间
        if((mNowTime - mPressedTime) > 2000){//比较两次按键时间差
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mPressedTime = mNowTime;
        }else{//退出程序
           this.finish();
           System.exit(0);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_logout:
                Dialog myDialog = new AlertDialog.Builder(this)
                        .setIcon(R.mipmap.ic_launcher)
                        .setTitle("提示")
                        .setMessage("是否退出登录")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface myDialog, int which) {
                                homePagerFragment.removeCache();
                                Intent it = new Intent(MainActivity4.this, LoginActivity.class);
                                startActivity(it);
                                MainActivity4.this.finish();
//                                System.exit(0);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create();
                myDialog.show() ;
                break;
        }
    }

    public void clickOverLockListener(int type, int state) {
        Toast.makeText(this, type + ", "+ state, Toast.LENGTH_SHORT).show();
        homePagerFragment.getSweingOrderInfo(Constant.sweingInfoUrl1,
                ShareUtils.getMachineType(this),
                ShareUtils.getSewingId(this));
    }
}
