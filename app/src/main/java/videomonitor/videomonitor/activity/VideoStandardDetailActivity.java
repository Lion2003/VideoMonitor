package videomonitor.videomonitor.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import videomonitor.videomonitor.R;
import videomonitor.videomonitor.entity.VideoDetailInfo;
import videomonitor.videomonitor.fragment.InstructBookOnePageFragment;
import videomonitor.videomonitor.fragment.VideoPlayerJCFragment;

/**
 * Created by Administrator on 2017-09-05.
 */

public class VideoStandardDetailActivity extends BaseActivity implements View.OnClickListener {
    private int currentState = 0; //0:video, 1:image
    private TextView tvState;
    private VideoPlayerJCFragment videoFragment;
    private InstructBookOnePageFragment bookFragment;

    private VideoDetailInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_standard_detail);

        info = (VideoDetailInfo) getIntent().getSerializableExtra(VideoDetailInfo.class.getSimpleName());
        currentState = getIntent().getIntExtra("currentState", 0);

        tvState = (TextView) findViewById(R.id.avsd_changeState);
        tvState.setOnClickListener(this);

        if(currentState == 0) {
            playVideo();
            tvState.setText("点击查看作业指导书");
        } else if(currentState == 1){
            showBook();
            tvState.setText("点击查看标准视频");
        }

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.avsd_changeState:
                if(currentState == 0) { //如果当前为视频界面
                    showBook();
                    currentState = 1;
                    tvState.setText("点击查看标准视频");
                } else { //如果当前为作业指导书界面
                    playVideo();
                    currentState = 0;
                    tvState.setText("点击查看作业指导书");
                }
                break;

            default:
                break;

        }
    }

    private void playVideo() {
        if(videoFragment != null) {
            videoFragment.getBDView().onStop();
            videoFragment.getBDView().onDestroy();
            videoFragment = null;
        }
        videoFragment = new VideoPlayerJCFragment();
        Bundle bundle = new Bundle();
        bundle.putString("videoUrl", info.getVideoPath());
        videoFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fh_linearlayout, videoFragment)
                .commit();
    }

    private void showBook() {
        if(videoFragment != null) {
            videoFragment.getBDView().onStop();
            videoFragment.getBDView().onDestroy();
            videoFragment = null;
        }
        bookFragment = new InstructBookOnePageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("source", R.mipmap.icon_zyzds1);
        bookFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fh_linearlayout, bookFragment)
                .commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(videoFragment != null) {
            videoFragment.getBDView().onStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(videoFragment != null) {
            videoFragment.getBDView().onDestroy();
        }
    }
}
