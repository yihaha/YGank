package com.ybh.lovemeizi.widget;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by y on 2016/4/15.
 */
public class YCompatToolbar extends Toolbar {

    private boolean mLayoutReady;

    public YCompatToolbar(Context context) {
//        super(context);
        this(context, null);
    }

    public YCompatToolbar(Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
        this(context, attrs, android.support.v7.appcompat.R.attr.toolbarStyle);
    }

    public YCompatToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (!mLayoutReady) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { //版本大于等于19
                if ((getWindowSystemUiVisibility()
                        & (SYSTEM_UI_FLAG_LAYOUT_STABLE | SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN))
                        == (SYSTEM_UI_FLAG_LAYOUT_STABLE | SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)) {
                    int statusBarHeight = getStatusBarHeight();
                    ViewGroup.LayoutParams layoutParams = getLayoutParams();
                    layoutParams.height = statusBarHeight + getHeight();
                    setPadding(0, statusBarHeight, 0, 0);
                }
            }
            mLayoutReady = true;
        }
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public int getStatusBarHeight() {
        Resources resources = Resources.getSystem();
        int identifier = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (identifier > 0) {
            return resources.getDimensionPixelSize(identifier);
        }
        return 0;
    }
}
