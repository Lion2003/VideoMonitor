package videomonitor.videomonitor.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import videomonitor.videomonitor.R;
import videomonitor.videomonitor.widget.ZoomImageView;

/**
 * Created by Administrator on 2017-06-27.
 */

public class InstructBookFragment extends Fragment {
    private View view;
    private TextView content;

    private ViewPager mViewPager;
    private List<Bitmap> mImgs = new ArrayList<Bitmap>();
    private ImageView[] mImageViews;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_instruct_book, container, false);

        String filePath = Environment.getExternalStorageDirectory() + "/作业指导书";
        File file = new File(filePath);
        if (!file.isDirectory())
        { //目录不存在
            file.mkdir(); //创建目录
        }
        File[] fileList = file.listFiles();

        if(fileList != null && fileList.length > 0) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            //options.inSampleSize = 2;
            for(int i = 0; i < fileList.length; i++) {
                Bitmap bm = BitmapFactory.decodeFile(fileList[i].toString(), options);
                mImgs.add(bm);
            }
            mImageViews = new ImageView[mImgs.size()];
        } else {
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_zyzds);
            Bitmap bmp1 = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_zyzds1);
            mImgs.add(bmp);
            mImgs.add(bmp1);
            mImageViews = new ImageView[mImgs.size()];
        }

        mViewPager = (ViewPager) view.findViewById(R.id.id_viewpager);
        mViewPager.setAdapter(new PagerAdapter()
        {

            @Override
            public Object instantiateItem(ViewGroup container, int position)
            {
                ZoomImageView imageView = new ZoomImageView(getActivity());
                imageView.setImageBitmap(mImgs.get(position));
                container.addView(imageView);
                mImageViews[position] = imageView;
                return imageView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object)
            {
                container.removeView(mImageViews[position]);
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1)
            {
                return arg0 == arg1;
            }

            @Override
            public int getCount()
            {
                return mImgs.size();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
