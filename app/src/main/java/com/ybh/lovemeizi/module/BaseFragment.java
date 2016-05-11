package com.ybh.lovemeizi.module;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ybh.lovemeizi.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by y on 2016/5/10.
 */
public class BaseFragment extends Fragment {
//    @Bind(R.id.swipelayout)
    public SwipeRefreshLayout mSwiRefreshLayout;
    public RecyclerView mRecycleView;
    private View view;

    /**
     *返回布局
     */
    protected int setContentLayout(){
        return -1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        view = inflater.inflate(setContentLayout(),container,false);
        return view;
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        ButterKnife.bind(this, view);
        mSwiRefreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.swipelayout);
        mRecycleView= (RecyclerView) view.findViewById(R.id.recycleview);
        mSwiRefreshLayout.setColorSchemeResources(R.color.md_black_1000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setRefresh(true);
            }
        },300);


    }

    /**
     * 刷新
     * @param tag
     */
    public void setRefresh(boolean tag){
        if (mSwiRefreshLayout!=null){
            if (!tag){
                //刷新消失不会太快
                mSwiRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwiRefreshLayout.setRefreshing(false);
                    }
                },1500);
            }else {
                mSwiRefreshLayout.setRefreshing(true);
            }
        }
    }

}
