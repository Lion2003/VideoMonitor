package videomonitor.videomonitor.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.WindowManager;

import com.boredream.bdvideoplayer.BDVideoView;
import com.boredream.bdvideoplayer.listener.SimpleOnVideoControlListener;
import com.boredream.bdvideoplayer.utils.DisplayUtils;

import videomonitor.videomonitor.R;
import videomonitor.videomonitor.entity.VideoDetailInfo;

/**
 * 全屏播放视频
 * Created by Administrator on 2017-09-11.
 */

public class FullScreenActivity extends BaseActivity {
    private String videoUrl;
    private BDVideoView videoView;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_full_screen);

        videoUrl = getIntent().getStringExtra("videoUrl");

        VideoDetailInfo info = new VideoDetailInfo();
        info.title = "标题";
        info.videoPath = videoUrl;

        videoView = (BDVideoView) findViewById(R.id.vv);
        videoView.setOnVideoControlListener(new SimpleOnVideoControlListener() {

            @Override
            public void onRetry(int errorStatus) {
                // TODO: 2017/6/20 调用业务接口重新获取数据
                // get info and call method "videoView.startPlayVideo(info);"
            }

            @Override
            public void onBack() {
//                onBackPressed();
            }

            @Override
            public void onFullScreen() {
//                DisplayUtils.toggleScreenOrientation(FullScreenActivity.this);
            }
        });
//        info.videoPath = "/mnt/sdcard/Movies/1/1.mp4";
        videoView.startPlayVideo(info);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        videoView.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();

        videoView.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        videoView.onDestroy();
    }

//    @Override
//    public void onBackPressed() {
//        if (!DisplayUtils.isPortrait(this)) {
//            if(!videoView.isLock()) {
//                DisplayUtils.toggleScreenOrientation(this);
//            }
//        } else {
//            super.onBackPressed();
//        }
//    }
}
