package videomonitor.videomonitor.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import videomonitor.videomonitor.R;
import videomonitor.videomonitor.widget.ZoomImageView;

/**
 * Created by Administrator on 2017-09-12.
 */

public class FullScreenBookActivity extends BaseActivity implements View.OnClickListener {

    private ZoomImageView zoomImageView;
    private ImageView img;
    private Bitmap bmp;
    private String source;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.fragment_instruct_book_onepage);
        zoomImageView = (ZoomImageView) findViewById(R.id.fibo_zoomImageView);
        img = (ImageView) findViewById(R.id.fab);
        img.setImageResource(R.mipmap.icon_finish);
        source = getIntent().getStringExtra("source");
        bmp = BitmapFactory.decodeFile(source);
        zoomImageView.setImageBitmap(bmp);

        img.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                finish();
                break;
        }
    }
}
