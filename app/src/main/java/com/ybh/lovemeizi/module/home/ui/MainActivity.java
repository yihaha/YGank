package com.ybh.lovemeizi.module.home.ui;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;


import com.socks.library.KLog;
import com.ybh.lovemeizi.Contant;
import com.ybh.lovemeizi.R;
import com.ybh.lovemeizi.http.gankio.GankRetrofitService;
import com.ybh.lovemeizi.http.ApiServiceFactory;
import com.ybh.lovemeizi.model.gankio.AllData;
import com.ybh.lovemeizi.model.gankio.FewDayData;
import com.ybh.lovemeizi.model.gankio.GankData;
import com.ybh.lovemeizi.utils.DateUtil;
import com.ybh.lovemeizi.utils.PreferenceUtil;
import com.ybh.lovemeizi.module.BaseActivity;
import com.ybh.lovemeizi.module.home.adapter.MainRecyclAdapter;
import com.ybh.lovemeizi.utils.StatusBarUtil;
import com.ybh.lovemeizi.utils.ToastSnackUtil;
import com.ybh.lovemeizi.widget.yrefreshview.YRefreshLayout;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.update.BmobUpdateAgent;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity {

    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @Bind(R.id.coordinatorlayout)
    CoordinatorLayout mCoordinatorLayout;

    @Bind(R.id.meizi_listview)
    RecyclerView mRecycleView;

    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Bind(R.id.navigation_view)
    NavigationView mNavigationView;

    @Bind(R.id.relayout)
    YRefreshLayout mRefreshLayout;
    private static final String TAG = "MainActivity";

    private MainRecyclAdapter mainRecyclAdapter;
    private final static int AVGCOUNT = 10; //每页数据
    private int page = 1;

    //    private List<GankData> meiziList = new ArrayList<>();
    private List<FewDayData.YData> mList = new ArrayList<>();
    private GankRetrofitService gService = ApiServiceFactory.getSingleService();

    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorForDrawerLayout(this, (DrawerLayout) findViewById(R.id.drawer_layout), colorPrimary, 0);
    }

    @Override
    public void initView() {
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
        actionBarDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mRefreshLayout.setOnRefreshListener(new YRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefreshing() {
                page = 1;
                newLoadData(page);
            }

            @Override
            public void onLoading() {
                page++;
                newLoadData(page);
            }
        });

        mainRecyclAdapter = new MainRecyclAdapter(MainActivity.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(linearLayoutManager);
        mRecycleView.setHasFixedSize(true);//item固定高度可以提高性能
        mRecycleView.setAdapter(mainRecyclAdapter);

        // 初始化BmobSDK
        Bmob.initialize(this, Contant.BOMB_APPID);

        //初始化建表操作,执行一次后,就应该注释掉
//        BmobUpdateAgent.initAppVersion(this);

        //取消只有在wifi情况下才更新
        BmobUpdateAgent.setUpdateOnlyWifi(false);
        //默认在wifi下才更新
        BmobUpdateAgent.update(this);

    }


    @Override
    public void initData() {
    }

    /**
     * 2016/05/15 根据新api写的另一个方法,以前的loadData方法没有这个适合展示首页数据
     *
     * @param page
     */
    private void newLoadData(final int page) {
        final Subscription subscribe = gService.getFewDayData(AVGCOUNT, page)
                .map(new Func1<FewDayData, List<FewDayData.YData>>() {
                    @Override
                    public List<FewDayData.YData> call(FewDayData fewDayData) {
                        List<FewDayData.YData> results = fewDayData.results;
                        KLog.w(TAG, "数据数量:  " + results.size());
                        for (FewDayData.YData yData : results) {
                            parseContent(yData.content, yData);
                        }
                        return results;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<FewDayData.YData>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        finishReorLoad();
                        KLog.w(TAG, e.toString());
                    }

                    @Override
                    public void onNext(List<FewDayData.YData> yDatas) {
                        finishReorLoad();
                        if (page == 1 && mList.size() > 0) { //刷新的时候要将旧数据清空
                            mList.clear();
                        }
                        mList.addAll(yDatas);
                        mainRecyclAdapter.setRefresh(mList);
                        KLog.w(TAG, "得到数据");
                    }
                });
        addSubscription(subscribe);
    }


    /**
     * 请求数据
     */
//    private void loadData(final int page) {
//        Subscription subscribe = Observable.zip(gService.getMeiziList(AVGCOUNT, page), gService.getVideoList(AVGCOUNT, page)
//                , new Func2<AllData, AllData, AllData>() {
//                    @Override
//                    public AllData call(AllData picAll, AllData videoAll) {
//                        return onMerageDesc(picAll, videoAll);
//                    }
//                })
//                .map(new Func1<AllData, List<GankData>>() {
//                    @Override
//                    public List<GankData> call(AllData allData) {
//                        return allData.results;
//                    }
//                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<List<GankData>>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        finishReorLoad();
////                        mRefreshLayout.finishRefreshing();
//                        KLog.w("onError", e + "");
////                        ToastSnackUtil.snackbarLong(mRefreshLayout, TAG + "异常: " + e.toString());
//                    }
//
//                    @Override
//                    public void onNext(List<GankData> gankDatas) {
//                        if (page == 1 && meiziList.size() > 0) { //刷新的时候要将旧数据清空
//                            meiziList.clear();
//                        }
//                        finishReorLoad();
//                        meiziList.addAll(gankDatas);
//                        mainRecyclAdapter.setRefresh(meiziList);
////                        mRefreshLayout.finishRefreshing();
//                    }
//                });
//        addSubscription(subscribe);
//
//    }

    /**
     * 为了保持刷新效果小于1秒时,也有1秒的效果
     */
    private void finishReorLoad() {
        mRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.finishRefreshing();
            }
        }, 1000);
    }

