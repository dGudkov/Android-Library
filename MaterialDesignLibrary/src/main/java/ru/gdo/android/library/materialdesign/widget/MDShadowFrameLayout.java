package ru.gdo.android.library.materialdesign.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 05.10.15.
 */

public class MDShadowFrameLayout extends MDShadow {

//    private LinearLayout mLayout;
    private FrameLayout mFrameLayout;


    public MDShadowFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void initView(Context context, AttributeSet attrs) {
        super.initView(context, attrs);
        this.mFrameLayout = new FrameLayout(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        this.mFrameLayout.removeAllViews();
        while (this.getChildCount() > 0) {
            View child = this.getChildAt(0);
            this.removeView(child);
            this.mFrameLayout.addView(child);
        }
        this.addView(
                this.mFrameLayout,
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public boolean isViewEnabled() {
        return (isEnabled() &&
                this.mFrameLayout.isEnabled());
    }

}
