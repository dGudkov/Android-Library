package ru.gdo.android.library.materialdesign.widget;

import android.content.Context;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.gdo.android.library.materialdesign.interfaces.OnToggleListener;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 09.10.15.
 */

public class MDShadowToggle extends MDShadow implements View.OnLongClickListener {

    private MDToggle mToggleButton;
    private GestureDetector mGestureDetector;
    private boolean mHasPerformedLongPress;

    public MDShadowToggle(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void initView(Context context, AttributeSet attrs) {
        this.mToggleButton = new MDToggle(context, attrs);
        super.initView(context, attrs);

        GestureDetector.SimpleOnGestureListener mLongClickListener = new GestureDetector.SimpleOnGestureListener() {
            public void onLongPress(MotionEvent e) {
                mHasPerformedLongPress = MDShadowToggle.this.mToggleButton.performClick();
            }

            @Override
            public boolean onDown(MotionEvent e) {
                MDShadowToggle.this.mHasPerformedLongPress = false;
                return super.onDown(e);
            }
        };

        this.mGestureDetector = new GestureDetector(context, mLongClickListener);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.getChildCount() > 0) {
            this.removeAllViews();
        }

        this.addView(
                this.mToggleButton,
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        boolean gestureResult = this.mGestureDetector.onTouchEvent(event);
        if (gestureResult || this.mHasPerformedLongPress) {
            return true;
        } else {
            int action = event.getActionMasked();
            switch (action) {
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    this.setPressed(false);
                    this.mToggleButton.setPressed(false);
                    break;
                case MotionEvent.ACTION_DOWN:
                    this.setPressed(true);
                    this.mToggleButton.setPressed(true);
                    break;
            }
        }

        return super.onTouch(v, event);
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        return super.onTouchEvent(event);
//    }

    @SuppressWarnings("unused")
    public void setTextSize(int unit, float size) {
        if (this.mToggleButton != null) {
            this.mToggleButton.setTextSize(unit, size);
        }
    }

    @SuppressWarnings("unused")
    public void setTextSize(float size) {
        if (this.mToggleButton != null) {
            this.mToggleButton.setTextSize(size);
        }
    }

    @SuppressWarnings("unused")
    public final void setText(int buttonIndex, CharSequence text) {
        if (this.mToggleButton != null) {
            this.mToggleButton.setText(buttonIndex, text);
        }
    }

    @SuppressWarnings("unused")
    public void setText(int buttonIndex, CharSequence text, TextView.BufferType type) {
        if (this.mToggleButton != null) {
            this.mToggleButton.setText(buttonIndex, text, type);
        }
    }

    @SuppressWarnings("unused")
    public final void setText(int buttonIndex, char[] text, int start, int len) {
        if (this.mToggleButton != null) {
            this.mToggleButton.setText(buttonIndex, text, start, len);
        }
    }

    @SuppressWarnings("unused")
    public final void setText(int buttonIndex, @StringRes int resId) {
        if (this.mToggleButton != null) {
            this.mToggleButton.setText(buttonIndex, resId);
        }
    }

    @SuppressWarnings("unused")
    public final void setText(int buttonIndex, @StringRes int resId, TextView.BufferType type) {
        if (this.mToggleButton != null) {
            this.mToggleButton.setText(buttonIndex, resId, type);
        }
    }

    @SuppressWarnings("unused")
    public CharSequence getText(int buttonIndex) {
        return this.mToggleButton.getText();
    }

    @SuppressWarnings("unused")
    public CharSequence getText() {
        return this.mToggleButton.getText();
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (this.mToggleButton != null) {
            this.mToggleButton.setEnabled(enabled);
        }
    }

    @SuppressWarnings("unused")
    @Override
    public boolean isViewEnabled() {
        return (isEnabled() && this.mToggleButton.isEnabled());
    }

    @SuppressWarnings("unused")
    public void setChecked(boolean checked) {
        this.mToggleButton.setChecked(checked);
    }


    @SuppressWarnings("unused")
    public void setChecked(boolean checked, int duration) {
        this.mToggleButton.setChecked(checked, duration);
    }

    @SuppressWarnings("unused")
    public boolean isChecked() {
        return this.mToggleButton.isChecked();
    }

    public void setOnToggleListener(OnToggleListener onToggleListener) {
        this.mToggleButton.setOnToggleListener(onToggleListener);
    }

    @Override
    public boolean onLongClick(View v) {
        return true;
    }
}