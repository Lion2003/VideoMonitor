package videomonitor.videomonitor.fragment;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import videomonitor.videomonitor.R;

/**
 * Created by Administrator on 2017-06-26.
 */

public class VideoPlayerFragment3 extends Fragment implements OnClickListener {
    private View view;
    private Button bt_play;
    private Button bt_replay;
    private Button bt_stop;
    private Button bt_pause;
    //    private ImageView imgRight;
    private SurfaceView sv;
    private TextView tvTime;

    private String filePath;
    private boolean isFirstInPlay = false;

    private MediaPlayer mediaPlayer;

    private int position;

    private SeekBar seek_bar;

    private Timer timer;
    private TimerTask task;
//    private List<VideoInfo> list;

    private int currentIndex = 0;
    private SimpleDateFormat format;
    private Date date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_video_play, container, false);

        filePath = getArguments().getString("filePath");
        isFirstInPlay = getArguments().getBoolean("isFirstInPlay", false);
        bt_play = (Button) view.findViewById(R.id.bt_play);
        bt_replay = (Button) view.findViewById(R.id.bt_replay);
        bt_stop = (Button) view.findViewById(R.id.bt_stop);
        bt_pause = (Button) view.findViewById(R.id.bt_pause);
        sv = (SurfaceView) view.findViewById(R.id.localSurfaceView);
        seek_bar = (SeekBar) view.findViewById(R.id.seek_bar);
        tvTime = (TextView) view.findViewById(R.id.cm_time);

        seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(mediaPlayer!=null){
                    mediaPlayer.seekTo(seekBar.getProgress());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }
        });


		/* 下面设置Surface不维护自己的缓冲区，而是等待屏幕的渲染引擎将内容推送到用户面前 */
        sv.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        sv.getHolder().addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                System.out.println("holder 被销毁了.");
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    position = mediaPlayer.getCurrentPosition();
                    stop();
                }
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                System.out.println("holder 被创建了.");
                if (position != 0) {
                    play(filePath);
                    mediaPlayer.seekTo(position);
                }

            }

            // 主要是当holder的大小发生变化的时候 调用
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {

            }
        });

//        imgRight.setOnClickListener(this);
        bt_play.setOnClickListener(this);
        bt_replay.setOnClickListener(this);
        bt_stop.setOnClickListener(this);
        bt_pause.setOnClickListener(this);

//        list =  VideoUtils.getVideoFile(new ArrayList<VideoInfo>(), new File(Environment.getExternalStorageDirectory() + "/Movies/1"));
        Log.e("as", "list");

        if(isFirstInPlay) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    play(filePath);
                    bt_play.setEnabled(false);
                }
            }, 1000);
        }


        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_play:
                play(filePath);
                bt_play.setEnabled(false);
                break;
            case R.id.bt_replay:
                replay();
                break;
            case R.id.bt_stop:
                stop();
                break;
            case R.id.bt_pause:
                pause();
                break;
//            case R.id.imgRight:
//                final BannerDlg dlg1 = new BannerDlg(getActivity(), list);
//                dlg1.setOnPageSelectListener(new BannerDlg.OnPageSelectListener() {
//                    @Override
//                    public void onPageSelect(int position) {
//                        mediaPlayer.stop();
//                        mediaPlayer.release();
//                        mediaPlayer = null;
//                        if (timer != null && task != null) {
//                            timer.cancel();
//                            task.cancel();
//                            timer = null;
//                            task = null;
//                        }
//                        currentIndex = position;
//                        play(list.get(currentIndex).filePath);
//                        dlg1.dismiss();
//                    }
//                });
//                dlg1.show();;
//                break;
        }
    }

    /**
     * 重新播放
     */
    private void replay() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo(0);
        } else {
            play(filePath);
        }

    }

    /**
     * 停止播放
     */
    public void stop() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();

            mediaPlayer = null;
            bt_play.setEnabled(true);
            if (timer != null && task != null) {
                timer.cancel();
                task.cancel();
                timer = null;
                task = null;
            }

        }

    }

    /**
     * 暂停播放
     */
    private void pause() {

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            // 暂停播放
            mediaPlayer.pause();
            bt_pause.setText("继续");
            return;
        }

        if (mediaPlayer != null) {
            if ("继续".equals(bt_pause.getText().toString())) {
                mediaPlayer.start();
                bt_pause.setText("暂停");
                return;
            }
        }

    }

    /**
     * 暂停播放
     */
    public void onMyPause() {

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            // 暂停播放
            mediaPlayer.pause();
            bt_pause.setText("继续");
            return;
        }

    }

    /**
     * 播放
     */
    public void play(final String filePath) {
//		String path = et_path.getText().toString().trim();
//        String path = Environment.getExternalStorageDirectory().toString() + "/DCIM/Video/V70319-134847.mp4";
//        String path = Environment.getExternalStorageDirectory().toString() + "/mnt/internal_sd/Movies/1/1.mp4";
//        String path = list.get(0).filePath;
        String path = filePath;
        File file = new File(path);
        if (file.exists() && file.length() > 0) {
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.reset();// 重置为初始状态
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
				/* 设置Video影片以SurfaceHolder播放 */
                mediaPlayer.setDisplay(sv.getHolder());
                mediaPlayer.setDataSource(path);
                mediaPlayer.prepare();
                mediaPlayer.start();// 播放
                int max = mediaPlayer.getDuration();
                final String formatTime = dateFormat(max);
                seek_bar.setMax(max);
                // 创建一个定时器
                timer = new Timer();
                // 创建一个定时器执行的任务
                task = new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            if(mediaPlayer != null) {
                                int position = mediaPlayer.getCurrentPosition();
                                seek_bar.setProgress(position);
                                tvTime.setText(format.format(new Date(position)) + "/" +  formatTime);
                            }
                        } catch(Exception e) {
                            Log.e(VideoPlayerFragment.class.getSimpleName(), e.toString());
                        }
                    }
                };
                timer.schedule(task, 0, 1000);

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
//                        stop();
//                        bt_play.setEnabled(true);
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null;
                        if (timer != null && task != null) {
                            timer.cancel();
                            task.cancel();
                            timer = null;
                            task = null;
                        }
                        play(filePath);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "播放失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String dateFormat(int duration) {
        format = new SimpleDateFormat("mm:ss");
        date = new Date(duration);
        return format.format(date);
    }

    public void releaseMediaPlayer() {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
        if (timer != null && task != null) {
            timer.cancel();
            task.cancel();
            timer = null;
            task = null;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
