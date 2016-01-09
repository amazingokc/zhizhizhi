package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pofeite.reader.R;

import java.util.List;

/**
 * Created by xgj on 2016/1/2.
 */
public class ZhihuAdapter extends RecyclerView.Adapter<MyViewHolder1> {

    private LayoutInflater mInflater;
    private Context mContext;
    private List<String> mTitle;
    private List<String> mVote;
    private List<String> mSummary;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setmOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    public ZhihuAdapter(Context context, List<String> title, List<String> vote, List<String> summary) {
        this.mContext = context;
        this.mTitle = title;
        this.mVote = vote;
        this.mSummary = summary;
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public MyViewHolder1 onCreateViewHolder(ViewGroup arg0, int i) {

        View view = mInflater.inflate(R.layout.item_zhihu, arg0, false);
        MyViewHolder1 viewHolder = new MyViewHolder1(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder1 holder, final int pos) {
        holder.tv.setText(mTitle.get(pos));
        holder.vote.setText(mVote.get(pos));
        holder.summary.setText(mSummary.get(pos));
        if (mOnItemClickListener != null) {
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
        return mTitle.size();  //返回数据的长度
//        return 1;
    }

//    public void addData(int pos) {
//        mTitle.add(pos, "Insert One");
//        notifyItemInserted(pos);
//    }
//
//    public void deleteData(int pos) {
//        mTitle.remove(pos);
//        notifyItemRemoved(pos);
//    }

}

class MyViewHolder1 extends RecyclerView.ViewHolder {
    TextView tv;
    TextView vote;
    TextView summary;

    public MyViewHolder1(View arg0) {
        super(arg0);
        tv = (TextView) arg0.findViewById(R.id.title_zhihu);
        vote = (TextView) arg0.findViewById(R.id.vote_zhihu);
        summary = (TextView) arg0.findViewById(R.id.summary_zhihu);
    }
}
