package com.example.recyclerviewtest.adapters;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerviewtest.MainActivity;
import com.example.recyclerviewtest.R;
import com.example.recyclerviewtest.recyclerviewtest.beans.ItemBean;

import java.util.List;

public  abstract class RecyclerViewBaseAdapter extends RecyclerView.Adapter{
    protected final List<ItemBean> mData;
    private OnItemClickListener mOnItemClickListener;

    public  RecyclerViewBaseAdapter(List<ItemBean> data){
        mData = data;
    }

    /**
     * 这个方法用于创建条目的View
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //在这里面创建条目的UI
        //传进去的这个View其实就是条目的界面
        //两个步骤
        //1.拿到View
        //2.创建InnerHolder
        View view = getSubView(parent,viewType);
        return new InnerHolder(view);
    }

    protected abstract View getSubView(ViewGroup parent,int viewType);


    /**
     * 这个方法用于绑定Holder的，一般用来设置数据
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //在这里设置数据
        ((InnerHolder)holder).setData(mData.get(position),position);

    }
    /**
     *  return the number of views
     */
    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        }

        return 0;
    }

    public  void setOnItemClickListener(OnItemClickListener onItemClickListener){
        //设置一个监听，其实就是设置一个接口，一个回调的接口
        mOnItemClickListener =  onItemClickListener;


    };
    /**
     * 编写回调的步骤
     * 1.创建这个接口
     * 2.定义接口内部的方法
     * 3.提供设置接口的方法（在外部进行实现）
     * 4.接口方法的调用
     * */
    public interface  OnItemClickListener{
        void onItemClick(int position);
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        private ImageView mIcon;
        private TextView mTitle;
        private int mPosition;


        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            mIcon = itemView.findViewById(R.id.icon);
            mTitle = itemView.findViewById(R.id.title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(mPosition);
                }
            });
        }
        /**
         * 这个方法用于设置数据
         *
         * @param itemBean
         */
        public void setData(ItemBean itemBean,int position) {
            mPosition = position;

            mIcon.setImageResource(itemBean.icon);
            mTitle.setText(itemBean.title);
        }
    }
}
