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

public class MDShadowRippleFrameLayout extends MDShadow {

    //    private LinearLayout mLayout;
    private MDRippleFrameLayout mRippleFrameLayout;


    public MDShadowRippleFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void initView(Context context, AttributeSet attrs) {
        super.initView(context, attrs);
        this.mRippleFrameLayout = new MDRippleFrameLayout(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        this.mRippleFrameLayout.removeAllViews();
        while (this.getChildCount() > 0) {
            View child = this.getChildAt(0);
            this.removeView(child);
            this.mRippleFrameLayout.addView(child);
        }
        this.addView(
                this.mRippleFrameLayout,
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public boolean isViewEnabled() {
        return (isEnabled() &&
                this.mRippleFrameLayout.isEnabled());
    }

}
