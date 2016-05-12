package com.ybh.lovemeizi.module.category.ui;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.socks.library.KLog;
import com.ybh.lovemeizi.R;
import com.ybh.lovemeizi.http.ApiServiceFactory;
import com.ybh.lovemeizi.http.zhihu.KanZhihuApi;
import com.ybh.lovemeizi.model.kanzhihu.KanzhihuAll;
import com.ybh.lovemeizi.model.kanzhihu.KanzhihuBean;
import com.ybh.lovemeizi.module.BaseFragment;
import com.ybh.lovemeizi.module.YBaseLoadingAdapter;
import com.ybh.lovemeizi.module.category.adapter.ZhihuAdapter;
import com.ybh.lovemeizi.utils.DateUtil;
import com.ybh.lovemeizi.utils.ToastSnackUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by y on 2016/5/10.
 */
public class ZhihuFragment extends BaseFragment {
    private static final String TAG = "ZhihuFragment";

    private int currPage = 1;

    private String flag;
    private KanZhihuApi kanZhihuApi = ApiServiceFactory.getZhihuSingle();
    private long dateLimit = 24 * 60 * 60 * 1000; //一天的时间
    private List<KanzhihuBean> mZhihuList;
    private ZhihuAdapter mAdapter;

    @Override
    protected int setContentLayout() {
        return R.layout.yfragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mZhihuList = new ArrayList<>();
        flag = getArguments().getString("flag");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecycleView.setLayoutManager(linearLayoutManager);
        mAdapter = new ZhihuAdapter(mRecycleView, mZhihuList);
        mRecycleView.setAdapter(mAdapter);
        // 刷新时，指示器旋转后变化的颜色
//        mSwiRefreshLayout.setColorSchemeResources(R.color.md_black_1000, R.color.white);
        mSwiRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currPage = 1;
                loadData(currPage);
            }
        });

        loadData(currPage);
        mAdapter.setOnLoadingListener(new YBaseLoadingAdapter.OnLoadingListener() {
            @Override
            public void onLoading() {
                currPage++;
                loadData(currPage);
            }
        });
    }

    private void loadData(final int page, String... type) {
        //通过计算得到今天,昨天等的数据
        long time = new Date().getTime() - (page - 1) * dateLimit;
        Date date = new Date(time);
//        昨日最新（yesterday）、近日热门（recent）和历史精华（archive）
        if ((type == null) || (!(type.length > 0))) {
            type = new String[1];
            type[0] = "yesterday";
        }
        kanZhihuApi.getZhihuJson(DateUtil.onDate2String(date, "yyyyMMdd"), type[0])
                .map(new Func1<KanzhihuAll, List<KanzhihuBean>>() {
                    @Override
                    public List<KanzhihuBean> call(KanzhihuAll kanzhihuAll) {
                        return kanzhihuAll.zhuhuAllList;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<KanzhihuBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        setRefresh(false);
                        mAdapter.setLoadingComplete(); //取消加载布局
                        KLog.w(TAG, e.toString());
                        ToastSnackUtil.snackbarLong(mRecycleView, TAG + "异常: " + e.toString());
                    }

                    @Override
                    public void onNext(final List<KanzhihuBean> kanzhihuBeen) {
                        KLog.w("zhifragment_next", "得到");
                        setRefresh(false);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.setLoadingComplete(); //取消加载布局
                                //将旧数据清除
                                if (page == 1 && mZhihuList.size() > 0) {
                                    mZhihuList.clear();
                                }
                                mZhihuList.addAll(kanzhihuBeen);
//                        mAdapter.notifyItemRangeChanged(0, mZhihuList.size() - 1);
                                mAdapter.notifyDataSetChanged();
                            }
                        }, 1500);
                    }
                });
    }

}
