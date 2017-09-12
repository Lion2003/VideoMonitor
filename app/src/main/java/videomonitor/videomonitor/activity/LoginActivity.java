package videomonitor.videomonitor.activity;

import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Request;
import videomonitor.videomonitor.R;
import videomonitor.videomonitor.dialog.CustomProgressDialog;
import videomonitor.videomonitor.entity.SewingInfoEntity;

/**
 * Created by Administrator on 2017-07-10.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    public static String CURRENT_ID;
    private AutoCompleteTextView focus;

    private MediaPlayer myMediaPlayer = null;				    // 媒体播放

    private ImageView imgLogin;
    private Dialog dialog;

    private ImageView imgArrow;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
        getWindow().addFlags(flags);
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        getData();
        imgLogin = (ImageView) findViewById(R.id.img_login);
        imgLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                sound();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent it = new Intent(LoginActivity.this, MainActivity4.class);
                        it.putExtra("CURRENT_ID", "0009153701");
                        startActivity(it);
                        finish();
                    }
                }, 1500);
            }
        });

        dialog = CustomProgressDialog.createLoadingDialog(this, "登录中，请稍后。。。");
        dialog.setCancelable(true);

        focus = (AutoCompleteTextView) findViewById(R.id.focus);
        focus.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                sound();
                if (((EditText) view).getText().toString().length() == 10) {
                    if(CURRENT_ID == null || CURRENT_ID.length() == 0) {
                        dialog.show();
                        CURRENT_ID = readCodeFromScanner(view);
                        Log.d("CARD_ID",CURRENT_ID);
//                        Intent it = new Intent(LoginActivity.this, MainActivity31.class);
//                        it.putExtra("CURRENT_ID", CURRENT_ID);
//                        startActivity(it);
//                        finish();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent it = new Intent(LoginActivity.this, MainActivity4.class);
                                it.putExtra("CURRENT_ID", CURRENT_ID);
                                startActivity(it);
                                finish();
                            }
                        }, 1500);
                    }
                }
                return false;
            }
        });

        imgArrow = (ImageView) findViewById(R.id.al_arrow);
        imgArrow.startAnimation(AnimationUtils.loadAnimation(this, R.anim.translate_anim));
    }

    @Override
    protected void onDestroy() {
        if(dialog.isShowing()) {
            dialog.dismiss();
        }
        super.onDestroy();
    }

    private void sound() {
        this.myMediaPlayer = MediaPlayer.create(this, R.raw.qrcode_completed); 	// 找到指定的资源
        this.myMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer media) {
                media.release(); 					// 释放所有状态
            }
        });
        if (myMediaPlayer != null) {
            myMediaPlayer.stop();		// 停止播放
        }
        try {
            this.myMediaPlayer.prepare();	// 进入到预备状态
            this.myMediaPlayer.start();	// 播放文件
        } catch (Exception e) {

        }

    }

    private String readCodeFromScanner(View input) {
        String s = ((EditText) input).getText().toString();
        s = s.substring(0,10);
        ((AutoCompleteTextView) input).setText(null);
        return s;
    }

    @Override
    public void onClick(View v) {

    }

    public class User {
        private int type;
        private String deviceNo;

        public User(int type, String deviceNo) {
            this.type = type;
            this.deviceNo = deviceNo;
        }
    }

    private void getData() {
        OkHttpUtils
                .postString()
                .url("http://fushan.oudot.cn/fushan_cisma_api/app")
                .content(new Gson().toJson(new User(1, "SJ000323")))
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(new MyStringCallback());
    }

    public class MyStringCallback extends StringCallback {
        @Override
        public void onBefore(Request request, int id) {
            Log.e("reeuest", "" + request + id);
        }

        @Override
        public void onAfter(int id) {
            Log.e("onAfter", id + "");
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            e.printStackTrace();
        }

        @Override
        public void onResponse(String response, int id) {
            Gson gson = new Gson();
        }

        @Override
        public void inProgress(float progress, long total, int id) {
//            mProgressBar.setProgress((int) (100 * progress));
        }
    }
}
