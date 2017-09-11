package videomonitor.videomonitor.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import videomonitor.videomonitor.R;
import videomonitor.videomonitor.widget.ZoomImageView;

/**
 * Created by Administrator on 2017-08-31.
 */

public class InstructBookOnePageFragment extends Fragment {
    private View view;
    private ZoomImageView zoomImageView;
    private Bitmap bmp;
    private int source;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_instruct_book_onepage, container, false);
        zoomImageView = (ZoomImageView) view.findViewById(R.id.fibo_zoomImageView);
        source = getArguments().getInt("source");
        bmp = BitmapFactory.decodeResource(getResources(), source);
        zoomImageView.setImageBitmap(bmp);

        return view;
    }
}
