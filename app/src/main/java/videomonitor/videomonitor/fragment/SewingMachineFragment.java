package videomonitor.videomonitor.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import videomonitor.videomonitor.R;
import videomonitor.videomonitor.activity.MainActivity4;
import videomonitor.videomonitor.db.ShareUtils;
import videomonitor.videomonitor.entity.SewingMachineEntity;

/**
 * 平缝机fragment
 * Created by Administrator on 2017-09-14.
 */

public class SewingMachineFragment extends Fragment implements View.OnClickListener {
    private View view;
    //缝纫机信息
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
    private TextView tvLockState; //锁的状态
    private TextView btnLockState; //锁的状态

    private SewingMachineEntity sewingEntity;
    private SewingMachineEntity sewingUnlockEntity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sewing_machine, container, false);

        sewingEntity = (SewingMachineEntity) getArguments().getSerializable(SewingMachineEntity.class.getSimpleName());

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

        tvLockState = (TextView) view.findViewById(R.id.fom_tvLockState); //锁的状态
        btnLockState = (TextView) view.findViewById(R.id.fom_btnLockState); //锁的状态
        btnLockState.setOnClickListener(this);

        code.setText(ShareUtils.getSewingId(getActivity())); //设备编号
        modelNo.setText("MK009"); //型号
        if(sewingEntity != null && sewingEntity.getRetBody() != null) {
            thisBootTime.setText(sewingEntity.getRetBody().getParam3() +  "分钟"); //本次开机时间
            sumBootTime.setText(sewingEntity.getRetBody().getParam4() + "小时"); //累计开机时间
            pinNum.setText(sewingEntity.getRetBody().getParam7() + "针"); //本次开机针数
            sumPinNum.setText(sewingEntity.getRetBody().getParam8()); //累计开机针数
            cutLineNum.setText(sewingEntity.getRetBody().getParam5() + "次"); //本次切线数
            sumCutLineNum.setText(sewingEntity.getRetBody().getParam6() + "次"); //累计切线数
            presserNum.setText(sewingEntity.getRetBody().getParam9() + "次"); //本次压脚抬起数
            sumPresserNum.setText(sewingEntity.getRetBody().getParam10() + "次"); //累计压脚抬起数
            speed.setText(sewingEntity.getRetBody().getParam11() + "RPM"); //当前转速
        }

        if(sewingUnlockEntity == null) {
            tvLockState.setVisibility(View.GONE);
            btnLockState.setVisibility(View.GONE);
        } else {
            tvLockState.setVisibility(View.VISIBLE);
            btnLockState.setVisibility(View.VISIBLE);
            if(sewingUnlockEntity.getRetCode() == 0) {  //解锁成功
                tvLockState.setText("解锁成功");
                btnLockState.setText("锁定");
            } else {  //解锁失败
                tvLockState.setText("解锁失败");
                btnLockState.setText("重新解锁");
            }
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fom_btnLockState:
//                lockStateListener.clickLockState(1,1);//第一个参数(1:包缝机 3:平缝机)； 第二个参数(接口是否调用成功)
                ((MainActivity4)getActivity()).clickOverLockListener(3,1);
                break;
        }
    }
}
