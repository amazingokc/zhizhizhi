package adapter;

/**
 * Created by xgj on 2015/12/14.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pofeite.reader.R;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<MyViewHolder> {

private LayoutInflater mInflater;
private Context mContext;
    private List<String> mDatas;

public interface OnItemClickListener {
    void onItemClick(View view, int position);
    void onItemLongClick(View view, int position);
}

private OnItemClickListener mOnItemClickListener;

    public void setmOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public HomeAdapter(Context context, List<String> datas) {
        this.mContext = context;
        this.mDatas = datas;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup arg0, int i) {

        View view = mInflater.inflate(R.layout.item_home, arg0, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int pos) {
        holder.tv.setText(mDatas.get(pos));
        if(mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int layoutPosition = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, layoutPosition);

                }
            });


            /**
             * longclick
             */
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int layoutPosition = holder.getLayoutPosition();
                    mOnItemClickListener.onItemLongClick(holder.itemView, layoutPosition);
                    return false;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mDatas.size();  //返回数据的长度
//        return 1;
    }

    public void addData(int pos) {
        mDatas.add(pos, "Insert One");
        notifyItemInserted(pos);
    }

    public void deleteData(int pos) {
        mDatas.remove(pos);
        notifyItemRemoved(pos);
    }

}

class MyViewHolder extends RecyclerView.ViewHolder {
    TextView tv;
    public MyViewHolder(View arg0) {
        super(arg0);
        tv = (TextView) arg0.findViewById(R.id.id_num);
    }
}