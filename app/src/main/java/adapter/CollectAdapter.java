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
 * Created by xgj on 2016/1/7.
 */
public class CollectAdapter extends RecyclerView.Adapter<MyViewHolder2> {

    private LayoutInflater mInflater;
    private Context mContext;
    private List<String> alltitle;
    private List<String> allSummary;
    private List<String> allVote;


    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setmOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    public CollectAdapter(Context context, List<String> alltitle, List<String> allSummary, List<String> allVote) {
        this.mContext = context;
        this.alltitle = alltitle;
        this.allSummary = allSummary;
        this.allVote = allVote;

        mInflater = LayoutInflater.from(context);
    }


    @Override
    public MyViewHolder2 onCreateViewHolder(ViewGroup arg0, int i) {

        View view = mInflater.inflate(R.layout.item_zhihu, arg0, false);
        MyViewHolder2 viewHolder = new MyViewHolder2(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder2 holder, final int pos) {
        holder.tv.setText(alltitle.get(pos));
        holder.vote.setText(allVote.get(pos));
        holder.summary_zhihu.setText(allSummary.get(pos));
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
        return alltitle.size();  //返回数据的长度
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

class MyViewHolder2 extends RecyclerView.ViewHolder {
    TextView tv;
    TextView vote,summary_zhihu;

    public MyViewHolder2(View arg0) {
        super(arg0);
        tv = (TextView) arg0.findViewById(R.id.title_zhihu);

        vote = (TextView) arg0.findViewById(R.id.vote_zhihu);
        summary_zhihu = (TextView) arg0.findViewById(R.id.summary_zhihu);
    }
}

