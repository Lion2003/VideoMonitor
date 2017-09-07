package videomonitor.videomonitor.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import videomonitor.videomonitor.R;
import videomonitor.videomonitor.entity.VideoEntity;

/**
 * Created by Administrator on 2017-09-04.
 */

public class VideoStandardAdapter extends RecyclerView.Adapter<VideoStandardAdapter.VideoViewHolder> {
    private LayoutInflater mLayoutInflater;
    private Context context;
    private List<VideoEntity> list;

    public VideoStandardAdapter(Context context, List<VideoEntity> list) {
        this.context = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VideoViewHolder(mLayoutInflater.inflate(R.layout.item_video_standard, parent, false));
    }

    @Override
    public void onBindViewHolder(final VideoViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).filePath).into(holder.img);
        holder.duration.setText(new SimpleDateFormat("mm:ss").format(new Date(list.get(position).duration))); // + list.get(position).duration + ""
        holder.title.setText("工艺编号   " + list.get(position).title);
        holder.content.setText("工艺名称   " + list.get(position).title);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getLayoutPosition();
                onItemClickListener.onItemClick(holder.itemView, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (list.size() == 0) ? 0 : list.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView duration;
        TextView title;
        TextView content;

        public VideoViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.ivs_img);
            duration = (TextView) itemView.findViewById(R.id.ivs_duration);
            title = (TextView) itemView.findViewById(R.id.ivs_title);
            content = (TextView) itemView.findViewById(R.id.ivs_content);
        }
    }

    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
