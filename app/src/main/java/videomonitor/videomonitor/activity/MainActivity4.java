package videomonitor.videomonitor.activity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import videomonitor.videomonitor.R;
import videomonitor.videomonitor.fragment.HomePagerFragment;
import videomonitor.videomonitor.fragment.InstructBookFragment;
import videomonitor.videomonitor.fragment.InstructionBookListFragment;
import videomonitor.videomonitor.fragment.MineFragment;
import videomonitor.videomonitor.fragment.VideoStandardFragment;
import videomonitor.videomonitor.utils.StringUtil;

@SuppressLint("NewApi")
public class MainActivity4 extends BaseActivity {

    protected static final String TAG = "MainActivity4";

    private Button[] mTabs;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main4);

        CURRENT_ID = getIntent().getStringExtra("CURRENT_ID").trim();
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


}
