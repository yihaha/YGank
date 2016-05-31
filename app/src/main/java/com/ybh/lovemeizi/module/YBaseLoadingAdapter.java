package com.ybh.lovemeizi.module;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.socks.library.KLog;
import com.ybh.lovemeizi.R;
import com.ybh.lovemeizi.utils.ToastSnackUtil;

import java.util.List;

/**
 * Created by y on 2016/5/12.
 */
public abstract class YBaseLoadingAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "YBaseLoadingAdapter";
    public List<T> mList;
    private RecyclerView mRecyclerView;

    //正常条目
    private static final int TYPE_NORMAL_ITEM = 0;
    //加载条目
    private static final int TYPE_LOADING_ITEM = 1;
    //首次进入
    private boolean mFirstEnter = true;
    //是否正在加载
    private boolean mIsLoading;

    private OnLoadingListener onLoadingListener;
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    private LoadingViewHolder mLoadingViewHolder;

    public YBaseLoadingAdapter(RecyclerView recyclerView, List<T> list) {
        this.mList = list;
        this.mRecyclerView = recyclerView;
        setCountSpan(mRecyclerView);
    }


    public interface OnLoadingListener {
        void onLoading();
    }

    /**
     * 设置监听接口的方法
     *
     * @param onLoadingListener
     */
    public void setOnLoadingListener(OnLoadingListener onLoadingListener) {
        this.onLoadingListener = onLoadingListener;
        setScrollListener(mRecyclerView);
    }

    /**
     * 滚动监听
     *
     * @param recyclerView
     */
    public void setScrollListener(RecyclerView recyclerView) {
        if (mRecyclerView == null) {
            return;
        }

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //1代表从下滚上来
                if (!ViewCompat.canScrollVertically(recyclerView, 1)) {
                    //没有在加载,也不是第一次进入才执行
                    if (!mIsLoading && !mFirstEnter) {
                        listAddLast();
                        mIsLoading = true;
                        if (mLoadingViewHolder != null) {
                            mLoadingViewHolder.mProgressBar.setVisibility(View.VISIBLE);
                            mLoadingViewHolder.mTextView.setText("正在加载...");
                        }
                        if (onLoadingListener != null) { //调用加载
                            onLoadingListener.onLoading();
                        }
                    }

                    if (mFirstEnter) { //已经进来设置为false
                        mFirstEnter = false;
                    }
                }

            }
        });
    }

    /**
     * 集合最后添加一个null,为了判断返回类型
     */
    private void listAddLast() {
        if (mList != null && mList.size() > 0) {
            mList.add(null);
            notifyItemInserted(mList.size() - 1);
        }
    }

    /**
     * 注意:从方法内容来看,此方法应该在将获取的新内容添加到集合之前调用
     * 为了移出已经添加到集合为了判断加载布局显示的数据
     */
    public void setLoadingComplete() {
        mIsLoading = false;
        if (mList != null && mList.size() > 0) {
            mList.remove(mList.size() - 1);
            notifyItemRemoved(mList.size() - 1);
        }
    }


    /**
     * 设置加载item占据一整行
     *
     * @param mRecyclerView
     */
    private void setCountSpan(RecyclerView mRecyclerView) {
        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        if (layoutManager == null) {
            ToastSnackUtil.snackbarLong(mRecyclerView, TAG + "layoutManager=null");
            KLog.w(TAG + "layoutManager=null");
        }
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int itemViewType = getItemViewType(position);
                    if (itemViewType == TYPE_NORMAL_ITEM) {
                        return 1;
                    } else {
                        return gridLayoutManager.getSpanCount();
                    }
                }
            });
        }

        if (layoutManager instanceof StaggeredGridLayoutManager) {
            //后面会用到
            mStaggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
        }

    }

    /**
     * 创建ViewHolder
     *
     * @param parent
     * @return
     */
    public abstract RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent);

    /**
     * 绑定ViewHolder
     *
     * @param viewHolder
     * @param position
     */
    public abstract void onBindNormalViewHolder(RecyclerView.ViewHolder viewHolder, int position);


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_NORMAL_ITEM) {
            return onCreateNormalViewHolder(parent);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bottomloading, parent, false);
            mLoadingViewHolder = new LoadingViewHolder(view);
            return mLoadingViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        if (itemViewType == TYPE_NORMAL_ITEM) {
            onBindNormalViewHolder(holder, position);
        } else {
            if (mStaggeredGridLayoutManager != null) {
                //设置瀑布流底部加载布局横跨全列
                StaggeredGridLayoutManager.LayoutParams layoutParams = new StaggeredGridLayoutManager
                        .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                        , 100); //100px
                layoutParams.setFullSpan(true);  //关键
                mLoadingViewHolder.mLinearLayout.setLayoutParams(layoutParams);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        T t = mList.get(position);
        if (t == null) { //不存在返回加载类型
            return TYPE_LOADING_ITEM;
        } else {
            return TYPE_NORMAL_ITEM;
        }
    }

    /**
     * 加载ViewHolder
     */
    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mLinearLayout;
        ProgressBar mProgressBar;
        TextView mTextView;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            mLinearLayout = (LinearLayout) itemView.findViewById(R.id.bottom_loading_layout);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.loading_pro);
            mTextView = (TextView) itemView.findViewById(R.id.loading_text);
        }
    }
}
