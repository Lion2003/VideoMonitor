package videomonitor.videomonitor.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import videomonitor.videomonitor.R;
import videomonitor.videomonitor.db.ShareUtils;
import videomonitor.videomonitor.entity.SewingMachineEntity;

/**
 * 包缝机fragment
 * Created by Administrator on 2017-09-14.
 */

public class OverlockMachineFragment extends Fragment {
    private View view;
    //缝纫机信息
    private TextView code; //设备编号
    private TextView modelNo; //型号
    private TextView thisBootTime; //前剪线累计次数
    private TextView sumBootTime; //后剪线累计次数
    private TextView pinNum; //自动抬压脚前抬累计次数
    private TextView sumPinNum; //自动抬压脚后抬累计次数
    private TextView cutLineNum; //后踏剪线累计次数
    private TextView sumCutLineNum; //一次前后剪线间隔时间
    private TextView presserNum; //前吸气累计次数
    private TextView sumPresserNum; //后吸气累计次数
    private TextView speed; //当前转速

    private SewingMachineEntity sewingEntity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_overlock_machine, container, false);

        sewingEntity = (SewingMachineEntity) getArguments().getSerializable(SewingMachineEntity.class.getSimpleName());

        //实例化缝纫机信息控件
        code = (TextView) view.findViewById(R.id.code); //设备编号
        modelNo = (TextView) view.findViewById(R.id.modelNo); //型号
        thisBootTime = (TextView) view.findViewById(R.id.thisBootTime); //前剪线累计次数
        sumBootTime = (TextView) view.findViewById(R.id.sumBootTime); //后剪线累计次数
        pinNum = (TextView) view.findViewById(R.id.pinNum); //自动抬压脚前抬累计次数
        sumPinNum = (TextView) view.findViewById(R.id.sumPinNum); //自动抬压脚后抬累计次数
        cutLineNum = (TextView) view.findViewById(R.id.cutLineNum); //后踏剪线累计次数
        sumCutLineNum = (TextView) view.findViewById(R.id.sumCutLineNum); //一次前后剪线间隔时间
        presserNum = (TextView) view.findViewById(R.id.presserNum); //前吸气累计次数
        sumPresserNum = (TextView) view.findViewById(R.id.sumPresserNum); //后吸气累计次数
        speed = (TextView) view.findViewById(R.id.speed); ////针数累计次数


        code.setText(ShareUtils.getSewingId(getActivity())); //设备编号
//        modelNo.setText("MK009"); //型号
        if(sewingEntity != null && sewingEntity.getRetBody() != null) {
            switch (sewingEntity.getRetBody().getParam1()) {
                case "0":
                    thisBootTime.setText("待机"); //前剪线累计次数
                    break;
                case "1":
                    thisBootTime.setText("电机运转"); //前剪线累计次数
                    break;
                case "2":
                    thisBootTime.setText("仅模式运转"); //前剪线累计次数
                    break;
                case "3":
                    thisBootTime.setText("电机和模式都运转"); //前剪线累计次数
                    break;
            }
            sumBootTime.setText(sewingEntity.getRetBody().getParam2() + "次"); //后剪线累计次数
            cutLineNum.setText(sewingEntity.getRetBody().getParam3() + "次"); //后踏剪线累计次数
            sumCutLineNum.setText(sewingEntity.getRetBody().getParam4() + "次"); //一次前后剪线间隔时间
//            speed.setText(sewingEntity.getRetBody().getParam5() + "次"); //针数累计次数

            pinNum.setText(sewingEntity.getRetBody().getParam5() + "次"); //自动抬压脚前抬累计次数
            sumPinNum.setText(sewingEntity.getRetBody().getParam6() + "次"); //自动抬压脚后抬累计次数
            presserNum.setText(sewingEntity.getRetBody().getParam7() + "时"); //前吸气累计次数
            sumPresserNum.setText(sewingEntity.getRetBody().getParam8() + "次"); //后吸气累计次数
        }

        return view;
    }

}
