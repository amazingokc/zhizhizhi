package com.pofeite.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.pofeite.reader.R;

import java.util.List;

import adapter.HomeAdapter;
import service.MyService;
import utils.Constants;



/**
 * Created by xgj on 2015/12/14.
 */
public class WeixinActivity extends BaseActivity {

    public Constants constants;

    private GridView mGridGv;
    private DisplayImageOptions options;	// 设置图片显示相关参数
//    private String[] imageUrls;		// 图片路径
    private List<String> imageUrls;
    private HomeAdapter mAdapter;
    private String title;


    @Override
    public int initResource() {
        return R.layout.activity_gridview;
//        return 0;
    }

    @Override
    public void initComponent() {
        mGridGv = (GridView) findViewById(R.id.gv_image);
    }

    @Override
    public void initData() {
        constants = new Constants();
//        if (imageUrls == null) {
            imageUrls = constants.allTypeDataContentImg;    //拿到img的url
//        }
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.ic_empty) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.ic_error) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
//                .imageScaleType(ImageScaleType.EXACTLY) // default 设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                .build();

        mGridGv.setAdapter(new ItemGridAdapter());
//        imageScaleType(ImageScaleType.EXACTLY_STRETCHED);
        if (allTypeName.size() == 0 && MyService.allTypeDateName != null) {
            allTypeName = MyService.allTypeDateName;
            allTypeDateId = MyService.allTypeDateId;
        }

        if (allTypeName != null) {
            mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(WeixinActivity.this));
            mRecyclerView.setAdapter(mAdapter = new HomeAdapter(WeixinActivity.this, allTypeName));
            //分类点击事件
            mAdapter.setmOnItemClickListener(new HomeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
//                    Toast.makeText(WeixinActivity.this, allTypeName.get(position) + allTypeDateId.get(position),
//                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(WeixinActivity.this, WeixinActivity.class);
//                    intent.putExtra("name", allTypeName.get(position));
                    intent.putExtra("id", allTypeDateId.get(position));  //给自己传递id
                    intent.putExtra("name", allTypeName.get(position));//给自己传递name
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onItemLongClick(View view, int position) {
                }
            });
        }
    }

    //图片点击事件
    @Override
    public void addListener() {
        mGridGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                                Intent intent = new Intent(WeixinActivity.this, WeixinWebviewActivity.class);
                                intent.putExtra("url",allTypeDataUrl.get(position));
                                intent.putExtra("title", allTypeDataTitle.get(position));
                                 startActivity(intent);
            }
        });
    }

    class ItemGridAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            Log.d("getCount:", "" + imageUrls.size());
            return imageUrls.size();
        }

        @Override
        public Object getItem(int position) {
            Log.d("getItem:", "" + imageUrls.get(position));
            return imageUrls.get(position);
        }

        @Override
        public long getItemId(int position) {
            Log.d("getItemId:", "" + position);
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = new ViewHolder();
            if (convertView == null) {
                Log.d("convertView == null", "aa11");
//                viewHolder = new ViewHolder();
                convertView = getLayoutInflater().inflate(R.layout.item_grid, parent, false);
                viewHolder.image2 = (ImageView) convertView.findViewById(R.id.tou_ming_img);
                viewHolder.image2.setAlpha(100);
                viewHolder.Title = (TextView) convertView.findViewById(R.id.title);
                viewHolder.image = (ImageView) convertView.findViewById(R.id.iv_grid_image);
                convertView.setTag(viewHolder);
            } else {
                Log.d("convertView != null", "bb11");
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.Title.setText(allTypeDataTitle.get(position));
            Log.d("imageUrls.size()", "" + imageUrls.size());
            imageLoader.displayImage(imageUrls.get(position), viewHolder.image, options);

            return convertView;
        }

        public class ViewHolder {
            public ImageView image,image2;
            public TextView Title;
        }

    }


}
