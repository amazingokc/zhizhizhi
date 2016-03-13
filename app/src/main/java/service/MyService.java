package service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import bean.TypeList;

/**
 * Created by xgj on 2015/12/21.
 */
public class MyService extends Service {

    private MyListener listener;
    int x;

    public MyService() {
    }

    public class ServiceBinder extends Binder {
        public MyService getService() {
            return MyService.this;
        }
    }

    boolean destroy;

    @Override
    public void onCreate() {
        super.onCreate();

    }


    boolean enable = true;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        gson = new Gson();
        mQueue = Volley.newRequestQueue(MyService.this);
        GoForTypetitle();
        mQueue.add(stringRequest);


        return new ServiceBinder();
    }

    Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            if (listener != null) {
                listener.chang(msg.what + "");
            } else {
                Log.e("", "eeeeee");
            }
        }
    };

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e("onUnbind", "onUnbind");
        System.out.print("wwwwww");
        enable = false;
        return super.onUnbind(intent);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroy = true;
        Log.e("", "destroy");
//        listener.chang("");
    }

    public interface MyListener {
        void chang(String str);
    }

    public void setMyListener(MyListener listener) {
        this.listener = listener;
    }

    static final String httpUrl = "http://apis.baidu.com/showapi_open_bus/weixin/weixin_article_type";
    public static List<String> allTypeDateName;
    public static List<String> allTypeDateId;
    RequestQueue mQueue;
    StringRequest stringRequest;
    Gson gson;

    /**
     * 获取侧滑栏的标题
     */
    public void GoForTypetitle() {
        stringRequest = new StringRequest(Request.Method.GET, httpUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        allTypeDateName = new ArrayList<>();
                        allTypeDateId = new ArrayList<>();
                        try {
                            JSONObject dataJson = new JSONObject(response);
                            Log.d("dataJson1", "" + dataJson);
                            JSONObject showapi_res_body = dataJson.getJSONObject("showapi_res_body");
                            JSONArray typeList = showapi_res_body.getJSONArray("typeList");

                            Type listType = new TypeToken<LinkedList<TypeList>>() {}.getType();
                            Gson gson = new Gson();
                            LinkedList<TypeList> users = gson.fromJson(typeList.toString(), listType);
                            for (Iterator iterator = users.iterator(); iterator.hasNext(); ) {
                                TypeList list = (TypeList) iterator.next();

                                allTypeDateName.add(list.getName() /*+ list.getId()*/);
                                allTypeDateId.add(list.getId());

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (allTypeDateName != null && allTypeDateId != null) {
                            Log.d("allTypeDateId1234", "" + allTypeDateId);
                            handler.sendEmptyMessage(x=0+(int)(Math.random()*19));
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                    }

                }) {
            /**
             * 添加请求头
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("apikey", "d1e19200ccd340362c47275a58a317e1");
                return headers;
            }
        };
        stringRequest.setTag("volleyget");
    }
}
