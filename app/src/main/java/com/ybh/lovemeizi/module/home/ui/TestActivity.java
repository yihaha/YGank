package com.ybh.lovemeizi.module.home.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ybh.lovemeizi.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoViewAttacher;

public class TestActivity extends AppCompatActivity {
    @Bind(R.id.test_img)
    ImageView mImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
//        PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(mImg);
        Glide.with(this).load("http://ww3.sinaimg.cn/large/005AsKDkjw1evvjm8gaazj31kw11x495.jpg")
                .into(mImg);
    }
}
