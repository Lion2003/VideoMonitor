package videomonitor.videomonitor.activity;

import android.hardware.Camera;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.app.Activity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import videomonitor.videomonitor.R;
import videomonitor.videomonitor.VideoUtils;
import videomonitor.videomonitor.dialog.BannerDlg;
import videomonitor.videomonitor.entity.VideoInfo;
import videomonitor.videomonitor.utils.ScreenUtils;

/**
 * Created by lenovo on 2017/6/21.
 */

public class MainActivity1 extends AppCompatActivity implements OnClickListener {
    private ImageView header;
    private Button bt_play;
    private Button bt_replay;
    private Button bt_stop;
    private Button bt_pause;
    private ImageView imgRight;
    private SurfaceView sv;
    private TextView tvTime;

//    private Button btnBanner;

    private MediaPlayer mediaPlayer;

    private int position;

    private SeekBar seek_bar;

    private Timer timer;
    private TimerTask task;
    private List<VideoInfo> list;

    private int currentIndex = 0;
    private SimpleDateFormat format;
    private Date date;


    //=========================================================
    static final String TAG =  "CAMERA ACTIVITY";
    //Camera object
    Camera mCamera;
    //Preview surface
    SurfaceView surfaceView;
    //Preview surface handle for callback
    SurfaceHolder surfaceHolder;
    //Camera button
    Button btnCapture;
    //Note if preview windows is on.
    boolean previewing;

    int mCurrentCamIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.requestWindowFeature(Window.FEATURE_NO_TITLE);//不显示标题
//        super.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);	// 全屏显示
        super.getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 高亮显示
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("车缝监控");
        setSupportActionBar(toolbar);
        imgRight = (ImageView) findViewById(R.id.imgRight);
        header = (ImageView) findViewById(R.id.cm_header);
        bt_play = (Button) findViewById(R.id.bt_play);
        bt_replay = (Button) findViewById(R.id.bt_replay);
        bt_stop = (Button) findViewById(R.id.bt_stop);
        bt_pause = (Button) findViewById(R.id.bt_pause);
        sv = (SurfaceView) findViewById(R.id.localSurfaceView);
        seek_bar = (SeekBar) findViewById(R.id.seek_bar);
        tvTime = (TextView) findViewById(R.id.cm_time);

//        btnBanner = (Button) findViewById(R.id.myBtn);

        int height = ScreenUtils.getScreenHeight(this);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) header.getLayoutParams();
        params.width = (int)((height / 3) * 0.6);
        params.height = (int)((height / 3) * 0.6);;
        header.setLayoutParams(params);
        header.setImageResource(R.mipmap.icon_timg);

        seek_bar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
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

        sv.getHolder().addCallback(new Callback() {

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
                    play(list.get(currentIndex).filePath);
                    mediaPlayer.seekTo(position);
                }

            }

            // 主要是当holder的大小发生变化的时候 调用
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {

            }
        });

        imgRight.setOnClickListener(this);
        bt_play.setOnClickListener(this);
        bt_replay.setOnClickListener(this);
        bt_stop.setOnClickListener(this);
        bt_pause.setOnClickListener(this);
//        btnBanner.setOnClickListener(this);

        list =  VideoUtils.getVideoFile(new ArrayList<VideoInfo>(), new File(Environment.getExternalStorageDirectory() + "/Movies/1"));
        Log.e("as", "list");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                play(list.get(currentIndex).filePath);
                bt_play.setEnabled(false);
            }
        }, 1000);


