package com.ybh.lovemeizi.module.home.ui;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ybh.lovemeizi.R;
import com.ybh.lovemeizi.utils.DateUtil;
import com.ybh.lovemeizi.utils.DialogUtil;
import com.ybh.lovemeizi.utils.ImgSaveUtil;
import com.ybh.lovemeizi.utils.ShareUtil;
import com.ybh.lovemeizi.utils.SlidrUtil;
import com.ybh.lovemeizi.module.BaseActivity;
import com.ybh.lovemeizi.utils.ToastSnackUtil;
import com.ybh.lovemeizi.widget.YDialog;
import com.ybh.lovemeizi.widget.slidr.model.SlidrInterface;

import java.io.File;
import java.util.Date;

import butterknife.Bind;
import cn.sharesdk.framework.Platform;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class ShowPicActivity extends BaseActivity {
    @Bind(R.id.show_meizi_img)
    ImageView mImg;

    @Bind(R.id.appbarlayout)
    AppBarLayout mAppbarLayout;


    public static final String MEIZI_IMG = "meiziImg";

    private String imgUrl;
    private String date;
    private String shTitle;

    @Override
    public int getContentViewId() {
        return R.layout.activity_show_pic;
    }

    /**
     *
     * @param context
     * @param url
     * @param etc 可以传递标题,时间等(qq空间可能有用),很可能不显示,无所谓;标题和时间一起传递过来
     * @return
     */
    public static Intent  newIntent(Context context,String url,String...etc ){
        String title="图片";
        String date=DateUtil.onDate2String(new Date());
        if (etc!=null&&etc.length==2){
            title=etc[0];
            date=etc[1];
        }
        Intent intent = new Intent(context,ShowPicActivity.class);
        intent.putExtra("imgUrl",url);
        intent.putExtra("title",title);
        intent.putExtra("date",date);
        return intent;
    }

    @Override
    public void initView() {
//        ViewCompat.setTransitionName(mImg,MEIZI_IMG);
        // 默认开启侧滑，默认是整个页码侧滑
        mSlidrInterface = SlidrUtil.initSlidrDefaultConfig(this, true);
    }

    /**
     * 控制滑动与否的接口
     */
    protected SlidrInterface mSlidrInterface;

    @Override
    public void initData() {

        Intent intent = getIntent();
        this.imgUrl = intent.getStringExtra("imgUrl");
        shTitle = intent.getStringExtra("title");
        this.date = intent.getStringExtra("date");

        setActivityTitle(this.date, true);
//        Glide.with(ShowPicActivity.this).load(imgUrl).into(mImg);
        Picasso.with(this)
                .load(this.imgUrl)
                .into(mImg);

        mImg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showShare();
                return true;
            }
        });
    }

    private void showShare(){
        ShareUtil.sdkShare(ShowPicActivity.this, imgUrl, shTitle, date,Platform.SHARE_IMAGE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Picasso.with(this).cancelRequest(mImg);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_picacitivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save:
                showSaveImgDialog();
                return true;
            case R.id.menu_share:
                showShare();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 保存图片的提示
     */
    private void showSaveImgDialog() {
        final YDialog yDialog = DialogUtil.getYDialog(ShowPicActivity.this, R.layout.ydialog_default);
        TextView dialogTitle = (TextView) yDialog.findViewById(R.id.ydialog_title);
        dialogTitle.setText("保存图片到本地?");
        yDialog.findViewById(R.id.ydialog_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Subscription subscribe = ImgSaveUtil.saveImg(ShowPicActivity.this, imgUrl, "ygank")
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Uri>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                Snackbar.make(mImg, "异常: " + e.getMessage() + "\n请再次尝试", Snackbar.LENGTH_LONG).setAction("知道了", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                    }
                                }).show();
                            }

                            @Override
                            public void onNext(Uri uri) {
                                File ygankDir = new File(Environment.getExternalStorageDirectory(), "ygank");
                                ToastSnackUtil.snackbarShort(mImg, String.format("图片已保存到%s文件夹下", ygankDir.getAbsolutePath()));
                            }
                        });
                DialogUtil.dismiss();
                addSubscription(subscribe);
            }
        });
    }

}
