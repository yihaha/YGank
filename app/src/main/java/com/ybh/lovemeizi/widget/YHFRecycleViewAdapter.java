package com.ybh.lovemeizi.widget;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by y on 2016/5/11.
 */
public class YHFRecycleViewAdapter<T extends RecyclerView.Adapter> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private T mAdapter;
    private boolean isStaggeredGrid; //判断是否是瀑布流

    //头/脚布局类型
    private static final int HEADER_TYPE = -1 << 10;
    private static final int FOOTER_TYPE = -1 << 11;

    //存储头/脚布局的集合
    private List<FixedViewInfo> mHeaderList = new ArrayList<>();
    private List<FixedViewInfo> mFooterList = new ArrayList<>();

    //和Listview类似
    public class FixedViewInfo {
        public View view;
        public int viewType;
    }

    public YHFRecycleViewAdapter(T adapter) {
        this.mAdapter = adapter;
    }


    /**
     * 添加头布局
     *
     * @param view
     */
    public void addHeaderView(View view) {
        if (null == view) {
            throw new RuntimeException("the view to add must not be null!");
        }
        FixedViewInfo fixedViewInfo = new FixedViewInfo();
        fixedViewInfo.view = view;
        fixedViewInfo.viewType = HEADER_TYPE + mHeaderList.size();
        mHeaderList.add(fixedViewInfo);
        notifyDataSetChanged();

    }

    /**
     * 添加脚布局
     *
     * @param view
     */
    public void addFooterView(View view) {
        if (null == view) {
            throw new RuntimeException("the view to add must not be null!");
        }
        FixedViewInfo fixedViewInfo = new FixedViewInfo();
        fixedViewInfo.view = view;
        fixedViewInfo.viewType = FOOTER_TYPE + mFooterList.size();
        mFooterList.add(fixedViewInfo);
        notifyDataSetChanged();

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (isHeaderType(viewType)) {
            int abs = Math.abs(viewType - HEADER_TYPE);
            View view = mHeaderList.get(abs).view;
            return onCreateHeaderFooterViewHolder(view);
        } else if (isFooterType(viewType)) {
            int abs = Math.abs(viewType - FOOTER_TYPE);
            View view = mFooterList.get(abs).view;
            return onCreateHeaderFooterViewHolder(view);
        } else {
            return mAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeaderPosition(position)) {
        } else if (isFooterPosition(position)) {
        } else {
            mAdapter.onBindViewHolder(holder, position - mHeaderList.size());
        }
    }


    @Override
    public int getItemCount() {
        return mHeaderList.size() + mAdapter.getItemCount() + mFooterList.size();
    }


    @Override
    public int getItemViewType(int position) {
        if (isHeaderPosition(position)) {
            return mHeaderList.get(position).viewType;
        } else if (isFooterPosition(position)) {
            return mFooterList.get(position - mHeaderList.size() - mAdapter.getItemCount()).viewType;
        } else {
            return mAdapter.getItemViewType(position - mHeaderList.size());
        }
    }

    /**
     * 头/脚布局的viewHolder
     *
     * @param view
     * @return
     */
    private RecyclerView.ViewHolder onCreateHeaderFooterViewHolder(View view) {
        if (isStaggeredGrid) {
            StaggeredGridLayoutManager.LayoutParams params = new StaggeredGridLayoutManager.LayoutParams(
                    StaggeredGridLayoutManager.LayoutParams.MATCH_PARENT, StaggeredGridLayoutManager.LayoutParams.WRAP_CONTENT);
            params.setFullSpan(true);
            view.setLayoutParams(params);
        }
        return new RecyclerView.ViewHolder(view) {
        };
    }


    /**
     * 根据索引判断是否是头布局
     *
     * @param position
     * @return
     */
    private boolean isHeaderPosition(int position) {
        return position < mHeaderList.size();
    }

    private boolean isFooterPosition(int position) {
        return position >= mHeaderList.size() + mAdapter.getItemCount();
    }


    /**
     * 根据类型判断是否是头布局
     *
     * @param type
     * @return
     */
    private boolean isHeaderType(int type) {
        return type >= HEADER_TYPE && type < HEADER_TYPE + mHeaderList.size();
    }

    private boolean isFooterType(int type) {
        return type >= FOOTER_TYPE && type < FOOTER_TYPE + mFooterList.size();
    }


    /**
     * 设置头/脚布局占用一整行
     *
     * @param recyclerView
     */
    public void setConutSpan(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager == null) {
            throw new RuntimeException("layoutManager is null");
        }
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    boolean itemViewType = isHeaderPosition(position) || isFooterPosition(position);
                    return itemViewType ? gridLayoutManager.getSpanCount() : 1;
                }
            });
        }

        if (layoutManager instanceof StaggeredGridLayoutManager) {
            this.isStaggeredGrid = true;
        }

    }

    /**
     * 设置是否隐藏头布局
     *
     * @param isShow
     */
    public void setHeaderViewVisibility(boolean isShow) {
        if (mHeaderList.size() > 0) {
            for (FixedViewInfo fixedViewInfo : mHeaderList) {
                fixedViewInfo.view.setVisibility(isShow ? View.VISIBLE : View.GONE);
            }
            notifyDataSetChanged();
        }
    }

    /**
     * 设置是否隐藏脚布局
     *
     * @param isShow
     */
    public void setFooterViewVisibility(boolean isShow) {
        if (mFooterList.size() > 0) {
            for (FixedViewInfo fixedViewInfo : mFooterList) {
                fixedViewInfo.view.setVisibility(isShow ? View.VISIBLE : View.GONE);
            }
            notifyDataSetChanged();
        }
    }

    /**
     * 得到传入的adapter
     *
     * @return
     */
    public T getAdapter() {
        return mAdapter;
    }

    /**
     * 移除脚布局
     */
    public void removeFooter() {
        if (mFooterList.size() > 0) {
            for (int i = 0; i < mFooterList.size(); i++) {
                mFooterList.remove(i);
            }
            notifyDataSetChanged();
        }
    }


}
