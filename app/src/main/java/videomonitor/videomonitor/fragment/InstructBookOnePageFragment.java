package videomonitor.videomonitor.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import videomonitor.videomonitor.R;
import videomonitor.videomonitor.activity.FullScreenBookActivity;
import videomonitor.videomonitor.widget.ZoomImageView;

/**
 * Created by Administrator on 2017-08-31.
 */

public class InstructBookOnePageFragment extends Fragment implements View.OnClickListener {
    private View view;
    private ZoomImageView zoomImageView;
    private ImageView img;
    private Bitmap bmp;
    private String source;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_instruct_book_onepage, container, false);
        zoomImageView = (ZoomImageView) view.findViewById(R.id.fibo_zoomImageView);
        img = (ImageView) view.findViewById(R.id.fab);
        source = getArguments().getString("source");
        bmp = BitmapFactory.decodeFile(source);
        zoomImageView.setImageBitmap(bmp);

        img.setOnClickListener(this);

        return view;
    }

    private void toFullScreen() {
        Intent it = new Intent(getActivity(), FullScreenBookActivity.class);
        it.putExtra("source", source);
        startActivity(it);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                toFullScreen();
                break;
        }
    }
}