//    /**
//     * 将视频内容的说明设置到图片说明
//     *
//     * @param picAll
//     * @param videoAll
//     * @return
//     */
//    private AllData onMerageDesc(AllData picAll, AllData videoAll) {
//        int maxLength = picAll.results.size() > videoAll.results.size() ? picAll.results.size() : videoAll.results.size();
//        for (int i = 0; i < maxLength; i++) {
//            GankData picGank = picAll.results.get(i);
//            GankData videoGank = videoAll.results.get(i);
//            //可能发生当前图片和当前视频不是同一天内容,进入当天详情时"描述内容"与首页内容不同,会误认为发生数据请求错误
//            if (DateUtil.onDate2String(picGank.publishedAt, "yyyy/MM/dd")
//                    .equals(DateUtil.onDate2String(videoGank.publishedAt, "yyyy/MM/dd"))) {
//                picGank.desc = videoGank.desc;
//            }
//        }
//        return picAll;
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        int itemId = item.getItemId();
//        switch (itemId) {
//
//            case R.id.action_day_night_mode:
//                PreferenceUtil preferenceUtil = new PreferenceUtil(MainActivity.this);
//                boolean isNightMode = preferenceUtil.getBoolean(Contant.DAY_NIGHT_MODE);
//                if (isNightMode) {
//                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                } else {
//                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                }
////                KLog.w("夜间模式",isNight);
//                isNightMode = !isNightMode;
//                preferenceUtil.saveBoolean(Contant.DAY_NIGHT_MODE, isNightMode);
//                recreate();
//                return true;
//            default:
//                break;
//        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
//        mHasDrawLayout=true;
        defaultCheckedItem = R.id.nav_home;
        onNavigationViewItemChecked();
    }


    /**
     * 解析数据
     * 以下内容参考自: http://www.open-open.com/jsoup/
     *
     * @param content
     */
    private void parseContent(String content, FewDayData.YData yData) {
        Document document = Jsoup.parseBodyFragment(content);
        Element body = document.body();
        String spareDesc = ""; //当没有视频信息,是去Android的第一个描述信息

        Elements imgEle = body.getElementsByTag("img");
        String src = imgEle.attr("src");
        if (src != null) {
            yData.imgUrl = src; //图片链接设置
        }

        List<GankData> gankDatas = new ArrayList<>();

        //获取分类(Ios,Android等)
        Elements typeList = body.getElementsByTag("h3");
//        for (Element element : typeList) {
//            String text = element.text();
//            KLog.w(TAG, text);
//        }

        ArrayList<String> typeStringList = new ArrayList<>();
        /**
         * <h3>标签和<ul>标签数量可能不同(实测有这种)
         * */
        for (int i = 0; i < typeList.size(); i++) {
            String type = typeList.get(i).text().trim(); //类型
            if (type == null || type.trim().equals("")) {
                continue;
            }
            typeStringList.add(type);
        }


        Elements ulEles = body.getElementsByTag("ul");
        int size = typeStringList.size() > ulEles.size() ? ulEles.size() : typeStringList.size();

        //实测当天图片,当天视频信息数据没有固定格式;去除最后一个分类,单独进行处理
        for (int i = 0; i < typeStringList.size() - 1; i++) {
            String type = typeStringList.get(i);
            Element eleUL = body.getElementsByTag("ul").get(i);
            Elements lis = eleUL.getElementsByTag("li");
            for (Element element : lis) {
                Elements elea = element.getElementsByTag("a");
                String contentUrl = elea.attr("href");  //链接
                String desc = elea.text(); //描述信息
                String trim = element.text().trim();
                String who = "";
                if (trim.contains("(")) { //得到作者
                    who = trim.substring(trim.lastIndexOf("(") + 1, trim.length() - 2);
                }
                if (type.equals("休息视频")) {
                    yData.desc = desc;
                }

                if (type.equalsIgnoreCase("Android") || i == 0) {
                    spareDesc = desc;
                }

                //设置属性
                GankData gankData = new GankData();
                gankData.type = type;
                gankData.desc = desc;
                gankData.who = who;
                gankData.url = contentUrl;
                gankDatas.add(gankData);
//                KLog.w(TAG, "desc: " + desc + "  url: " + contentUrl);
            }
        }

        /***获取最后一条数据***/
        //获取所有带有href属性的a元素
        Elements selectEles = body.select("a[href]");
        //取最后一个
        Element element = selectEles.get(selectEles.size() - 1);
        //获取作者
        Element parent = element.parent();
        String text = parent.text().trim();
        String who = text.substring(text.lastIndexOf("(") + 1, text.length() - 2);
        //得到链接
        String endUrl = element.attr("href");

        String desc = element.text();
        String type = typeStringList.get(typeStringList.size() - 1);
        if (type.equals("休息视频")) {
            yData.desc = desc;
        }

        GankData gankData = new GankData();
        gankData.type = type;
        gankData.desc = desc;
        gankData.who = who;
        gankData.url = endUrl;
        gankDatas.add(gankData);

        /********************/

        if (yData.desc == null || yData.desc.trim().equals("")) {
            yData.desc = spareDesc;
        }
        yData.gankDataList = gankDatas;

    }

}
