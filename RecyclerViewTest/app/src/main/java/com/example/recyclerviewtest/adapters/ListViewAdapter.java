package com.example.recyclerviewtest.adapters;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerviewtest.R;
import com.example.recyclerviewtest.recyclerviewtest.beans.ItemBean;

import java.util.List;

public class ListViewAdapter extends RecyclerViewBaseAdapter {
    //普通条目类型
    public static final int TYPE_NORMAL = 0;
    //加载更多
    public static final int TYPE_LOAD_MORE = 1;
    private OnRefreshListener mUpPullListenerRefresh;

    public ListViewAdapter(List<ItemBean> data) {
        super(data);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = getSubView(parent, viewType);
        Log.d("创建新的ViewHolder","sssss");
        if (viewType == TYPE_NORMAL) {
            return new InnerHolder(view);
        } else {
            return new LoadMoreHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d("绑定的ViewHolder","sss"+position+" "+getItemCount());
        if (getItemViewType(position) == TYPE_NORMAL && holder instanceof InnerHolder) {
            //在这里设置数据
            ((InnerHolder) holder).setData(mData.get(position), position);
        } else if (getItemViewType(position) == TYPE_LOAD_MORE && holder instanceof LoadMoreHolder) {
            ((LoadMoreHolder) holder).update(LoadMoreHolder.LOAD_STATE_LOADING);//第一个进来开始刷新数据
        }


    }

    @Override
    protected View getSubView(ViewGroup parent, int viewType) {

        View view;
        //根据类型来创建view
        if (viewType == TYPE_NORMAL) {
            //普通类型
            view = View.inflate(parent.getContext(), R.layout.item_list_view, null);
        } else {
            //这个是加载更多的类型
            view = View.inflate(parent.getContext(), R.layout.item_list_load_more, null);
        }

        return view;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            //最后一个则返回加载更多
            Log.d("我是最後一個",position+" ");
            return TYPE_LOAD_MORE;

        }

        return TYPE_NORMAL;
    }

    /**
     * 设置刷新的监听的接口
     */
    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.mUpPullListenerRefresh = onRefreshListener;
    }

    //最后一过View是加载更多，需要返回比data数量大一个的数量

    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size()+1;
        }
        return 0;
    }

    //定义接口
    public  interface  OnRefreshListener{
        void onUpPullRefresh(LoadMoreHolder loadMoreHolder);
    }
    public class LoadMoreHolder extends RecyclerView.ViewHolder {

        private final TextView mReload;
        private final LinearLayout mLoading;
        public static final int LOAD_STATE_LOADING = 0;
        public static final int LOAD_STATE_RELOAD = 1;
        public static final int LOAD_STATE_NORMAL = 2;

        public LoadMoreHolder(@NonNull View itemView) {
            super(itemView);
            Log.d("创建新的ViewHolder","");
            mLoading = itemView.findViewById(R.id.load_more);
            mReload = itemView.findViewById(R.id.reload);
            mReload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //这里要去触发加载数据
                    update(LOAD_STATE_LOADING);
                }
            });
        }

        public void update(int state) {
            mLoading.setVisibility(View.GONE);
            mReload.setVisibility(View.GONE);
            Log.d("创建新的ViewHolder","");
            switch (state) {
                //重置控件的状态
                case LOAD_STATE_LOADING:
                    mLoading.setVisibility(View.VISIBLE);
                    //触发加载数据
                    startLoadMore();
                    break;
                case LOAD_STATE_RELOAD:
                    mReload.setVisibility(View.VISIBLE);
                    break;
                case LOAD_STATE_NORMAL:
                    mLoading.setVisibility(View.GONE);
                    mReload.setVisibility(View.GONE);
                    break;
            }

        }

        private void startLoadMore() {
            if (mUpPullListenerRefresh != null) {
                mUpPullListenerRefresh.onUpPullRefresh(this);
            }
        }
    }
}
