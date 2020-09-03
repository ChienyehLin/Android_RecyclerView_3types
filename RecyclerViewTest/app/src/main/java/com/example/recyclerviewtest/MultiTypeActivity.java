package com.example.recyclerviewtest;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerviewtest.adapters.MultiTypeAdapter;
import com.example.recyclerviewtest.recyclerviewtest.beans.Datas;
import com.example.recyclerviewtest.recyclerviewtest.beans.MultiType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class  MultiTypeActivity extends AppCompatActivity {

    private static final String TAG = MultiTypeActivity.class.getSimpleName() ;
    private RecyclerView mRecyclerView;
    private List<MultiType> mData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_type);


        //find the recyclerView here
        mRecyclerView = findViewById(R.id.multi_type_list);
        initData();

        //创建和设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        //创建适配器
        MultiTypeAdapter adapter = new MultiTypeAdapter(mData);


        //设置适配器
        mRecyclerView.setAdapter(adapter);

    }

    private void initData() {
        //准备数据
        Random random = new Random();
        mData = new ArrayList<>();
        for (int i = 0; i < Datas.icons.length; i++) {
            MultiType data =  new MultiType();
            data.pic= Datas.icons[i];
            data.type=random.nextInt(3);
            Log.d(TAG,"type =====>"+data.type);
            mData.add(data);
        }
    }
}
