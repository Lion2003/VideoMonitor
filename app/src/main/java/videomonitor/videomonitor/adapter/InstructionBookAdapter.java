package videomonitor.videomonitor.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import videomonitor.videomonitor.R;
import videomonitor.videomonitor.entity.InstructionBookEntity;

/**
 * Created by Administrator on 2017-09-05.
 */

public class InstructionBookAdapter extends RecyclerView.Adapter<InstructionBookAdapter.InstructionBookViewHolder> {
    private LayoutInflater mLayoutInflater;
    private Context context;
    private List<InstructionBookEntity> list;

    public InstructionBookAdapter(Context context, List<InstructionBookEntity> list) {
        this.context = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public InstructionBookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new InstructionBookAdapter.InstructionBookViewHolder(mLayoutInflater.inflate(R.layout.item_instruction_book, parent, false));
    }

    @Override
    public void onBindViewHolder(final InstructionBookAdapter.InstructionBookViewHolder holder, int position) {
        holder.technologyId.setText("工艺编号   " + list.get(position).getTechnologyId());
        holder.technologyName.setText("工艺名称   " + list.get(position).getTechnologyName());

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

    public class InstructionBookViewHolder extends RecyclerView.ViewHolder {
        TextView technologyId;
        TextView technologyName;

        public InstructionBookViewHolder(View itemView) {
            super(itemView);
            technologyId = (TextView) itemView.findViewById(R.id.technologyId);
            technologyName = (TextView) itemView.findViewById(R.id.technologyName);
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
