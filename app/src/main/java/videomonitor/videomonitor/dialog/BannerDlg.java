package videomonitor.videomonitor.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;

import java.util.List;

import videomonitor.videomonitor.R;
import videomonitor.videomonitor.entity.VideoInfo;

/**
 * Created by Administrator on 2017-06-21.
 */

public class BannerDlg extends Dialog implements OnClickListener {
    protected Context mContext;
    private List<VideoInfo> list;
    private OnPageSelectListener onPageSelectListener;

    public BannerDlg(Context context, List<VideoInfo> list) {
        super(context, R.style.THeme_Dialog_NoFrame);
        mContext = context;
        this.list = list;
        setContentView(R.layout.dialog_banner);
        RollPagerView mViewPager = (RollPagerView) findViewById(R.id.view_pager);
        mViewPager.setAdapter(new ImageLoopAdapter(mViewPager));

    }

    private class ImageLoopAdapter extends LoopPagerAdapter {
//        int[] imgs = new int[]{
//                R.mipmap.img1,
//                R.mipmap.img2,
//                R.mipmap.img3,
//                R.mipmap.img4,
//                R.mipmap.img5,
//        };

        public ImageLoopAdapter(RollPagerView viewPager) {
            super(viewPager);
        }

        @Override
        public View getView(ViewGroup container, final int position) {
            ImageView view = new ImageView(container.getContext());
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//            view.setImageResource(imgs[position]);
            Glide.with(mContext).load(list.get(position).filePath).into(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPageSelectListener.onPageSelect(position);
                }
            });
            return view;
        }

        @Override
        public int getRealCount() {
//            return imgs.length;
            return list.size();
        }
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }

    // 屏蔽长按menu键弹出输入法~！11.1
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getRepeatCount() > 0
                && event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    public void setOnPageSelectListener(OnPageSelectListener onPageSelectListener) {
        this.onPageSelectListener = onPageSelectListener;
    }

    public interface OnPageSelectListener {
        void onPageSelect(int position);
    }

}
