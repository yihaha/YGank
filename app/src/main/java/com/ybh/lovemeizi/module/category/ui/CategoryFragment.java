package com.ybh.lovemeizi.module.category.ui;


import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ybh.lovemeizi.R;
import com.ybh.lovemeizi.module.BaseFragment;
import com.ybh.lovemeizi.module.category.adapter.CategoryAdapter;

import butterknife.Bind;

/**
 * Created by y on 2016/5/10.
 */
public class CategoryFragment extends BaseFragment {

    @Bind(R.id.swipelayout)
    SwipeRefreshLayout mSwiRefreshLayout;

    @Bind(R.id.recycleview)
    RecyclerView mRecycleView;
    private CategoryAdapter categoryAdapter;
    private String flag;

    @Override
    protected int setContentLayout() {
        return R.layout.yfragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        flag = getArguments().getString("flag");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        categoryAdapter = new CategoryAdapter();
        mRecycleView.setAdapter(categoryAdapter);
        mRecycleView.setLayoutManager(linearLayoutManager);
        // 刷新时，指示器旋转后变化的颜色
        mSwiRefreshLayout.setColorSchemeResources(R.color.md_black_1000, R.color.white);

        initData();
        loadData();
    }

    private void loadData() {
        //如果是知乎
        if (flag.equals(getResources().getStringArray(R.array.fragments_title)[0])){

        }
    }

    private void initData() {

    }
}
