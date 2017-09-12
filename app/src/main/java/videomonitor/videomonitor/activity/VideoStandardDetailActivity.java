package videomonitor.videomonitor.activity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import videomonitor.videomonitor.ImageUtils;
import videomonitor.videomonitor.R;
import videomonitor.videomonitor.VideoUtils;
import videomonitor.videomonitor.entity.ImageInfo;
import videomonitor.videomonitor.entity.VideoDetailInfo;
import videomonitor.videomonitor.entity.VideoInfo;
import videomonitor.videomonitor.fragment.InstructBookOnePageFragment;
import videomonitor.videomonitor.fragment.VideoPlayerJCFragment;
import videomonitor.videomonitor.utils.StringUtil;

/**
 * Created by Administrator on 2017-09-05.
 */

public class VideoStandardDetailActivity extends BaseActivity implements View.OnClickListener {
    private int currentState = 0; //0:video, 1:image
    private TextView tvState;
    private VideoPlayerJCFragment videoFragment;
    private InstructBookOnePageFragment bookFragment;

    private VideoDetailInfo info;

    private List<VideoInfo> list;
    private List<ImageInfo> imageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_standard_detail);

        info = (VideoDetailInfo) getIntent().getSerializableExtra(VideoDetailInfo.class.getSimpleName());
        currentState = getIntent().getIntExtra("currentState", 0);

        list =  VideoUtils.getVideoFile(new ArrayList<VideoInfo>(), new File(Environment.getExternalStorageDirectory() + "/DCIM"));//  /DCIM/Video   /Movies/1
        imageList = ImageUtils.getImageFile(new ArrayList<ImageInfo>(), new File(Environment.getExternalStorageDirectory() + "/DCIM"));//  /DCIM/Video   /Movies/1

        tvState = (TextView) findViewById(R.id.avsd_changeState);
        tvState.setOnClickListener(this);

        if(currentState == 0) {
            playVideo();
            tvState.setText("点击查看作业指导书");
        } else if(currentState == 1){
            String path = ImageUtils.checkContainImage(imageList, info.getVideoTitle());
            if(!StringUtil.isEmpty(path)) {
                showBook(path);
            } else {
                Toast.makeText(this, "无作业指导书", Toast.LENGTH_SHORT).show();
            }
//            showBook();
            tvState.setText("点击查看标准视频");
        }

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.avsd_changeState:
                if(currentState == 0) { //如果当前为视频界面
                    String path = ImageUtils.checkContainImage(imageList, info.getVideoTitle());
                    if(!StringUtil.isEmpty(path)) {
                        showBook(path);
                        currentState = 1;
                        tvState.setText("点击查看标准视频");
                    } else {
                        Toast.makeText(this, "无作业指导书", Toast.LENGTH_SHORT).show();
                    }
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

    private void showBook(String path) {
        if(videoFragment != null) {
            videoFragment.getBDView().onStop();
            videoFragment.getBDView().onDestroy();
            videoFragment = null;
        }
        bookFragment = new InstructBookOnePageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("source", path);
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
