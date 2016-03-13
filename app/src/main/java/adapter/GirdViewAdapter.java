package adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pofeite.reader.R;

import java.util.List;

/**
 * Created by xgj on 2016/3/9.
 */
public class GirdViewAdapter extends BaseAdapter {

//    Constants constants;
    private List<String> imageUrls;    //拿到img的url;
    private List<String> allTypeDataTitle;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;	// 设置图片显示相关参数
    private LayoutInflater mInflater;

    public GirdViewAdapter(Context context, List<String> imageUrls, ImageLoader imageLoader,
                           List<String> allTypeDataTitle, DisplayImageOptions options){
//        this.constants = constants;
        this.imageUrls = imageUrls;
        this.imageLoader = imageLoader;
        this.options = options;
        this.allTypeDataTitle = allTypeDataTitle;
        mInflater = LayoutInflater.from(context);
    }


    /**
     * 决定了listview一共有多少个item
     */
    @Override
    public int getCount() {
        return imageUrls.size();
    }

    /**
     * setOnItemClickListener、setOnItemLongClickListener、
     * setOnItemSelectedListener的点击选择处理事件中通过AdapterView的
     * getItemAtPosition(position)调用此方法
     */
    @Override
    public Object getItem(int position) {
        return imageUrls.get(position);
    }

    /**
     * 某些方法（如onclicklistener的onclick方法）有id这个参数，而这个id
     * 参数就是取决于getItemId()这个返回值的
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 当界面每显示出来一个item时，就会调用该方法,getView()有三个参数，第一个参数表示该item在Adapter中的位置；
     * 第二个参数是item的View对象，是滑动list时将要显示在界面上的item，如果有item在显示界面消失，这时
     * android会将消失的item返回，称为旧view，也就是说此时的view不为null；第三个参数用在加载xml视图。
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();//获得实例对象
        if (convertView == null) {//当滑动list时，如果没有item消失(应该是没有任何操作，保存当前的实例)，
            // 这时参数对象view是没有任何指向的，为null
            Log.d("convertView == null", "aa11");
//                viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_grid,null);
            viewHolder.image2 = (ImageView) convertView.findViewById(R.id.tou_ming_img);
            viewHolder.image2.setAlpha(100);
            viewHolder.Title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.iv_grid_image);
            convertView.setTag(viewHolder);//向view中添加附加数据信息，在这里也就是两个ImageView和textview对象
        } else {
            Log.d("convertView != null", "bb11");
            //如果有旧的view对象返回(该情况是滑动list时有item消失)，从该view中提取已经创建的
            // 两个ImageView和textview对象，达到对象循环使用
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.Title.setText(allTypeDataTitle.get(position));
        Log.d("imageUrls.size()", "" + imageUrls.size());
        imageLoader.displayImage(imageUrls.get(position), viewHolder.image, options);

        return convertView;
    }

    /**
     * //该类用来暂存textView和ImageView的实例化对象，达到循环使用
     */
    public class ViewHolder {
        public ImageView image,image2;
        public TextView Title;
    }
}
