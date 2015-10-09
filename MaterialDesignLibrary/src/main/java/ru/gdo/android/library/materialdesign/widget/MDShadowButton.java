package ru.gdo.android.library.materialdesign.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 30.09.15.
 */

public class MDShadowButton extends MDShadow {

    private MDRippleButton mChildButton;

    public MDShadowButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void initView(Context context, AttributeSet attrs) {
        this.mChildButton = new MDRippleButton(context, attrs);
        super.initView(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.getChildCount() > 0) {
            this.removeAllViews();
        }

        this.addView(
                this.mChildButton,
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @SuppressWarnings("unused")
    public void setTextSize(int unit, float size) {
        if (this.mChildButton != null) {
            this.mChildButton.setTextSize(unit, size);
        }
    }

    @SuppressWarnings("unused")
    public void setTextSize(float size) {
        if (this.mChildButton != null) {
            this.mChildButton.setTextSize(size);
        }
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (this.mChildButton != null) {
            this.mChildButton.setEnabled(enabled);
        }
    }

    @Override
    public boolean isViewEnabled() {
        return (isEnabled() && this.mChildButton.isEnabled());
    }

}