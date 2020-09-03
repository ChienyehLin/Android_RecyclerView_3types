package com.example.recyclerviewtest.adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.recyclerviewtest.R;
import com.example.recyclerviewtest.recyclerviewtest.beans.MultiType;

import java.util.List;

public class MultiTypeAdapter extends RecyclerView.Adapter {

    private final List<MultiType> mData;
    //定义三个常量标识，因为有三种类型
    public static final int TYPE_FULL_IMAGE = 0;
    public static final int TYPE_LEFT_IMAGE = 1;
    public static final int TYPE_THREE_IMAGE = 2;

    public MultiTypeAdapter(List<MultiType> data) {
        mData = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        /**
         * 根据ViewType来创建对应的条目， 这样子条目就不一样了
         * */
        View view;
        //TODO:这里面去创建ViewHolder
        if (viewType == TYPE_FULL_IMAGE) {
            view = View.inflate(parent.getContext(), R.layout.item_type_full_image,null);//TODO
            return new FullImageHolder(view);
        } else if (viewType == TYPE_LEFT_IMAGE) {
            view = View.inflate(parent.getContext(), R.layout.item_type_left_title_right_image,null);
            return  new LeftImageHolder(view);
            //TODO
        } else {
            //TODO
            view = View.inflate(parent.getContext(), R.layout.item_type_three_image,null);
            return  new ThreeImageHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //这里就不设定数据了

    }

    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        }
        return 0;
    }

    //我们要写一个方法，这个方法根据条件来返回条目类型

    @Override
    public int getItemViewType(int position) {
        MultiType multiType = mData.get(position);
        if (multiType.type == 0)
            return TYPE_FULL_IMAGE;
        else if (multiType.type == 1)
            return TYPE_LEFT_IMAGE;
        else
            return TYPE_THREE_IMAGE;
    }

    //多种条目类型的Holder
    public class FullImageHolder extends RecyclerView.ViewHolder {

        public FullImageHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public class LeftImageHolder extends RecyclerView.ViewHolder {

        public LeftImageHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public class ThreeImageHolder extends RecyclerView.ViewHolder {

        public ThreeImageHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
