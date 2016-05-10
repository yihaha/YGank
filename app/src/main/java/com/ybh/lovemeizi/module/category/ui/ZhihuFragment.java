package com.ybh.lovemeizi.module.category.ui;


import android.os.Bundle;
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
import com.ybh.lovemeizi.module.category.adapter.ZhihuAdapter;
import com.ybh.lovemeizi.utils.DateUtil;

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

    @Bind(R.id.swipelayout)
    SwipeRefreshLayout mSwiRefreshLayout;

    @Bind(R.id.recycleview)
    RecyclerView mRecycleView;
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
        mAdapter = new ZhihuAdapter(mZhihuList);
        mRecycleView.setAdapter(mAdapter);
        mRecycleView.setLayoutManager(linearLayoutManager);
        // 刷新时，指示器旋转后变化的颜色
//        mSwiRefreshLayout.setColorSchemeResources(R.color.md_black_1000, R.color.white);
        mSwiRefreshLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        loadData(1);
    }

    private void loadData(int page, String... type) {
        //通过计算得到今天,昨天等的数据
        long time = new Date().getTime()-(page-1)*dateLimit;
        Date date = new Date(time);
//        昨日最新（yesterday）、近日热门（recent）和历史精华（archive）
        if ((type==null)||(! (type.length>0))){
            type=new String[1];
            type[0]="yesterday";
        }
        kanZhihuApi.getZhihuJson(DateUtil.onDate2String(date, "yyyyMMdd"), type[0])
                .map(new Func1<KanzhihuAll, List<KanzhihuBean>>() {
                    @Override
                    public List<KanzhihuBean> call(KanzhihuAll kanzhihuAll) {
                        List<KanzhihuBean> zhuhuAllList = kanzhihuAll.zhuhuAllList;
                        mZhihuList.addAll(zhuhuAllList);
                        return zhuhuAllList;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<KanzhihuBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.w("zhifragment_error",e.toString());
                    }

                    @Override
                    public void onNext(List<KanzhihuBean> kanzhihuBeen) {
                        mAdapter.notifyDataSetChanged();
                        KLog.w("zhifragment_next","得到");
                    }
                });
    }

}
