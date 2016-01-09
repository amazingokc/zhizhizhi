package com.pofeite.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.pofeite.reader.R;

/**
 * Created by xgj on 2016/1/2.
 */
public class ZhihuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhihu_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setNavigationIcon(R.drawable.back);//设置ToolBar头部图标
        toolbar.setTitle("看知乎");//设置标题，也可以在xml中静态实现
        setSupportActionBar(toolbar);//使活动支持ToolBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//添加返回键
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}
