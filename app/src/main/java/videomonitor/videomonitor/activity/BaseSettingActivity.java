package videomonitor.videomonitor.activity;

import android.os.Bundle;
import videomonitor.videomonitor.R;
import videomonitor.videomonitor.fragment.BaseSettingFragment;


/**
 * Created by Administrator on 2017-09-20.
 */

public class BaseSettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_basesetting);

        BaseSettingFragment bookFragment = new BaseSettingFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.ab_linearlayout, bookFragment)
                .commitAllowingStateLoss();
    }

}
