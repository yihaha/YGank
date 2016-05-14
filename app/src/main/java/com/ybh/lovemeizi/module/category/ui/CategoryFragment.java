package com.ybh.lovemeizi.module.category.ui;


import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.socks.library.KLog;
import com.ybh.lovemeizi.R;
import com.ybh.lovemeizi.http.ApiServiceFactory;
import com.ybh.lovemeizi.http.gankio.GankRetrofitService;
import com.ybh.lovemeizi.model.gankio.AllData;
import com.ybh.lovemeizi.model.gankio.GankData;
import com.ybh.lovemeizi.module.BaseFragment;
import com.ybh.lovemeizi.module.YBaseLoadingAdapter;
import com.ybh.lovemeizi.module.category.adapter.AndroidIosAdapter;
import com.ybh.lovemeizi.module.category.adapter.MeituAdapter;
import com.ybh.lovemeizi.utils.ToastSnackUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by y on 2016/5/10.
 */
public class CategoryFragment extends BaseFragment {
    private static final String TAG = "CategoryFragment";
    private String flag;

    private GankRetrofitService mService = ApiServiceFactory.getSingleService();
    private List<GankData> mGankDatas;
    private int DEFAULT_PAGECOUNT = 16;//默认请求16条数据
    private int DEFAULT_CURRPAGE = 1;//默认第一页数据
    private int pageCount = DEFAULT_PAGECOUNT;
    private int currPage = DEFAULT_CURRPAGE;
    private RecyclerView.LayoutManager layoutManager;
    private YBaseLoadingAdapter yBaseLoadingAdapter;

    @Override
    protected int setContentLayout() {
        return R.layout.yfragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        flag = getArguments().getString("flag");
        mGankDatas = new ArrayList<>();
        if (flag.equals("美图")) {
            //竖直分成两列瀑布流
            layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            mRecycleView.setLayoutManager(layoutManager); //放在这里是因为adapter里面用到这个属性
            yBaseLoadingAdapter = new MeituAdapter(mRecycleView, mGankDatas);
            mRecycleView.setPadding(0, 8, 0, 0); //瀑布流自带有间隙,此处进行缩小
        } else {
            layoutManager = new LinearLayoutManager(getActivity());
            mRecycleView.setLayoutManager(layoutManager);
            yBaseLoadingAdapter = new AndroidIosAdapter(mRecycleView, mGankDatas);
        }
        mRecycleView.setAdapter(yBaseLoadingAdapter);
        mSwiRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currPage = DEFAULT_CURRPAGE;
                loadData(pageCount, currPage);
            }
        });

        loadData(pageCount, currPage);
        yBaseLoadingAdapter.setOnLoadingListener(new YBaseLoadingAdapter.OnLoadingListener() {
            @Override
            public void onLoading() {
                currPage++;
                loadData(pageCount, currPage);
            }
        });

    }


    /**
     * @param pageCount 每页数据数量
     * @param currPage  当前是第几页
     */
    private void loadData(int pageCount, final int currPage) {
        Observable<AllData> dataObservable = null;
        switch (flag) {
            case "Android":
                dataObservable = mService.getAndroidList(pageCount, currPage);
                break;
            case "ios":
                dataObservable = mService.getIosList(pageCount, currPage);
                break;
            case "美图":
                dataObservable = mService.getMeiziList(pageCount, currPage);
                break;
            default:
                break;
        }

        if (dataObservable != null) {
            dataObservable
                    .map(new Func1<AllData, List<GankData>>() {
                        @Override
                        public List<GankData> call(AllData allData) {
                            return allData.results;
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<List<GankData>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            setRefresh(false);
                            yBaseLoadingAdapter.setLoadingComplete();
                            KLog.w(TAG+"得到数据");
//                            ToastSnackUtil.snackbarLong(mSwiRefreshLayout, TAG + "异常: " + e.toString());
                        }

                        @Override
                        public void onNext(final List<GankData> gankDatas) {
                            KLog.w(TAG+"得到数据");
                            setRefresh(false);//取消刷新
                            //为了让加载更多消失的不会太快
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    yBaseLoadingAdapter.setLoadingComplete();
                                    if (currPage == 1 && mGankDatas.size() > 0) {
                                        mGankDatas.clear();
                                    }
                                    mGankDatas.addAll(gankDatas);
//                                    if (flag.equals("美图")) {
//                                        yBaseLoadingAdapter.notifyItemRangeChanged(1, mGankDatas.size()-1);
//                                    } else {
//                                        yBaseLoadingAdapter.notifyDataSetChanged();
//                                    }
                                        yBaseLoadingAdapter.notifyDataSetChanged();
                                }
                            }, 1500);

                        }
                    });
        }

    }


}
