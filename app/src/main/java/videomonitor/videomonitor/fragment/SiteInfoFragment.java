package videomonitor.videomonitor.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import videomonitor.videomonitor.R;
import videomonitor.videomonitor.entity.SiteInfoEntity.ProcessDataBean;

/**
 * 站点信息
 * Created by Administrator on 2017-08-31.
 */

public class SiteInfoFragment extends Fragment implements View.OnClickListener{
    private View view;
    private TextView siteNum; //站点
    private TextView processNumber; //工序编号
    private TextView processName; //工序名称
    private TextView standardHour; //标准工时
    private TextView processPrice; //工序单价
    private TextView todayPlanNumber; //今日计划数
    private TextView todayCompanyNumber; //今日完成数
    private TextView todayPlanSalary; //今日计划工资
    private TextView todayGotSalary; //今日已拿工资
    private Button btnBook;
    private Button btnVideo;

    private ProcessDataBean entity;
    private int position;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_site_info, container, false);

        entity = (ProcessDataBean) getArguments().getSerializable("entity");

        siteNum = (TextView) view.findViewById(R.id.fsi_siteNum); //站点
        processNumber = (TextView) view.findViewById(R.id.fsi_processNumber); //工序编号
        processName = (TextView) view.findViewById(R.id.fsi_processName); //工序名称
        standardHour = (TextView) view.findViewById(R.id.fsi_standardHour); //标准工时
        processPrice = (TextView) view.findViewById(R.id.fsi_processPrice); //工序单价
        todayPlanNumber = (TextView) view.findViewById(R.id.fsi_todayPlanNumber); //今日计划数
        todayCompanyNumber = (TextView) view.findViewById(R.id.fsi_todayCompanyNumber); //今日完成数
        todayPlanSalary = (TextView) view.findViewById(R.id.fsi_todayPlanSalary); //今日计划工资
        todayGotSalary = (TextView) view.findViewById(R.id.fsi_todayGotSalary); //今日已拿工资
        btnBook = (Button) view.findViewById(R.id.fsi_watchBook);
        btnVideo = (Button) view.findViewById(R.id.fsi_watchVideo);

        siteNum.setText("未知");
        processNumber.setText(entity.getProcessCode());
        processName.setText(entity.getProcessName());
        standardHour.setText(entity.getStandardHour() + "秒");
        processPrice.setText(entity.getUnitPrice() + "元/秒");
        todayPlanNumber.setText(entity.getPlanNumber() + "件");
        todayCompanyNumber.setText(entity.getFinishNumber() + "件");
        todayPlanSalary.setText(entity.getPlanWages() + "元");
        todayGotSalary.setText(entity.getTakenWages() + "元");

        btnBook.setOnClickListener(this);
        btnVideo.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fsi_watchBook:
                listener.bookListener(entity.getProcessCode(), position);
                break;
            case R.id.fsi_watchVideo:
                listener.videoListener(entity.getProcessCode(), position);
                break;
        }
    }
    public VideoBookListener listener;
    public void setOnVideoBookListener(VideoBookListener listener) {
        this.listener = listener;
    }
    public interface VideoBookListener{
        void videoListener(String videoUrl, int position);
        void bookListener(String bookUrl, int position);
    }
}
