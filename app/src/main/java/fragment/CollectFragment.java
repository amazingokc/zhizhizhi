package fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pofeite.activity.ZhihuWebviewActivity;
import com.pofeite.reader.R;

import java.util.ArrayList;
import java.util.List;

import adapter.ZhihuAdapter;
import bean.Collection;

/**
 * Created by xgj on 2016/1/7.
 */
public class CollectFragment extends BaseFragment {

    private String answer_url;
    public RecyclerView mRecyclerView;
    private TextView no_data;
    private ZhihuAdapter mAdapter;

    public List<String> alltitle;
    public List<String> allUrl;
    public List<String> allSummary;
    public List<String> allVote;
    private List<Collection> title;

    public CollectFragment() {
    }

    @Override
    public void GetRecentdate() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recent, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.id_recyclerview_recentfragment);
        no_data = (TextView) view.findViewById(R.id.no_data);
        lazyLoad();
        return view;
    }

    /**
     * 重写此方法，当给viewpage可见时,再重新加载此fragment
     */
    @Override
    protected void lazyLoad() {
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        alltitle = new ArrayList<>();
        allUrl = new ArrayList<>();
        allSummary = new ArrayList<>();
        allVote = new ArrayList<>();
        initRecyclerView();
        mAdapter.setmOnItemClickListener(new ZhihuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                answer_url = allUrl.get(position);
                Intent intent = new Intent(getActivity(), ZhihuWebviewActivity.class);
                intent.putExtra("answer_url", answer_url);
                intent.putExtra("title", alltitle.get(position));
                intent.putExtra("summary", "" + allSummary.get(position));
                intent.putExtra("vote", "" + allVote.get(position));
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        });


        ItemTouchHelper.Callback mCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP
                | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {
            /**
             * @param recyclerView
             * @param viewHolder 拖动的ViewHolder
             * @param target 目标位置的ViewHolder
             * @return
             */
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//                int fromPosition = viewHolder.getAdapterPosition();//得到拖动ViewHolder的position
//                int toPosition = target.getAdapterPosition();//得到目标ViewHolder的position
//                if (fromPosition < toPosition) {
//                    //分别把中间所有的item的位置重新交换
//                    for (int i = fromPosition; i < toPosition; i++) {
//                        Collections.swap(alltitle, i, i + 1);
//                    }
//                } else {
//                    for (int i = fromPosition; i > toPosition; i--) {
//                        Collections.swap(alltitle, i, i - 1);
//                    }
//                }
//                mAdapter.notifyItemMoved(fromPosition, toPosition);
                //返回true表示执行拖动
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        getActivity());
                builder.setTitle("执行");
                builder.setMessage("不再收藏？");
                builder.setIcon(R.drawable.ic_launcher);
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        title = Collection.listAll(Collection.class);
                        int size = title.size() - 1 - position;
                        Log.d("size22", "" + size);
                        if (size >= 0) {
                            title.get(size).delete();
                            Log.d("position", "" + position);
                            alltitle.remove(position);
                            mAdapter.notifyItemRemoved(position);
                            dialog.cancel();
                        }

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAdapter.notifyItemChanged(position);
                        dialog.cancel();
                    }
                });
                builder.create().show();

            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    //左右滑动时改变Item的透明度
                    final float alpha = 1 - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
                    viewHolder.itemView.setAlpha(alpha);
                    viewHolder.itemView.setTranslationX(dX);
                }
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                super.onSelectedChanged(viewHolder, actionState);
                //当选中Item时候会调用该方法，重写此方法可以实现选中时候的一些动画逻辑
                Log.v("zxy", "onSelectedChanged");
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                //当动画已经结束的时候调用该方法，重写此方法可以实现恢复Item的初始状态
                Log.v("zxy", "clearView");
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(mCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    private void initRecyclerView() {
        title = Collection.listAll(Collection.class);
        Log.d("title22", "" + title.size());
        if (title.size() == 0) {
            no_data.setVisibility(View.VISIBLE);
            no_data.setText("暂时没有收藏");
        } else {
            no_data.setVisibility(View.GONE);
        }
        for (int i = (title.size() - 1); i >= 0; i--) {
            alltitle.add(title.get(i).getAllTitle());
            allUrl.add(title.get(i).getAllUrl());
            allSummary.add(title.get(i).getAllSummary());
            allVote.add(title.get(i).getAllVote());
        }
        Log.d("aa11", "CollectFragment");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter = new ZhihuAdapter(getActivity(), alltitle, allVote, allSummary));

    }
}
