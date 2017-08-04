package videomonitor.videomonitor.activity;

import android.content.Intent;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.view.SurfaceHolder.Callback;
import android.widget.TextView;
import android.widget.Toast;

import videomonitor.videomonitor.R;
import videomonitor.videomonitor.fragment.InstructBookFragment;
import videomonitor.videomonitor.fragment.MessageFragment;
import videomonitor.videomonitor.fragment.VideoViewPagerFragment;
import videomonitor.videomonitor.utils.DensityUtils;
import videomonitor.videomonitor.utils.ScreenUtils;

/**
 * Created by lenovo on 2017/6/21.
 */

public class MainActivity31 extends AppCompatActivity implements Callback,MediaPlayer.OnPreparedListener, MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnCompletionListener, OnClickListener {
    private ImageView header;
//    private Button btnStart, btnStop;

    private MessageFragment messageFragment = new MessageFragment();
    private VideoViewPagerFragment videoViewFragment = new VideoViewPagerFragment();
    private InstructBookFragment instructFragment = new InstructBookFragment();

    //=========================================================
    private static final String TAG="RecordActivity";

    private String videoPath="/sdcard/love.3gp";

    private MediaRecorder mediarecorder;// 录制视频的类
    private MediaPlayer mediaPlayer;//播放视频的类
    private SurfaceView surfaceview;// 显示视频的控件
    private  Camera camera;
    //实现这个接口的Callback接口
    private SurfaceHolder surfaceHolder;
    /**
     * 是否正在录制true录制中 false未录制
     */
    private boolean isRecord=false;

    public boolean isCameraBack=true;

    private ImageView recordIv;
    private ImageView recordPlayIv;

    private int mVideoWidth;
    private int mVideoHeight;
    int cameraCount = 0;

    private int cameraPosition = 1;//0代表前置摄像头，1代表后置摄像头

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.requestWindowFeature(Window.FEATURE_NO_TITLE);//不显示标题
//      super.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);	// 全屏显示
        super.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 高亮显示
        setContentView(R.layout.activity_main31);


        TextView tvNum = (TextView) findViewById(R.id.cm_number);
        tvNum.setText("编号：" + getIntent().getStringExtra("CURRENT_ID").trim());

        header = (ImageView) findViewById(R.id.cm_header);
//        btnStart = (Button) findViewById(R.id.btnStart);
//        btnStop = (Button) findViewById(R.id.btnStop);

//        btnVideo.setOnClickListener(this);
//        btnBook.setOnClickListener(this);
//        btnStart.setOnClickListener(this);
//        btnStop.setOnClickListener(this);

        int height = ScreenUtils.getScreenHeight(this);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) header.getLayoutParams();
        params.width = (int)((height / 3) * 0.6);
        params.height = (int)((height / 3) * 0.6);
        header.setLayoutParams(params);
        header.setImageResource(R.mipmap.icon_timg);


//        setTitleStr("");
        initView();


        getSupportFragmentManager().beginTransaction().add(R.id.cm3_relativeLayout, instructFragment).add(R.id.cm3_relativeLayout, messageFragment).
                add(R.id.cm3_relativeLayout, videoViewFragment).show(instructFragment).show(messageFragment).hide(videoViewFragment).commit();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
//                if(viewPager.getCurrentItem() < list.size() - 1) {
//                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
//                } else {
//                    viewPager.setCurrentItem(0, true);
//                }
                onBackPressed();
            }
        });
    }

    public void showVideo1() {
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.animator_one_enter,
                R.anim.animator_one_exit,R.anim.animator_two_enter,R.anim.animator_two_exit)
                .hide(messageFragment).show(videoViewFragment).hide(instructFragment).commit();
    }

    public void showInstruct() {
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.animator_one_enter,
                R.anim.animator_one_exit,R.anim.animator_two_enter,R.anim.animator_two_exit)
                .hide(messageFragment).hide(videoViewFragment).show(instructFragment).commit();
