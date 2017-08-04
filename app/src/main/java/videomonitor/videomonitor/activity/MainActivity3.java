package videomonitor.videomonitor.activity;

import android.hardware.Camera;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import android.os.Environment;
import android.app.Activity;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import videomonitor.videomonitor.R;
import videomonitor.videomonitor.fragment.InstructBookFragment;
import videomonitor.videomonitor.fragment.MessageFragment;
import videomonitor.videomonitor.fragment.VideoViewPagerFragment;
import videomonitor.videomonitor.utils.ScreenUtils;

/**
 * Created by lenovo on 2017/6/21.
 */

public class MainActivity3 extends AppCompatActivity implements OnClickListener {
    private ImageView header;
    private Button btnStart, btnStop;

    private MessageFragment messageFragment = new MessageFragment();
    private VideoViewPagerFragment videoViewFragment = new VideoViewPagerFragment();
    private InstructBookFragment instructFragment = new InstructBookFragment();

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
//      super.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);	// 全屏显示
        super.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 高亮显示
        setContentView(R.layout.activity_main3);

        header = (ImageView) findViewById(R.id.cm_header);
        btnStart = (Button) findViewById(R.id.btnStart);
        btnStop = (Button) findViewById(R.id.btnStop);

//        btnVideo.setOnClickListener(this);
//        btnBook.setOnClickListener(this);
        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);

        int height = ScreenUtils.getScreenHeight(this);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) header.getLayoutParams();
        params.width = (int)((height / 3) * 0.6);
        params.height = (int)((height / 3) * 0.6);
        header.setLayoutParams(params);
        header.setImageResource(R.mipmap.icon_timg);

        surfaceView = (SurfaceView) findViewById(R.id.surface);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(new SurfaceViewCallback());
        //surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

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
                Toast.makeText(MainActivity3.this, "[Test] Photo take and store in" + file.toString(),Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(MainActivity3.this, "Picture Failed" + e.toString(),
                        Toast.LENGTH_LONG).show();
            }
        };
    };

    public void scanFileToPhotoAlbum(String path) {

        MediaScannerConnection.scanFile(MainActivity3.this,
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
                setCameraDisplayOrientation(MainActivity3.this, mCurrentCamIndex, mCamera);
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

}
