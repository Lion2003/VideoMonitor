package videomonitor.videomonitor.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import videomonitor.videomonitor.widget.ZoomImageView;

/**
 * Created by Administrator on 2017-08-31.
 */

public class InstructBookOnePageFragment extends Fragment {
    private Bitmap bmp;
    private int source;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        source = getArguments().getInt("source");
        bmp = BitmapFactory.decodeResource(getResources(), source);
        ZoomImageView imageView = new ZoomImageView(getActivity());
        imageView.setImageBitmap(bmp);

        return imageView;
    }
}