//        InstruductDialog dlg = new InstruductDialog(this);
//        dlg.show();
//        sv.setVisibility(View.VISIBLE);
//        ((RelativeLayout)findViewById(R.id.cm3_relativeLayout)).setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.fm_video:

                break;
            case R.id.btnStart:

                break;
            case R.id.btnStop:

                break;
        }
    }

    private long mPressedTime = 0;

    @Override
    public void onBackPressed() {

        switch(MessageFragment.TAG) {
            case "MessageFragment":
                long mNowTime = System.currentTimeMillis();//获取第一次按键时间
                if((mNowTime - mPressedTime) > 2000){//比较两次按键时间差
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    mPressedTime = mNowTime;
                }else{//退出程序
                    this.finish();
                    System.exit(0);
                }
                break;
            case "VideoViewPagerFragment":
                getSupportFragmentManager().beginTransaction().hide(videoViewFragment).hide(instructFragment).show(messageFragment).commit();
                videoViewFragment.pauseAllVideo();
                break;
            case "InstructBookFragment":
                getSupportFragmentManager().beginTransaction().hide(videoViewFragment).hide(instructFragment).show(messageFragment).commit();
//                sv.setVisibility(View.GONE);
//                ((RelativeLayout)findViewById(R.id.cm3_relativeLayout)).setVisibility(View.VISIBLE);
                break;
        }
        MessageFragment.TAG = "MessageFragment";

    }


    //=======================================================================================================
    private void initView() {
        surfaceview = (SurfaceView) this.findViewById(R.id.surfaceview);
        recordIv=(ImageView)findViewById(R.id.recordIv);
        recordPlayIv=(ImageView)findViewById(R.id.recordPlayIv);

        SurfaceHolder holder = surfaceview.getHolder();// 取得holder
        holder.addCallback(this); // holder加入回调接口
        // setType必须设置，要不出错.
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        recordIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                videoViewFragment.stopAllVideo();
//                recordVideo(v);
                Intent it = new Intent(MainActivity31.this, VideoRecorderActivity.class);
                startActivity(it);
            }
        });
    }

    /**
     * 播放视频
     * @param v
     */
    public void playVideo(View v){
        recordPlayIv.setVisibility(View.GONE);
        try {
            mediaPlayer=new MediaPlayer();
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

    /**
     * 开始录制/停止录制
     * @param v
     */
    public void recordVideo(View v){
        if(isRecord){
            isRecord=false;
            recordIv.setImageResource(R.mipmap.video_recorder_start_btn_nor);
            recordPlayIv.setVisibility(View.VISIBLE);
            if (mediarecorder != null) {
                // 停止录制
                mediarecorder.stop();
                // 释放资源
                mediarecorder.release();
                mediarecorder = null;
            }
            if(camera!=null){
                camera.release();
            }
            recordIv.setVisibility(View.GONE);
        }else{
            isRecord=true;
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
            if(camera!=null){
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

//    protected void setTitleStr(String str) {
//        TextView titleText = (TextView) findViewById(R.id.common_title_text);
//        titleText.setText(str.trim());
//
//        Button left_button=(Button)findViewById(R.id.left_button);
//        left_button.setVisibility(View.GONE);
//
//        Button right_button=(Button)findViewById(R.id.right_button);
//        right_button.setVisibility(View.VISIBLE);
//        right_button.setText("");
//        right_button.setBackgroundResource(R.drawable.btn_video_switch_bg);
//        right_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                cameraCount=Camera.getNumberOfCameras();
//                if(isCameraBack){
//                    isCameraBack=false;
//                }else{
//                    isCameraBack=true;
//                }
////					SurfaceHolder holder = surfaceview.getHolder();// 取得holder
////					holder.addCallback(RecordActivity.this); // holder加入回调接口
////					LogsUtil.i(TAG, "cameraCount="+cameraCount);
//
//                int cameraCount = 0;
//                Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
//                cameraCount = Camera.getNumberOfCameras();//得到摄像头的个数
//
//                for(int i = 0; i < cameraCount; i++) {
//
//                    Camera.getCameraInfo(i, cameraInfo);//得到每一个摄像头的信息
//                    if(cameraPosition == 1) {
//                        //现在是后置，变更为前置
//                        if(cameraInfo.facing  == Camera.CameraInfo.CAMERA_FACING_FRONT) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
//                            camera.stopPreview();//停掉原来摄像头的预览
//                            camera.release();//释放资源
//                            camera = null;//取消原来摄像头
//                            camera = Camera.open(i);//打开当前选中的摄像头
//                            try {
//                                deal(camera);
//                                camera.setPreviewDisplay(surfaceHolder);//通过surfaceview显示取景画面
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                            camera.startPreview();//开始预览
//                            cameraPosition = 0;
//                            break;
//                        }
//                    } else {
//                        //现在是前置， 变更为后置
//                        if(cameraInfo.facing  == Camera.CameraInfo.CAMERA_FACING_BACK) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
//                            camera.stopPreview();//停掉原来摄像头的预览
//                            camera.release();//释放资源
//                            camera = null;//取消原来摄像头
//                            camera = Camera.open(i);//打开当前选中的摄像头
//                            try {
//                                deal(camera);
//                                camera.setPreviewDisplay(surfaceHolder);//通过surfaceview显示取景画面
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                            camera.startPreview();//开始预览
//                            cameraPosition = 1;
//                            break;
//                        }
//                    }
//                }
//            }
//        });
//    }

    //    public Camera deal(Camera camera){
//        //设置camera预览的角度，因为默认图片是倾斜90度的
//        camera.setDisplayOrientation(90);
//
//        Camera.Size pictureSize=null;
//        Camera.Size previewSize=null;
//        Camera.Parameters parameters = camera.getParameters();
//        parameters.setPreviewFrameRate(5);
//        //设置旋转代码
//        parameters.setRotation(90);
////			parameters.setPictureFormat(PixelFormat.JPEG);
//
//        List<Camera.Size> supportedPictureSizes
//                = SupportedSizesReflect.getSupportedPictureSizes(parameters);
//        List<Camera.Size> supportedPreviewSizes
//                = SupportedSizesReflect.getSupportedPreviewSizes(parameters);
//
//        if ( supportedPictureSizes != null &&
//                supportedPreviewSizes != null &&
//                supportedPictureSizes.size() > 0 &&
//                supportedPreviewSizes.size() > 0) {
//            //2.x
//            pictureSize = supportedPictureSizes.get(0);
//
//            int maxSize = 1280;
//            if(maxSize > 0){
//                for(Camera.Size size : supportedPictureSizes){
//                    if(maxSize >= Math.max(size.width,size.height)){
//                        pictureSize = size;
//                        break;
//                    }
//                }
//            }
//
//            WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//            Display display = windowManager.getDefaultDisplay();
//            DisplayMetrics displayMetrics = new DisplayMetrics();
//            display.getMetrics(displayMetrics);
//
//            previewSize = getOptimalPreviewSize(supportedPreviewSizes, display.getWidth(), display.getHeight());
//
//            parameters.setPictureSize(pictureSize.width, pictureSize.height);
//            parameters.setPreviewSize(previewSize.width, previewSize.height);
//
//        }
////			camera.setParameters(parameters);
//        return camera;
//    }
    /* (non-Javadoc)
     * @see android.view.SurfaceHolder.Callback#surfaceChanged(android.view.SurfaceHolder, int, int, int)
     */
    int mCurrentCamIndex = 0;
    @Override
    public void surfaceChanged(SurfaceHolder holder, int arg1, int arg2, int arg3) {
        // 将holder，这个holder为开始在oncreat里面取得的holder，将它赋给surfaceHolder
//		surfaceHolder = holder;
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
            setCameraDisplayOrientation(this, mCurrentCamIndex, camera);
        } catch (Exception e) {}
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
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        }
        else {
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

//		try {
//			if(isCameraBack){
//				camera = Camera.open(CameraInfo.CAMERA_FACING_BACK);//打开摄像头
//			}else{
//				camera = Camera.open(CameraInfo.CAMERA_FACING_FRONT);//打开摄像头
//			}
//			//设置camera预览的角度，因为默认图片是倾斜90度的
//			camera.setDisplayOrientation(90);
//
//			Size pictureSize=null;
//			Size previewSize=null;
//			Camera.Parameters parameters = camera.getParameters();
//			parameters.setPreviewFrameRate(5);
//			//设置旋转代码
//			parameters.setRotation(90);
////			parameters.setPictureFormat(PixelFormat.JPEG);
//
//			List<Size> supportedPictureSizes
//			= SupportedSizesReflect.getSupportedPictureSizes(parameters);
//			List<Size> supportedPreviewSizes
//			= SupportedSizesReflect.getSupportedPreviewSizes(parameters);
//
//			if ( supportedPictureSizes != null &&
//					supportedPreviewSizes != null &&
//					supportedPictureSizes.size() > 0 &&
//					supportedPreviewSizes.size() > 0) {
//
//					//2.x
//					pictureSize = supportedPictureSizes.get(0);
//
//					int maxSize = 1280;
//					if(maxSize > 0){
//						for(Size size : supportedPictureSizes){
//							if(maxSize >= Math.max(size.width,size.height)){
//								pictureSize = size;
//								break;
//							}
//						}
//					}
//
//					WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//					Display display = windowManager.getDefaultDisplay();
//					DisplayMetrics displayMetrics = new DisplayMetrics();
//					display.getMetrics(displayMetrics);
//
//					previewSize = getOptimalPreviewSize(supportedPreviewSizes,
//										display.getWidth(), display.getHeight());
//
//					parameters.setPictureSize(pictureSize.width, pictureSize.height);
//					parameters.setPreviewSize(previewSize.width, previewSize.height);
//				}
//			camera.setParameters(parameters);
//			camera.setPreviewDisplay(holder);
//			camera.startPreview();

//		} catch (Exception e) {
//			e.printStackTrace();
//		}
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
        if(camera!=null){
            camera.release();
        }
        surfaceview = null;
        surfaceHolder = null;
        if (surfaceHolder != null) {
            surfaceHolder=null;
        }
        if (mediarecorder != null) {
            mediarecorder=null;
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
        if (mVideoWidth != 0 && mVideoHeight != 0){
		   /* 设置视频的宽度和高度 */
            surfaceHolder.setFixedSize(mVideoWidth,mVideoHeight);

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
