package videomonitor.videomonitor.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import videomonitor.videomonitor.R;
import videomonitor.videomonitor.activity.MainActivity31;

/**
 * Created by Administrator on 2017-06-27.
 */

public class MessageFragment extends Fragment implements View.OnClickListener {
    private View view;
    private Button video, book;
    private VideoViewPagerFragment fragment;
    public static String TAG = "MessageFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_message, container, false);
        TAG = "MessageFragment";
        video = (Button) view.findViewById(R.id.fm_video);
        book = (Button) view.findViewById(R.id.fm_book);
        fragment = new VideoViewPagerFragment();
        video.setOnClickListener(this);
        book.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.fm_video:
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.cm3_relativeLayout, new VideoViewPagerFragment()).commit();
                ((MainActivity31)getActivity()).showVideo1();
                TAG = "VideoViewPagerFragment";
                break;
            case R.id.fm_book:
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.cm3_relativeLayout, new InstructBookFragment()).commit();
                ((MainActivity31)getActivity()).showInstruct();
                TAG = "InstructBookFragment";
                break;
        }
    }
}
