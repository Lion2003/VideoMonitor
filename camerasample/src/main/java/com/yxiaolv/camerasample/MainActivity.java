package com.yxiaolv.camerasample;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

public class MainActivity extends Activity {
	private Context mContext;
	private Camera mCamera;
	private PowerManager.WakeLock mWakeLock;
	private Bitmap bmp;
	private SurfaceView mSurfaceView;
	private LinearLayout llBottom, llCamera, llSave, llShare;
	private SurfaceHolder holder;
	private Handler handler = new Handler();
	private Runnable runnable = new Runnable() {

		@Override
		public void run() {
			llBottom.setVisibility(View.GONE);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = this;
		initWakeLock();
		initSurfaceView();
		initBottomLayout();
		/**********************************************
		 * 友盟自动更新
		 **********************************************/
		UmengUpdateAgent.update(this);
	}

	private void initBottomLayout() {
		llBottom = (LinearLayout) findViewById(R.id.ll_bottom);
		llCamera = (LinearLayout) findViewById(R.id.ll_camera);
		llSave = (LinearLayout) findViewById(R.id.ll_save);
		llShare = (LinearLayout) findViewById(R.id.ll_share);
		// 拍照
		llCamera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mCamera.autoFocus(new AutoFocusCallback() {

					@Override
					public void onAutoFocus(boolean focused, Camera arg1) {
						takePicture();
					}
				});
			}
		});
		// 保存照片
		llSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					ImageTool.saveImage(bmp, "MirrorPic", "mp", 100);
					Toast.makeText(MainActivity.this, "照片保存成功",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(MainActivity.this, "操作SDCard失败，请检查是否插入SD卡",
							Toast.LENGTH_SHORT).show();
				}
				llSave.setVisibility(View.GONE);
				llShare.setVisibility(View.GONE);
				handler.postDelayed(runnable, 5000);
				mCamera.startPreview();
			}
		});

		// 分享照片
		llShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent shareIntent = new Intent(Intent.ACTION_SEND);
				shareIntent.putExtra(Intent.EXTRA_STREAM, Uri
						.fromFile(ImageTool.saveImage(bmp, "MirrorPic", "mp",
								100)));
				shareIntent.setType("image/*");
				startActivity(Intent.createChooser(shareIntent, "分享照片"));
				llSave.setVisibility(View.GONE);
				llShare.setVisibility(View.GONE);
				handler.postDelayed(runnable, 5000);
				mCamera.startPreview();
			}
		});
	}

	private void initSurfaceView() {
		mSurfaceView = (SurfaceView) findViewById(R.id.mSurfaceView);
		holder = mSurfaceView.getHolder();
		// 添加回调监听
		holder.addCallback(new Callback() {

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				stopPreviewAndReleaseCamera();
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				int cameraCount = 0;
				Camera cam = null;
				Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
				cameraCount = Camera.getNumberOfCameras();
				for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
					Camera.getCameraInfo(camIdx, cameraInfo);
					if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
						try {
							mCamera = Camera.open(camIdx);
						} catch (RuntimeException e) {
						}
					}
				}

//				mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
				mCamera.setDisplayOrientation(90);
			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
				setCameraParametersAndStartPreview(holder);
			}
		});

		// SurfaceView触摸监听，实现隐藏或显示底部操作栏
		mSurfaceView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				if (llBottom.isShown()) {
					llBottom.setVisibility(View.GONE);
				} else {
					llBottom.setVisibility(View.VISIBLE);
					handler.postDelayed(runnable, 5000);
				}
				return false;
			}
		});
	}

	// 停止预览并释放相机
	private void stopPreviewAndReleaseCamera() {
		mCamera.stopPreview();
		mCamera.release();
		mCamera = null;
	}

	private void setCameraParametersAndStartPreview(SurfaceHolder holder) {
		try {
			Camera.Parameters parameters = mCamera.getParameters();
			// 设置预览照片大小
			parameters.setPreviewSize(ScreenUtils.getScreenWidth(mContext),
					ScreenUtils.getScreenHeight(mContext));
			// 设置预览照片时每秒显示多少帧最小值和最大值
			parameters.setPreviewFpsRange(4, 10);
			// 设置图片格式
			parameters.setPictureFormat(PixelFormat.RGB_565);
			// 设置照片大小
			parameters.setPictureSize(ScreenUtils.getScreenWidth(mContext),
					ScreenUtils.getScreenHeight(mContext));
//			mCamera.setParameters(parameters);
			// 通过SurfaceView显示取景画面
			mCamera.setPreviewDisplay(holder);
			// 开始预览
			mCamera.startPreview();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 拍照
	private void takePicture() {
		if (mCamera != null) {
			mCamera.takePicture(new ShutterCallback() {

				@Override
				public void onShutter() {
					// TODO 当用户按下快门时执行此处的代码

				}
			}, new PictureCallback() {

				@Override
				public void onPictureTaken(byte[] arg0, Camera arg1) {
					// TODO 当相机获取原始照片时执行此处代码，可在此处保存原始照片信息

				}
			}, new PictureCallback() {

				@Override
				public void onPictureTaken(byte[] data, Camera arg1) {
					// 此处根据拍照所得数据创建位图
					bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
					llSave.setVisibility(View.VISIBLE);
					llShare.setVisibility(View.VISIBLE);
				}
			});
			// 拍完照片后取消所有隐藏底部操作栏的Runnable
			handler.removeCallbacks(runnable);
			// 设置底部操作栏可见
			llBottom.setVisibility(View.VISIBLE);
		}
	}

	// 初始化电源锁
	private void initWakeLock() {
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
				"MainActivity");
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 友盟统计代码
		MobclickAgent.onResume(this);
		// 获取电源锁，以避免黑屏
		mWakeLock.acquire();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 友盟统计代码
		MobclickAgent.onPause(this);
		// 释放电源锁
		if (mWakeLock != null) {
			mWakeLock.release();
		}
	}

}
