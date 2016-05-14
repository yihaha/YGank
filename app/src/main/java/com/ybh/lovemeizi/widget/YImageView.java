package com.ybh.lovemeizi.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.socks.library.KLog;

import java.util.Random;

/**
 * Created by y on 2016/5/11.
 */
public class YImageView extends ImageView {
    public YImageView(Context context) {
        super(context);
    }

    public YImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public YImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        //取最小的值
//        int i = (width > height) ? (width = height) : (height = width);
        if (width>0){
            //为了让图片的高度不是统一高度()recycleView加载中效果不是很好)
//            height = (int) (Math.random() * (width/2) + width);
            height = (int) (width*1.5+0.5);
        }
//        KLog.w("YImage >>>  ",width);
        setMeasuredDimension(width,height);
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
