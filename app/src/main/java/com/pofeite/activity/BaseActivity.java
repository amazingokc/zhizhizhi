package com.pofeite.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pofeite.reader.R;

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
import java.util.Timer;
import java.util.TimerTask;

import bean.Contentlist;
import service.MyService;
import utils.Constants;

public abstract class BaseActivity extends AppCompatActivity {

    protected ImageLoader imageLoader;

    static String httpUrl = "http://apis.baidu.com/showapi_open_bus/weixin/weixin_article_list";
    RequestQueue mQueue;
    StringRequest stringRequest;
    Gson gson;
    public RecyclerView mRecyclerView;

    public List<String> allTypeDataDate;
    public List<String> allTypeDataTitle;
    public List<String> allTypeDataUrl;
    public List<String> allTypeDataUserName;

    public Constants constants;

    private TextView data;
    private static String typeId;


    private boolean status = true;

    /**
     * 初始化布局资源文件
     */
    public abstract int initResource();

    /**
     * 初始化组件
     */
    public abstract void initComponent();

    /**
     * 初始化数据
     */
    public abstract void initData();

    /**
     * 添加监听
     */
    public abstract void addListener();

    private ActionBarDrawerToggle mDrawerToggle;         //定义toolbar左上角的弹出左侧菜单按钮
    private DrawerLayout mDrawerLayout;
    List<String> allTypeName;
    List<String> allTypeDateId;
    private String typeName;

    private ServiceConnection conn = new ServiceConnection() {
        /** 获取服务对象时的操作 */
        public void onServiceConnected(ComponentName name, IBinder service) {
            countService = ((MyService.ServiceBinder) service).getService();

            MyService.MyListener listener = new MyService.MyListener() {
                @Override
                public void chang(String str) {
//                  Toast.makeText(BaseActivity.this, "" + str, Toast.LENGTH_LONG).show();
                    Log.d("str", "" + str);
                    typeId = str;
                    gson = new Gson();
                    mQueue = Volley.newRequestQueue(BaseActivity.this);
                    GoForDetailinfo();
                    mQueue.add(stringRequest);
                    imageLoader = ImageLoader.getInstance();
                }
            };
            countService.setMyListener(listener);
        }

        /** 无法获取到服务对象时的操作 */
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    MyService countService = null;
    private ProgressBarCircularIndeterminate pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initResource());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//      toolbar.setNavigationIcon(R.mipmap.ic_launcher);//设置ToolBar头部图标

        Intent intent = getIntent();
        typeId = intent.getStringExtra("id");
        typeName = intent.getStringExtra("name");
        toolbar.setTitle(typeName);//设置标题，也可以在xml中静态实现

        setSupportActionBar(toolbar);//使活动支持ToolBar
        pb = (ProgressBarCircularIndeterminate) findViewById(R.id.pb2);
        pb.setVisibility(View.VISIBLE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open,
                R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

//        startService(new Intent(this, MyService.class));
        bindService(new Intent(this, MyService.class), conn, Context.BIND_AUTO_CREATE);

        constants = new Constants();

        allTypeName = new ArrayList<>();
        allTypeDateId = new ArrayList<>();

        //每次拿到id后去请求相应文章的title，img...
        if (typeId != null) {
            gson = new Gson();
            mQueue = Volley.newRequestQueue(BaseActivity.this);
            GoForDetailinfo();
            mQueue.add(stringRequest);
            imageLoader = ImageLoader.getInstance();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        unbindService(conn);
        if (mQueue != null) {
            mQueue.cancelAll(BaseActivity.this);
        }
        //让全局imgulr数组清空,否则会叠加之前的元素
        constants.allTypeDataContentImg = new ArrayList<>();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
        Log.d("onDestroy", "onDestroy");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        /* ShareActionProvider配置 */
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.about:
                Intent intent = new Intent(BaseActivity.this, AboutActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                break;
            case R.id.zhihu:
                Intent intent2 = new Intent(BaseActivity.this, ZhihuActivity.class);
                startActivity(intent2);
                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }



    public void GoForDetailinfo() {
        stringRequest = new StringRequest(Request.Method.POST, httpUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        allTypeDataDate = new ArrayList<>();
                        allTypeDataTitle = new ArrayList<>();
                        allTypeDataUrl = new ArrayList<>();
                        allTypeDataUserName = new ArrayList<>();
                        try {
                            JSONObject dataJson = new JSONObject(response);
                            JSONObject showapi_res_body = dataJson.getJSONObject("showapi_res_body");
                            JSONObject pagebean = showapi_res_body.getJSONObject("pagebean");
                            JSONArray typeList11 = pagebean.getJSONArray("contentlist");

                            Type listType = new TypeToken<LinkedList<Contentlist>>() {
                            }.getType();
                            Gson gson = new Gson();
                            if (constants.allTypeDataContentImg != null) {
                                constants.allTypeDataContentImg = new ArrayList<>();
                            }
                            LinkedList<Contentlist> users = gson.fromJson(typeList11.toString(), listType);
                            for (Iterator iterator = users.iterator(); iterator.hasNext(); ) {
                                Contentlist list = (Contentlist) iterator.next();

                                HashMap<String, Object> Type = new HashMap<>();
                                Type.put("contentImg", list.getContentImg());
                                Type.put("date", list.getDate());
                                Type.put("title", list.getTitle());
                                Type.put("url", list.getUrl());
                                Type.put("userName", list.getUserName());

                                constants.allTypeDataContentImg.add(list.getContentImg());
                                allTypeDataDate.add(list.getDate());
                                allTypeDataTitle.add(list.getTitle());
                                allTypeDataUrl.add(list.getUrl());
                                allTypeDataUserName.add(list.getUserName());
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        initComponent();
                        initData();
                        pb.setVisibility(View.GONE);
                        addListener();

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

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("typeId", typeId);
                map.put("page", "1");
                map.put("key", "1");
                return map;
            }
        };
    }

    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click(); //调用双击退出函数
        }
        return false;
    }

    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
            System.exit(0);
        }
    }

}
