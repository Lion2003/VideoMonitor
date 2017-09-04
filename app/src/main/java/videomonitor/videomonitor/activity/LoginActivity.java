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
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import videomonitor.videomonitor.R;
import videomonitor.videomonitor.dialog.CustomProgressDialog;

/**
 * Created by Administrator on 2017-07-10.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    public static String CURRENT_ID;
    private AutoCompleteTextView focus;

    private MediaPlayer myMediaPlayer = null;				    // 媒体播放

    private ImageView imgLogin;
    private Dialog dialog;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

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
}
