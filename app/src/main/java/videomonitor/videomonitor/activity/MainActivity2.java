package videomonitor.videomonitor.activity;

import android.hardware.Camera;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;
import android.app.Activity;
import android.support.v7.widget.Toolbar;
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
import videomonitor.videomonitor.VideoUtils;
import videomonitor.videomonitor.adapter.VideoPlayerAdapter;
import videomonitor.videomonitor.entity.VideoInfo;
import videomonitor.videomonitor.fragment.VideoPlayerFragment;
import videomonitor.videomonitor.utils.ScreenUtils;

/**
 * Created by lenovo on 2017/6/21.
 */

public class MainActivity2 extends AppCompatActivity implements OnClickListener {
    private ImageView header;
    private TabLayout tablayout;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private List<VideoInfo> list;

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
        setContentView(R.layout.activity_main2);

        header = (ImageView) findViewById(R.id.cm_header);
        tablayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        int height = ScreenUtils.getScreenHeight(this);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) header.getLayoutParams();
        params.width = (int)((height / 3) * 0.6);
        params.height = (int)((height / 3) * 0.6);;
        header.setLayoutParams(params);
        header.setImageResource(R.mipmap.icon_timg);

        list =  VideoUtils.getVideoFile(new ArrayList<VideoInfo>(), new File(Environment.getExternalStorageDirectory() + "/Movies/1"));
        Log.e("as", "list");
        viewPager.setOffscreenPageLimit(list.size());

        final List<VideoPlayerFragment> fragmentList = new ArrayList<VideoPlayerFragment>();
        for (int i = 0; i < list.size(); i++) {
            tablayout.addTab(tablayout.newTab().setText(list.get(i).displayName));
            VideoPlayerFragment fragment = new VideoPlayerFragment();
            Bundle bundle = new Bundle();
            bundle.putString("filePath", list.get(i).filePath);
            if(i == 0) {
                bundle.putBoolean("isFirstInPlay", true);
            } else {
                bundle.putBoolean("isFirstInPlay", false);
            }
            fragment.setArguments(bundle);
            fragmentList.add(fragment);
        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for(int i = 0; i < list.size(); i++) {
                    if (i != position) {
                        fragmentList.get(i).stop();
                    } else {
                        fragmentList.get(i).play(list.get(i).filePath);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        VideoPlayerAdapter adapter = new VideoPlayerAdapter(getSupportFragmentManager(), list, fragmentList);
        viewPager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewPager);
        tablayout.setTabsFromPagerAdapter(adapter);

        surfaceView = (SurfaceView) findViewById(R.id.surface);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(new SurfaceViewCallback());
        //surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                if(viewPager.getCurrentItem() < list.size() - 1) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                } else {
                    viewPager.setCurrentItem(0, true);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

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
                Toast.makeText(MainActivity2.this, "[Test] Photo take and store in" + file.toString(),Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(MainActivity2.this, "Picture Failed" + e.toString(),
                        Toast.LENGTH_LONG).show();
            }
        };
    };

    public void scanFileToPhotoAlbum(String path) {

        MediaScannerConnection.scanFile(MainActivity2.this,
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
                setCameraDisplayOrientation(MainActivity2.this, mCurrentCamIndex, mCamera);
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