//        btnCapture = (Button) findViewById(R.id.btn_capture);
//        btnCapture.setOnClickListener(new Button.OnClickListener() {
//            public void onClick(View arg0) {
//                if (previewing)
//                    mCamera.takePicture(shutterCallback, rawPictureCallback,
//                            jpegPictureCallback);
//            }
//        });
        surfaceView = (SurfaceView) findViewById(R.id.surface);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(new SurfaceViewCallback());
        //surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_play:
                play(list.get(currentIndex).filePath);
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
//            case R.id.myBtn:
//                final BannerDlg dlg = new BannerDlg(this, list);
//                dlg.setOnPageSelectListener(new BannerDlg.OnPageSelectListener() {
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
//                        dlg.dismiss();
//                    }
//                });
//                dlg.show();;
//                break;
            case R.id.imgRight:
                final BannerDlg dlg1 = new BannerDlg(this, list);
                dlg1.setOnPageSelectListener(new BannerDlg.OnPageSelectListener() {
                    @Override
                    public void onPageSelect(int position) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null;
                        if (timer != null && task != null) {
                            timer.cancel();
                            task.cancel();
                            timer = null;
                            task = null;
                        }
                        currentIndex = position;
                        play(list.get(currentIndex).filePath);
                        dlg1.dismiss();
                    }
                });
                dlg1.show();;
                break;
        }

    }

    /**
     * 重新播放
     */
    private void replay() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo(0);
        } else {
            play(list.get(currentIndex).filePath);
        }

    }

    /**
     * 停止播放
     */
    private void stop() {
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
     * 播放
     */
    private void play(String filePath) {
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
                             Log.e(TAG, e.toString());
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
                        currentIndex = currentIndex + 1;
                        if(currentIndex < list.size()) {
                            play(list.get(currentIndex).filePath);
                        } else {
                            currentIndex = 0;
                            play(list.get(currentIndex).filePath);
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "播放失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String dateFormat(int duration) {
        format = new SimpleDateFormat("mm:ss");
        date = new Date(duration);
        return format.format(date);
    }

    private long mPressedTime = 0;

    @Override
    public void onBackPressed() {
        long mNowTime = System.currentTimeMillis();//获取第一次按键时间
        if((mNowTime - mPressedTime) > 2000){//比较两次按键时间差
           Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
           mPressedTime = mNowTime;
        }else{//退出程序
            this.finish();
            System.exit(0);
        }
    }

    //=======================================================================================================
    Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {
        }
    };

    Camera.PictureCallback rawPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] arg0, Camera arg1) {

        }
    };

    Camera.PictureCallback jpegPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] arg0, Camera arg1) {

            String fileName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                    .toString()
                    + File.separator
                    + "PicTest_" + System.currentTimeMillis() + ".jpg";
            File file = new File(fileName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdir();
            }

            try {
                BufferedOutputStream bos = new BufferedOutputStream(
                        new FileOutputStream(file));
                bos.write(arg0);
                bos.flush();
                bos.close();
                scanFileToPhotoAlbum(file.getAbsolutePath());
                Toast.makeText(MainActivity1.this, "[Test] Photo take and store in" + file.toString(),Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(MainActivity1.this, "Picture Failed" + e.toString(),
                        Toast.LENGTH_LONG).show();
            }
        };
    };

    public void scanFileToPhotoAlbum(String path) {

        MediaScannerConnection.scanFile(MainActivity1.this,
                new String[] { path }, null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("TAG", "Finished scanning " + path);
                    }
                });
    }
    private final class SurfaceViewCallback implements SurfaceHolder.Callback {
        public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3)
        {
            if (previewing) {
                mCamera.stopPreview();
                previewing = false;
            }

            try {
                mCamera.setPreviewDisplay(arg0);
                mCamera.startPreview();
                previewing = true;
                setCameraDisplayOrientation(MainActivity1.this, mCurrentCamIndex, mCamera);
            } catch (Exception e) {}
        }
        public void surfaceCreated(SurfaceHolder holder) {
//				mCamera = Camera.open();
            //change to front camera
            mCamera = openFrontFacingCameraGingerbread();
            // get Camera parameters
            Camera.Parameters params = mCamera.getParameters();

            List<String> focusModes = params.getSupportedFocusModes();
            if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                // Autofocus mode is supported
            }
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
            previewing = false;
        }
    }

    private Camera openFrontFacingCameraGingerbread() {
        int cameraCount = 0;
        Camera cam = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();

        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                try {
                    cam = Camera.open(camIdx);
                    mCurrentCamIndex = camIdx;
                } catch (RuntimeException e) {
                    Log.e(TAG, "Camera failed to open: " + e.getLocalizedMessage());
                }
            }
        }

        return cam;
    }

    //���ݺ������Զ�����preview����Starting from API level 14, this method can be called when preview is active.
    private static void setCameraDisplayOrientation(Activity activity,int cameraId, Camera camera)
    {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();

        //degrees  the angle that the picture will be rotated clockwise. Valid values are 0, 90, 180, and 270.
        //The starting position is 0 (landscape).
        int degrees = 0;
        switch (rotation)
        {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT)
        {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        }
        else
        {
            // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

}
