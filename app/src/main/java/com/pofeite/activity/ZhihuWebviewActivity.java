package com.pofeite.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFloat;
import com.gc.materialdesign.views.ProgressBarIndeterminate;
import com.pofeite.reader.R;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import bean.Collection;
import utils.Util;

/**
 * Created by xgj on 2016/1/2.
 */
public class ZhihuWebviewActivity extends AppCompatActivity  {

    private IWXAPI iwxapi;
    private WebView webview;
    private ProgressBarIndeterminate pb;
    private String url;
    private String title;
    private String summary;
    private String vote;
    private ShareActionProvider mShareActionProvider;

    ButtonFloat buttonFloat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhihu_webview_layout);

        iwxapi = WXAPIFactory.createWXAPI(this, "wxa7fe36921eba81f5");
        iwxapi.registerApp("wxa7fe36921eba81f5");
        webview = (WebView) findViewById(R.id.webview_zhihu);
        pb = (ProgressBarIndeterminate) findViewById(R.id.progressBarIndeterminate);

        buttonFloat = (ButtonFloat) findViewById(R.id.buttonFloat);
        buttonFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collector();
            }
        });
        initToolbar();
        getData();
        initWebview();
    }

    private void getData() {
        Intent intent = getIntent();
        url = intent.getStringExtra("answer_url");
        title = intent.getStringExtra("title");
        summary = intent.getStringExtra("summary");
        vote = intent.getStringExtra("vote");
    }

    private void initWebview() {
        WebSettings settings = webview.getSettings();
        settings.setSupportZoom(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);  //设置缓存模式
        settings.setBuiltInZoomControls(true);  // 隐藏缩放按钮
        settings.setDisplayZoomControls(false);
        settings.setUseWideViewPort(true);      // 可任意比例缩放
        settings.setDomStorageEnabled(true);  //
//        settings.setPluginsEnabled(true);  //支持插件
//        settings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        //设置WebView属性，能够执行Javascript脚本
        settings.setJavaScriptEnabled(true);
        //加载需要显示的网页

        Log.d("title11", "" + title + " " + summary + " " + vote);

        //设置Web视图
//        webview.setWebChromeClient(new WebChromeClient());
        webview.setWebViewClient(new HelloWebViewClient());
        webview.loadUrl(url);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setNavigationIcon(R.drawable.back);//设置ToolBar头部图标
        toolbar.setTitle("");//设置标题，也可以在xml中静态实现
        setSupportActionBar(toolbar);//使活动支持ToolBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//添加返回键
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.share_wx_timeline:
                        wecharShare();
                        break;
                    case R.id.other_share:
                    default:
                        break;
                }
                return true;
            }
        });
    }

    private void wecharShare() {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;

        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
//        msg.description = "网页描述";
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.logo2);
        msg.thumbData = Util.bmpToByteArray(thumb, true);

        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
//        req.transaction = buildTra    ?
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        iwxapi.sendReq(req);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        /* ShareActionProvider配置 */
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menu
                .findItem(R.id.other_share));
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
        intent.putExtra(Intent.EXTRA_TEXT, title + "(" + url + ")");
        intent.setType("image/*");
        mShareActionProvider.setShareIntent(intent);
//        startActivity(Intent.createChooser(intent, getTitle()));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
//        return false;
    }
    /**
     * 收藏文章
     */
    private void collector() {
        Toast.makeText(ZhihuWebviewActivity.this, "已收藏", Toast.LENGTH_SHORT).show();
        buttonFloat.setVisibility(View.GONE);
        Collection collection = new Collection(title, url, summary, vote);
        collection.save();
//        Log.d("gettt",collection.getAllTitle());
    }

    /**
     * 按物理返回键是返回上一个HTML
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {
            webview.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO

            Log.d("url111", "" + url);
            pb.setVisibility(View.GONE);
//            Toast.makeText(ZhihuWebviewActivity.this, "finish", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        webview.reload();  //此方法是停止正在播放的音乐或者视频
    }


}
