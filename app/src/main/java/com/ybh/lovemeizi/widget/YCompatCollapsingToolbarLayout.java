package com.ybh.lovemeizi.widget;

import android.content.Context;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.util.AttributeSet;

import java.lang.reflect.Field;

/**
 * Created by y on 2016/4/15.
 */
public class YCompatCollapsingToolbarLayout extends CollapsingToolbarLayout {

    private boolean mLayoutRead;

    public YCompatCollapsingToolbarLayout(Context context) {
        this(context,null);
    }

    public YCompatCollapsingToolbarLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public YCompatCollapsingToolbarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (!mLayoutRead) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {//版本大于20
                if ((getWindowSystemUiVisibility()
                        & (SYSTEM_UI_FLAG_LAYOUT_STABLE | SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN))
                        == (SYSTEM_UI_FLAG_LAYOUT_STABLE | SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
                        ) {
                    try {
                        Field mLastInsets = CollapsingToolbarLayout.class.getDeclaredField("mLastInsets");
                        mLastInsets.setAccessible(true);
                        mLastInsets.set(this, null);
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            mLayoutRead = true;
        }
        super.onLayout(changed, left, top, right, bottom);
    }
}
