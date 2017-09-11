package videomonitor.videomonitor.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import videomonitor.videomonitor.R;
import videomonitor.videomonitor.db.ShareUtils;
import videomonitor.videomonitor.utils.StringUtil;

/**
 * Created by Administrator on 2017-09-08.
 */

public class BaseSettingFragment extends Fragment implements View.OnClickListener {
    private View view;
    private EditText productorderId, frjId, siteId;
    private Button btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_base_setting, container, false);
        productorderId = (EditText) view.findViewById(R.id.producOrderId);
        frjId = (EditText) view.findViewById(R.id.frjId);
        siteId = (EditText) view.findViewById(R.id.siteId);
        btn = (Button) view.findViewById(R.id.save);
        btn.setOnClickListener(this);

        frjId.setText(ShareUtils.getSewingId(getActivity()));
        siteId.setText(ShareUtils.getSiteId(getActivity()));

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                if (StringUtil.isEmpty(productorderId.getText().toString())) {
                    Toast.makeText(getActivity(), "请输入生产单编号", Toast.LENGTH_SHORT).show();
                    return;
                } else if(StringUtil.isEmpty(frjId.getText().toString())) {
                    Toast.makeText(getActivity(), "请输入缝纫机编号", Toast.LENGTH_SHORT).show();
                    return;
                } else if(StringUtil.isEmpty(siteId.getText().toString())) {
                    Toast.makeText(getActivity(), "请输入站点编号", Toast.LENGTH_SHORT).show();
                    return;
                }
                ShareUtils.saveInfo(getActivity(), productorderId.getText().toString().trim(),
                        frjId.getText().toString().trim(),
                        siteId.getText().toString().trim());
                break;

        }
    }


}
