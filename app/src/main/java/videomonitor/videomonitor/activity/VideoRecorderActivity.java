package videomonitor.videomonitor.activity;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.IOException;
import java.util.List;

import videomonitor.videomonitor.R;

/**
 * Created by Administrator on 2017-07-03.
 */

public class VideoRecorderActivity extends AppCompatActivity implements SurfaceHolder.Callback,MediaPlayer.OnPreparedListener, MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnCompletionListener {
    private static final String TAG = "RecordActivity";

    private String videoPath = "/sdcard/love.3gp";

    private MediaRecorder mediarecorder;// 录制视频的类
    private MediaPlayer mediaPlayer;//播放视频的类
    private SurfaceView surfaceview;// 显示视频的控件
    private Camera camera;
    //实现这个接口的Callback接口
    private SurfaceHolder surfaceHolder;
    /**
     * 是否正在录制true录制中 false未录制
     */
    private boolean isRecord = false;

    public boolean isCameraBack = true;

    private ImageView recordIv;
    private ImageView recordPlayIv;

    private int mVideoWidth;
    private int mVideoHeight;
    int cameraCount = 0;

    private int cameraPosition = 1;//0代表前置摄像头，1代表后置摄像头

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_video_record1);
//        setTitleStr("");
        // 选择支持半透明模式,在有surfaceview的activity中使用。
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        initView();
    }

    private void initView() {
        surfaceview = (SurfaceView) this.findViewById(R.id.surfaceview);
        recordIv = (ImageView) findViewById(R.id.recordIv);
        recordPlayIv = (ImageView) findViewById(R.id.recordPlayIv);

        SurfaceHolder holder = surfaceview.getHolder();// 取得holder
        holder.addCallback(this); // holder加入回调接口
        // setType必须设置，要不出错.
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        recordIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordVideo(v);
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recordVideo(null);
            }
        }, 1000);
    }

    /**
     * 播放视频
     *
     * @param v
     */
    public void playVideo(View v) {
        recordPlayIv.setVisibility(View.GONE);
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(videoPath);
            mediaPlayer.setDisplay(surfaceHolder);
            mediaPlayer.prepareAsync();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

//    @Override
//    protected void onDestroy() {
//        if (mediarecorder != null) {
//            // 停止录制
//            mediarecorder.stop();
//            // 释放资源
//            mediarecorder.release();
//            mediarecorder = null;
//        }
//        if (camera != null) {
//            camera.release();
//        }
//        super.onDestroy();
//    }

    @Override
    public void onBackPressed() {
        if (mediarecorder != null) {
            // 停止录制
            mediarecorder.stop();
            // 释放资源
            mediarecorder.release();
            mediarecorder = null;
        }
        if (camera != null) {
            camera.release();
        }
        super.onBackPressed();
    }

    /**
     * 开始录制/停止录制
     *
     * @param v
     */
    public void recordVideo(View v) {
        if (isRecord) {
            isRecord = false;
            recordIv.setImageResource(R.mipmap.video_recorder_start_btn_nor);
            recordPlayIv.setVisibility(View.VISIBLE);
            if (mediarecorder != null) {
                // 停止录制
                mediarecorder.stop();
                // 释放资源
                mediarecorder.release();
                mediarecorder = null;
            }
            if (camera != null) {
                camera.release();
            }
            recordIv.setVisibility(View.GONE);
        } else {
            isRecord = true;
            recordIv.setImageResource(R.mipmap.video_recorder_recording_btn);
            recordPlayIv.setVisibility(View.GONE);
            mediarecorder = new MediaRecorder();// 创建mediarecorder对象
//			// 从麦克风源进行录音
//			mediarecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
//			// 设置输出格式
//			mediarecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
//			// 设置编码格式
//			mediarecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            /**
             * 设置竖着录制
             */
            if (camera != null) {
                camera.release();
            }

//			if(cameraPosition==1){
//				camera = Camera.open(CameraInfo.CAMERA_FACING_BACK);//打开摄像头
////				 camera = Camera.open(cameraPosition);//打开摄像头
////		         Camera.Parameters parameters = camera.getParameters();
////		         camera.setDisplayOrientation(90);
//
////		         camera.setParameters(parameters);
//
//		         camera=deal(camera);
//		         mediarecorder.setOrientationHint(90);//视频旋转90度
//			}else{
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);//打开摄像头
            Camera.Parameters parameters = camera.getParameters();
//		         camera.setDisplayOrientation(90);
            camera.setParameters(parameters);
//				mediarecorder.setOrientationHint(270);//视频旋转90度
//			}
            camera.unlock();

            mediarecorder.setCamera(camera);
            // 设置录制视频源为Camera(相机)
            mediarecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            // 设置录制完成后视频的封装格式THREE_GPP为3gp.MPEG_4为mp4
            mediarecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            // 设置录制的视频编码h263 h264
            mediarecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            // 设置视频录制的分辨率。必须放在设置编码和格式的后面，否则报错
//			mediarecorder.setVideoSize(176, 144);
//			// 设置录制的视频帧率。必须放在设置编码和格式的后面，否则报错
//			mediarecorder.setVideoFrameRate(20);

            mediarecorder.setPreviewDisplay(surfaceHolder.getSurface());
            // 设置视频文件输出的路径
            mediarecorder.setOutputFile(videoPath);
            try {
                // 准备录制
                mediarecorder.prepare();
                mediarecorder.start();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    int mCurrentCamIndex = 0;

    @Override
    public void surfaceChanged(SurfaceHolder holder, int arg1, int arg2, int arg3) {
        // 将holder，这个holder为开始在oncreat里面取得的holder，将它赋给surfaceHolder
//		surfaceHolder = holder;
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
            setCameraDisplayOrientation(this, mCurrentCamIndex, camera);
        } catch (Exception e) {
        }
    }

    //���ݺ������Զ�����preview����Starting from API level 14, this method can be called when preview is active.
    private static void setCameraDisplayOrientation(Activity activity, int cameraId, Camera camera) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();

        //degrees  the angle that the picture will be rotated clockwise. Valid values are 0, 90, 180, and 270.
        //The starting position is 0 (landscape).
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {
            // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    /* (non-Javadoc)
     * @see android.view.SurfaceHolder.Callback#surfaceCreated(android.view.SurfaceHolder)
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        surfaceHolder = holder;

        camera = openFrontFacingCameraGingerbread();
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

    /* (non-Javadoc)
     * @see android.view.SurfaceHolder.Callback#surfaceDestroyed(android.view.SurfaceHolder)
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        if (camera != null) {
            camera.release();
        }
        surfaceview = null;
        surfaceHolder = null;
        if (surfaceHolder != null) {
            surfaceHolder = null;
        }
        if (mediarecorder != null) {
            mediarecorder = null;
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    /* (non-Javadoc)
     * @see android.media.MediaPlayer.OnCompletionListener#onCompletion(android.media.MediaPlayer)
     */
    @Override
    public void onCompletion(MediaPlayer arg0) {
        Log.i(TAG, "onCompletion");
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        recordPlayIv.setVisibility(View.VISIBLE);
    }

    /* (non-Javadoc)
     * @see android.media.MediaPlayer.OnPreparedListener#onPrepared(android.media.MediaPlayer)
     */
    @Override
    public void onPrepared(MediaPlayer arg0) {
        mVideoWidth = mediaPlayer.getVideoWidth();
        mVideoHeight = mediaPlayer.getVideoHeight();
        if (mVideoWidth != 0 && mVideoHeight != 0) {
		   /* 设置视频的宽度和高度 */
            surfaceHolder.setFixedSize(mVideoWidth, mVideoHeight);

		   /* 开始播放 */
            mediaPlayer.start();
        }
    }

    /* (non-Javadoc)
     * @see android.media.MediaPlayer.OnBufferingUpdateListener#onBufferingUpdate(android.media.MediaPlayer, int)
     */
    @Override
    public void onBufferingUpdate(MediaPlayer player, int arg1) {

    }
}