package videomonitor.videomonitor.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boredream.bdvideoplayer.BDVideoView;
import com.boredream.bdvideoplayer.listener.SimpleOnVideoControlListener;
import com.boredream.bdvideoplayer.utils.DisplayUtils;
import com.bumptech.glide.Glide;

import videomonitor.videomonitor.R;
import videomonitor.videomonitor.activity.FullScreenActivity;
import videomonitor.videomonitor.entity.VideoDetailInfo;

/**
 * 播放视频
 * Created by Administrator on 2017-08-31.
 */

public class VideoPlayerJCFragment extends Fragment {
    private View view;
    private String videoUrl;
    private BDVideoView videoView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_videoplayer_jc, container, false);
        videoUrl = getArguments().getString("videoUrl");

//        JCVideoPlayerStandard jcVideoPlayerStandard = (JCVideoPlayerStandard) view.findViewById(R.id.jc_video);
//        jcVideoPlayerStandard.setUp(videoUrl, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "嫂子闭眼睛");
////        jcVideoPlayerStandard.thumbImageView.setImage("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640");
//        Glide.with(getActivity()).load(videoUrl).into(jcVideoPlayerStandard.thumbImageView);

        VideoDetailInfo info = new VideoDetailInfo();
        info.title = "标题";
        info.videoPath = videoUrl;

        videoView = (BDVideoView) view.findViewById(R.id.vv);
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
//                DisplayUtils.toggleScreenOrientation(getActivity());
                Intent it = new Intent(getActivity(), FullScreenActivity.class);
                it.putExtra("videoUrl", videoUrl);
                startActivity(it);
            }
        });
//        info.videoPath = "/mnt/sdcard/Movies/1/1.mp4";
        videoView.startPlayVideo(info);
        videoView.onStart();
        return view;
    }

    public BDVideoView getBDView() {
        return videoView;
    }

}
