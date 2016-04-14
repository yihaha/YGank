package com.ybh.lovemeizi.view.activity;



import com.bumptech.glide.Glide;
import com.ybh.lovemeizi.R;

import butterknife.Bind;
import uk.co.senab.photoview.PhotoView;

public class ShowPicActivity extends BaseActivity {
    @Bind(R.id.meizi_img)
    PhotoView mImg;

    @Override
    public int getContentViewId() {
        return R.layout.activity_show_pic;
    }

    @Override
    public void initView() { }

    @Override
    public void initData() {
        String imgUrl = getIntent().getStringExtra("imgUrl");
        Glide.with(ShowPicActivity.this).load(imgUrl).into(mImg);
    }
}
