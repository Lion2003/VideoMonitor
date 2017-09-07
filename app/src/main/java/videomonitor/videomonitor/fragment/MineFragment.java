package videomonitor.videomonitor.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import videomonitor.videomonitor.R;

/**
 * Created by Administrator on 2017-08-31.
 */

public class MineFragment extends Fragment {
    private View view;
    private RadioGroup radioGroup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine, container, false);

        radioGroup = (RadioGroup) view.findViewById(R.id.fm_radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.fm_myCollection:
                        setMyColloctionFragment();
                        break;
                    case R.id.fm_myClient:
                        setFragment("我的客户");
                        break;
                    case R.id.fm_myTaskManager:
                        setFragment("任务管理");
                        break;
                    case R.id.fm_myCompany:
                        setFragment("我的公司");
                        break;
                    case R.id.fm_myNews:
                        setFragment("我的消息");
                        break;
                    case R.id.fm_myBasicSettings:
                        setFragment("基本设置");
                        break;
                    default:

                        break;
                }
            }
        });
        setMyColloctionFragment();
        return view;
    }

    private void setFragment(String name) {
        MyClientFragment clientFragment = new MyClientFragment();
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        clientFragment.setArguments(bundle);
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fm_linearLayout, clientFragment)
                .commit();
    }

    private void setMyColloctionFragment() {
        MyCollectionFragment collectionFragment = new MyCollectionFragment();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fm_linearLayout, collectionFragment)
                .commit();
    }
}
