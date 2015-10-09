package ru.gdo.android.library.materialdesign.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import ru.gdo.android.library.materialdesign.interfaces.OnToggleListener;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 09.10.15.
 */

public class MDShadowToggle extends MDShadow {

    public MDShadowToggle(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void initView(Context context, AttributeSet attrs) {
        super.initView(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                this.setPressed(false);
                break;
            case MotionEvent.ACTION_DOWN:
                this.setPressed(true);
                break;
        }

        return super.onTouch(v, event);
    }

    @SuppressWarnings("unused")
    public CharSequence getText(int buttonIndex) {
        return (this.mChildView != null) ? ((MDToggle) this.mChildView).getText() : null;
    }

    @SuppressWarnings("unused")
    public CharSequence getText() {
        return (this.mChildView != null) ? ((MDToggle) this.mChildView).getText() : null;
    }

    @SuppressWarnings("unused")
    public void setChecked(boolean checked) {
        if (this.mChildView != null) {
            ((MDToggle) this.mChildView).setChecked(checked);
        }
    }

    @SuppressWarnings("unused")
    public void setChecked(boolean checked, int duration) {
        if (this.mChildView != null) {
            ((MDToggle) this.mChildView).setChecked(checked, duration);
        }
    }

    @SuppressWarnings("unused")
    public boolean isChecked() {
        return this.mChildView != null && ((MDToggle) this.mChildView).isChecked();
    }

    public void setOnToggleListener(OnToggleListener onToggleListener) {
        if (this.mChildView != null) {
            ((MDToggle) this.mChildView).setOnToggleListener(onToggleListener);
        }
    }

    @Override
    public View getChildView(Context context, AttributeSet attrs) {
        return new MDToggle(context, attrs);
    }

}