package videomonitor.videomonitor.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import videomonitor.videomonitor.R;

/**
 * Created by Administrator on 2017-09-06.
 */

public class MyClientFragment extends Fragment {
    private View view;
    private TextView tvName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String name = getArguments().getString("name");
        view = inflater.inflate(R.layout.fragment_my_client, container, false);
        tvName = (TextView) view.findViewById(R.id.fmc_name);
        tvName.setText(name  + "正在开发中。。。");
        return view;
    }
}
