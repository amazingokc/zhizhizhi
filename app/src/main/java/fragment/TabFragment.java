package fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pofeite.reader.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xgj on 2016/1/2.
 */
public class TabFragment extends Fragment {

    private TabLayout tab_FindFragment_title;                            //定义TabLayout
    private ViewPager vp_FindFragment_pager;                             //定义viewPager
    private FragmentPagerAdapter fAdapter;                               //定义adapter

    private List<Fragment> list_fragment;                                //定义要装fragment的列表
    private List<String> list_title;

    private ArchiveFragment archiveFragment;                            //归档精选
    private YesterdayFragment yesterdayFragment;                        //昨天精选
    private RecentFragment recentFragment;                              //当前精选
    private CollectFragment collectFragment;                            //个人收藏

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab, container, false);

        initControls(view);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * 初始化各控件
     * @param view
     */
    private void initControls(View view) {

        tab_FindFragment_title = (TabLayout)view.findViewById(R.id.tab_FindFragment_title);
        vp_FindFragment_pager = (ViewPager)view.findViewById(R.id.vp_FindFragment_pager);

        //初始化各fragment
        recentFragment = new RecentFragment();
        yesterdayFragment = new YesterdayFragment();
        archiveFragment = new ArchiveFragment();
        collectFragment = new CollectFragment();

        //将fragment装进集合
        list_fragment = new ArrayList<>();
        list_fragment.add(recentFragment);
        list_fragment.add(yesterdayFragment);
        list_fragment.add(archiveFragment);
        list_fragment.add(collectFragment);


        //将名称加载tab名字列表，正常情况下，我们应该在values/arrays.xml中进行定义然后调用
        list_title = new ArrayList<>();
        list_title.add("当前最新");
        list_title.add("昨日热门");
        list_title.add("历史精华");
        list_title.add("个人收藏");

        //设置TabLayout的模式
        tab_FindFragment_title.setTabMode(TabLayout.MODE_FIXED);  //固定不可滚动
        //为TabLayout添加tab名称
        tab_FindFragment_title.addTab(tab_FindFragment_title.newTab().setText(list_title.get(0)));
        tab_FindFragment_title.addTab(tab_FindFragment_title.newTab().setText(list_title.get(1)));
        tab_FindFragment_title.addTab(tab_FindFragment_title.newTab().setText(list_title.get(2)));
        tab_FindFragment_title.addTab(tab_FindFragment_title.newTab().setText(list_title.get(3)));




        fAdapter = new Find_tab_Adapter(getActivity().getSupportFragmentManager(),
                list_fragment,list_title);

        //viewpager加载adapter
        vp_FindFragment_pager.setAdapter(fAdapter);
        vp_FindFragment_pager.setOffscreenPageLimit(3);  //预留三个viewpage
//        vp_FindFragment_pager.setCurrentItem(1);//跳到指定页面
//        vp_FindFragment_pager.

//        给ViewPager添加监听(这里我们直接使用TabLayout里面提供的
//         TabLayoutOnPageChangeListener无需自己再编写)
        vp_FindFragment_pager.addOnPageChangeListener(new TabLayout.
                TabLayoutOnPageChangeListener(tab_FindFragment_title));

//        //设置setOnTabSelectedListener
//        tab_FindFragment_title.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                //切换到指定的item
//                vp_FindFragment_pager.setCurrentItem(tab.getPosition());
//                Log.d("bb11", "" + tab.getPosition() + list_fragment.get(tab.getPosition()));
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });

        //TabLayout加载viewpager
        tab_FindFragment_title.setupWithViewPager(vp_FindFragment_pager);
    }
}
