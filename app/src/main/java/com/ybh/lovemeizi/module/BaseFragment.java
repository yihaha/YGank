package com.ybh.lovemeizi.module;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
    @Bind(R.id.swipelayout)
    public SwipeRefreshLayout mSwiRefreshLayout;
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
        ButterKnife.bind(this, view);
//        mSwiRefreshLayout.setColorSchemeResources(R.color.md_black_1000, R.color.white);

    }
}
