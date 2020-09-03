package com.example.recyclerviewtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.recyclerviewtest.adapters.GridViewAdapter;
import com.example.recyclerviewtest.adapters.ListViewAdapter;
import com.example.recyclerviewtest.adapters.RecyclerViewBaseAdapter;
import com.example.recyclerviewtest.recyclerviewtest.beans.Datas;
import com.example.recyclerviewtest.recyclerviewtest.beans.ItemBean;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * 总结：
 * 1.首先我们要有控件，这个RecyclerView他呢是在Androidx包下,所以我们要他开module settings里的Dependency
 * 添加RecyclerView的依赖, 这样子才能在布局文件里使用RecyclerView这个控件
 * 2.findViewyID找到控件
 * 3.准备好数据
 * 4.设置布局管理器
 * 5.创建适配器
 * 6.设定适配器
 * 7.数据就可以显示出来
 * 8. 因为会多次被使用到， 需要代码重构
 */

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private RecyclerView mList;
    private List<ItemBean> mData;
    private RecyclerViewBaseAdapter mAdapter;
    private SwipeRefreshLayout mRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //找到控件
        mList = findViewById(R.id.recycler_view);
        mRefreshLayout = findViewById(R.id.refresh_layout);

        //准备数据

        /**
         *   准备数据，一般来说，我们在现实开发中，我们的数据是从网络中获取的
         *   所以我们只是模拟数据，在现实开发中也是要模拟数据的，比如说，后台没有准备好的时候
         */
        initDate();

        //设定预设显示样式为ListView效果
        showList(true, false);

        //处理下拉刷新
        handlerDownPullUpdate();


    }

    private void handlerDownPullUpdate() {

        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);
        mRefreshLayout.setEnabled(true);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //在这里面执行刷新数据的操作
                /**
                 * 在当我们在顶部，下拉的时候，这个方法就会被触发
                 * 但是这个方法是MainThread主线程，不可以执行耗时操作
                 * 一般来说，我们去请求数据再开一个线程去获取数据
                 * //在这里面演示的话，我直接添加一条数据
                 * */
                //添加数据
                ItemBean data = new ItemBean();
                data.title = "我是新添加的数据";
                data.icon = R.mipmap.pic_08;
                mData.add(0, data);
                //更新UI
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //这里面要做两件事，一件是让刷新停止，另外一件则是要更新列表
                        mAdapter.notifyDataSetChanged();
                        mRefreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });

    }

    private void initListener() {
        mAdapter.setOnItemClickListener(new RecyclerViewBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //这里的条目处理点击事件 该干嘛就干嘛， 跳转的就跳转
                Toast.makeText(MainActivity.this, "您目前点击的是" + position + "个条目", Toast.LENGTH_LONG).show();
            }
        });

        //这里去处理上来加载更多的点击事件
        if (mAdapter instanceof ListViewAdapter) {
            ((ListViewAdapter) mAdapter).setOnRefreshListener(new ListViewAdapter.OnRefreshListener() {
                @Override
                public void onUpPullRefresh(final ListViewAdapter.LoadMoreHolder loadMoreHolder) {
                    //这里面去加载更多的数据，需要在子线程中完成，这里仅做演示
                    //添加数据

                    //更新UI
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            Random random = new Random();
                            if (random.nextInt() % 2 == 0) {
                                ItemBean data = new ItemBean();
                                data.title = "我是新添加的数据";
                                data.icon = R.mipmap.pic_08;
                                mData.add(data);
                                Log.d("加一個新的資料", "第 " + (mData.size() - 1));
                                //这里面要做两件事，一件是让刷新停止，另外一件则是要更新列表
                                mAdapter.notifyDataSetChanged();
                                Log.d("更新dataset", "新添加數據");
                                loadMoreHolder.update(loadMoreHolder.LOAD_STATE_NORMAL);
                            } else {
                                loadMoreHolder.update(loadMoreHolder.LOAD_STATE_RELOAD);
                            }

                        }
                    }, 3000);
                }
            });
        }
    }

    /**
     * 这个方法用于初始化模拟数据
     */
    private void initDate() {
        //List<DataBean>------->Adapter------>setAdapter----->显示数据。\
        //创建集合
        mData = new ArrayList<>();

        //创建模拟数据
        for (int i = 0; i < Datas.icons.length; i++) {
            //创建数据对象
            ItemBean data = new ItemBean();
            data.icon = Datas.icons[i];
            data.title = "我是第 " + i + " 个条目";

            //添加到集合里面
            mData.add(data);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {//加载菜单
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            //listView的部分
            case R.id.list_view_vertical_standard:
                Log.d(TAG, "点击了ListView里面的垂直标准");
                showList(true, false);
                break;
            case R.id.list_view_vertical_reverse:
                Log.d(TAG, "点击了ListView里面的垂直反向");
                showList(true, true);
                break;
            case R.id.list_view_horizontal_standard:
                Log.d(TAG, "点击了ListView里面的水平标准");
                showList(false, false);
                break;
            case R.id.list_view_horizontal_reverse:
                Log.d(TAG, "点击了ListView里面的水平反向");
                showList(false, true);
                break;
            //GridView的部分
            case R.id.grid_view_vertical_standard:
                showGrid(true, false);
                break;
            case R.id.grid_view_vertical_reverse:
                showGrid(true, true);
                break;
            case R.id.grid_view_horizontal_standard:
                showGrid(false, false);
                break;
            case R.id.grid_view_horizontal_reverse:
                showGrid(false, true);
                break;

            //StaggerView的部分
            case R.id.stagger_view_vertical_standard:
                showStagger(true, false);
                break;
            case R.id.stagger_view_vertical_reverse:
                showStagger(true, true);
                break;
            case R.id.stagger_view_horizontal_standard:
                showStagger(false, false);
                break;
            case R.id.stagger_view_horizontal_reverse:
                showStagger(false, true);
                break;
            //多种条目类型
            case R.id.multi_type:
                //跳到一个新的Activity里面去实现这个功能
                Intent intent = new Intent(this, MultiTypeActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 这个方法用于实现瀑布流的效果
     */
    private void showStagger(boolean isVertical, boolean isReverse) {
        //准备布局管理器
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, isVertical ? StaggeredGridLayoutManager.VERTICAL : StaggeredGridLayoutManager.HORIZONTAL);

        //设置方向
        layoutManager.setReverseLayout(isReverse);

        //设置布局管理器到RecyclerView
        mList.setLayoutManager(layoutManager);

        //创建适配器
        mAdapter = new StaggerAdapter(mData);

        //设置适配器
        mList.setAdapter(mAdapter);
        //初始化事件
        initListener();

    }

    /**
     * 这个方法用于实现和GridView一样的效果
     */
    private void showGrid(boolean isVertical, boolean isReverse) {

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        layoutManager.setOrientation(isVertical ? GridLayoutManager.VERTICAL : GridLayoutManager.HORIZONTAL);
        layoutManager.setReverseLayout(isReverse);
        //设置布局管理器
        mList.setLayoutManager(layoutManager);


        //创建适配器
        mAdapter = new GridViewAdapter(mData);
        //设置适配器
        mList.setAdapter(mAdapter);
        //初始化事件
        initListener();
    }

    /**
     * 这个方法用于显示ListView一样的效果
     */
    private void showList(boolean isVertical, boolean isReverse) {

        //RecyclerView需要设定样式，其实就是设定布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        //设置水平还是垂直
        //设置标准（正向） 还是反向
        layoutManager.setOrientation(isVertical ? LinearLayoutManager.VERTICAL : LinearLayoutManager.HORIZONTAL);
        layoutManager.setReverseLayout(isReverse);


        mList.setLayoutManager(layoutManager);


        //创建适配器
        mAdapter = new ListViewAdapter(mData);

        //设置到RecyclerView里面
        mList.setAdapter(mAdapter);
        //初始化事件
        initListener();
    }
}