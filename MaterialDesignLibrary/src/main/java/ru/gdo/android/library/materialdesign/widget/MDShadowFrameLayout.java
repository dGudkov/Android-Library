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

public class MDShadowFrameLayout extends MDShadow<FrameLayout> {

    public MDShadowFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initLayout() {
        ViewGroup vGroup = (ViewGroup) this.mChildView;
        vGroup.removeAllViews();
        while (this.getChildCount() > 0) {
            View child = this.getChildAt(0);
            this.removeView(child);
            vGroup.addView(child);
        }
        this.addView(
                this.mChildView,
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public FrameLayout getChildView(Context context, AttributeSet attrs) {
        return new FrameLayout(context, attrs);
    }

}
