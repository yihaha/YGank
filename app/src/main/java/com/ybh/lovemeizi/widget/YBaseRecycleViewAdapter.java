package com.ybh.lovemeizi.widget;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by y on 2016/5/11.
 */
public class YBaseRecycleViewAdapter <T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {
    public YBaseRecycleViewAdapter(){

    }

    @Override
    public T onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(T holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
