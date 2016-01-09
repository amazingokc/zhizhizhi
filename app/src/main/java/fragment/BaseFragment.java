package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pofeite.activity.ZhihuWebviewActivity;
import com.pofeite.reader.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import adapter.ZhihuAdapter;
import bean.Answers;
import bean.TypeList;

/**
 * Created by xgj on 2016/1/3.
 */
public abstract class BaseFragment extends Fragment {
    public RecyclerView mRecyclerView;
    private ZhihuAdapter mAdapter;
    RequestQueue mQueue;
    StringRequest stringRequest;
    String httpUrl = "http://api.kanzhihu.com/getpostanswers/";

    private List<String> allTitle;
    private List<String> allSummary;
    private List<String> allQuestionid;
    private List<String> allAnswerid;
    private List<String> allVote;
    private List<String> allTime;
    private String error;

    ProgressBarCircularIndeterminate progressBarCircularIndetermininate;

    /**
     * 获取时间的方法
     */
    public abstract void GetRecentdate();

    /**
     * 每次切换到这个fragment时都会调用这个方法
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recent, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.id_recyclerview_recentfragment);
        progressBarCircularIndetermininate = (ProgressBarCircularIndeterminate) view.
                findViewById(R.id.progressBarCircularIndetermininate);
        progressBarCircularIndetermininate.setVisibility(View.VISIBLE);
        GetRecentdate();
        if (allTitle == null) {
            GoForZhihuDetailInfo();
            mQueue.add(stringRequest);

        }
        if (allTitle != null) {
            setAdapter();
        }
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQueue = Volley.newRequestQueue(getActivity());

    }

    /** Fragment当前状态是否可见 */
    protected boolean isVisible;


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }


    /**
     * 可见
     */
    protected void onVisible() {
        lazyLoad();
    }


    /**
     * 不可见
     */
    protected void onInvisible() {


    }


    /**
     * 延迟加载
     * 子类必须重写此方法
     */
    protected abstract void lazyLoad();


    public void GoForZhihuDetailInfo() {
        stringRequest = new StringRequest(Request.Method.GET, httpUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        allTitle = new ArrayList<>();
                        allSummary = new ArrayList<>();
                        allQuestionid = new ArrayList<>();
                        allAnswerid = new ArrayList<>();
                        allVote = new ArrayList<>();
                        allTime = new ArrayList<>();
                        try {
                            JSONObject dataJson = new JSONObject(response);
                            Log.d("allresponse", "" + dataJson);
                            String Jerror = dataJson.getString("error");
                            error = Jerror;
//                            if (error.equals(R.string.no_result)){
//                                Toast.makeText(getActivity(),R.string.nodate, Toast.LENGTH_SHORT).show();
//                            }
                            JSONArray answers = dataJson.getJSONArray("answers");
                            Log.d("answer11", "" + answers);

                            Type listType = new TypeToken<LinkedList<Answers>>() {}.getType();
                            Gson gson = new Gson();
                            LinkedList<TypeList> users = gson.fromJson(answers.toString(), listType);
                            for (Iterator iterator = users.iterator(); iterator.hasNext(); ) {
                                Answers list = (Answers) iterator.next();
                                allTitle.add(list.getTitle() /*+ list.getId()*/);
                                allSummary.add(list.getSummary());
                                allQuestionid.add(list.getQuestionid());
                                allAnswerid.add(list.getAnswerid());
                                allVote.add(list.getVote());
                                allTime.add(list.getTime());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        mRecyclerView = (RecyclerView) view.findViewById(R.id.id_recyclerview_recentfragment);
                        setAdapter();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                        progressBarCircularIndetermininate.setVisibility(View.GONE);
                    }

                }) {
        };
    }

    public void setAdapter() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter = new ZhihuAdapter(getActivity(), allTitle, allVote, allSummary));
        //分类点击事件
        progressBarCircularIndetermininate.setVisibility(View.GONE);
        mAdapter.setmOnItemClickListener(new ZhihuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                                Toast.makeText(getActivity(), allTitle.get(position),
//                                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), ZhihuWebviewActivity.class);
//                                String answer_url = "http://www.zhihu.com/question/questionid/answer/answerid"
                String answer_url = "http://www.zhihu.com/question/" + allQuestionid.get(position) +
                        "/answer/" + allAnswerid.get(position);
                Log.d("answer_url", answer_url);
                intent.putExtra("answer_url", answer_url);
                intent.putExtra("title", allTitle.get(position));
                intent.putExtra("summary", "" + allSummary.get(position));
                intent.putExtra("vote", "" + allVote.get(position));
                startActivity(intent);
            }
            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
    }

}
