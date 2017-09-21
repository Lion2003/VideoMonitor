package videomonitor.videomonitor.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import videomonitor.videomonitor.R;
import videomonitor.videomonitor.db.ShareUtils;
import videomonitor.videomonitor.utils.StringUtil;

/**
 * Created by Administrator on 2017-09-08.
 */

public class BaseSettingFragment extends Fragment implements View.OnClickListener {
    private View view;
    private RadioGroup radioGroup;
    private RadioButton bfj, pfj;
    private EditText productorderId, edColor, edSize, frjId, siteId, url, time;
//    private Spinner spinner;
    private Button btn;

    private int type = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_base_setting, container, false);
        radioGroup = (RadioGroup) view.findViewById(R.id.fbs_radioGroup);
        bfj = (RadioButton) view.findViewById(R.id.fbs_bfj);
        pfj = (RadioButton) view.findViewById(R.id.fbs_pfj);
        productorderId = (EditText) view.findViewById(R.id.producOrderId);
        frjId = (EditText) view.findViewById(R.id.frjId);
        edColor = (EditText) view.findViewById(R.id.color);
        url = (EditText) view.findViewById(R.id.url);
        time = (EditText) view.findViewById(R.id.time);
        edSize = (EditText) view.findViewById(R.id.size);

        siteId = (EditText) view.findViewById(R.id.siteId);
        btn = (Button) view.findViewById(R.id.save);
        btn.setOnClickListener(this);

//        int position = 0;
//        Resources resources =getResources();
//        String[] list = resources.getStringArray(R.array.size_labels);
//        for(int i = 0; i < list.length; i++) {
//            if(ShareUtils.getSize(getActivity()).equals(list[i])) {
//                position = i;
//            }
//        }

        productorderId.setText(ShareUtils.getProductOrderId(getActivity()));
        edColor.setText(ShareUtils.getColor(getActivity()));
        edSize.setText(ShareUtils.getSize(getActivity()));
//        spinner.setSelection(position);
        frjId.setText(ShareUtils.getSewingId(getActivity()));
        siteId.setText(ShareUtils.getSiteId(getActivity()));

//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Resources resources =getResources();
//                String[] list = resources.getStringArray(R.array.size_labels);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.fbs_bfj:
                        type = 1;
                        ShareUtils.setMachineType(getActivity(), 1);
                        break;
                    case R.id.fbs_pfj:
                        type = 3;
                        ShareUtils.setMachineType(getActivity(), 3);
                        break;
                }
            }
        });

        if(ShareUtils.getMachineType(getActivity()) == 1) {
            bfj.setChecked(true);
        } else if(ShareUtils.getMachineType(getActivity()) == 3) {
            pfj.setChecked(true);
        }
        url.setText(ShareUtils.getUrl(getActivity()));
        time.setText(ShareUtils.getTime(getActivity()) + "");
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                if (StringUtil.isEmpty(productorderId.getText().toString())) {
                    Toast.makeText(getActivity(), "请输入生产单编号", Toast.LENGTH_SHORT).show();
                    return;
                } else if(StringUtil.isEmpty(edColor.getText().toString())){
                    Toast.makeText(getActivity(), "请输入生产单颜色", Toast.LENGTH_SHORT).show();
                    return;
                }  else if(StringUtil.isEmpty(edSize.getText().toString())){
                    Toast.makeText(getActivity(), "请输入生产单颜色", Toast.LENGTH_SHORT).show();
                    return;
                } else if(StringUtil.isEmpty(frjId.getText().toString())) {
                    Toast.makeText(getActivity(), "请输入缝纫机编号", Toast.LENGTH_SHORT).show();
                    return;
                } else if(StringUtil.isEmpty(siteId.getText().toString())) {
                    Toast.makeText(getActivity(), "请输入站点编号", Toast.LENGTH_SHORT).show();
                    return;
                } else if(StringUtil.isEmpty(url.getText().toString())) {
                    Toast.makeText(getActivity(), "请输入站点编号", Toast.LENGTH_SHORT).show();
                    return;
                } else if(StringUtil.isEmpty(time.getText().toString())) {
                    Toast.makeText(getActivity(), "请输入刷新时间", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(Integer.parseInt(time.getText().toString()) < 5) {
                    Toast.makeText(getActivity(), "刷新时间要大于5秒", Toast.LENGTH_SHORT).show();
                    return;
                }
                ShareUtils.saveInfo(getActivity(), type, productorderId.getText().toString().trim(),
                        edColor.getText().toString().trim(),
                        edSize.getText().toString(),
                        frjId.getText().toString().trim(),
                        siteId.getText().toString().trim());
                ShareUtils.setUrl(getActivity(), url.getText().toString());
                ShareUtils.setTime(getActivity(), Integer.parseInt(time.getText().toString()));
                Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_SHORT).show();
                break;

        }
    }


}
