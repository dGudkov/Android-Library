package ru.gdo.android.library.materialdesign.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import ru.gdo.android.library.materialdesign.R;
import ru.gdo.android.library.materialdesign.interfaces.OnToggleListener;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 07.10.15.
 */

public class MDToggle extends FrameLayout implements
        View.OnClickListener, View.OnLongClickListener {

    private static final String ANDROID_NAMESPACE = "http://schemas.android.com/apk/res/android";
    private static final String ATTRIBUTE_DURATION = "duration";
    private static final String ATTRIBUTE_CHECKED  = "checked";

    public static final int SWITCH_ON = 0;
    public static final int SWITCH_OFF = 1;

    private static final int DEFAULT_SLIDER_BACKGROUND_ID = R.drawable.md_default_selector_linear_gradient;
    private static final int DEFAULT_BUTTON_BACKGROUND_ID = R.drawable.md_default_selector_linear_gradient;
    private static final int DEFAULT_SLIDER_ON_TEXT_ID = R.string.str_sliding_touch_switch_on;
    private static final int DEFAULT_SLIDER_OFF_TEXT_ID = R.string.str_sliding_touch_switch_off;
    private static final int DEFAULT_TEXT_COLOR_ID = Color.BLACK;
    private static final int DEFAULT_DURATION = 100;
    private static final boolean DEFAULT_CHECKED = true;

    private Button mBtnMovable;
    private Button mBtnOn;
    private Button mBtnOff;
    private boolean mInMotion = false;
    private boolean mLayoutChanged = true;

    private int mDuration;

    private OnToggleListener mOnToggleListener;

    private int mPosition = SWITCH_ON;
    private int[] mMovableButtonBackgroundId = new int[2];
    private Drawable[] movableButtonBackground = new Drawable[2];
    private ObjectAnimator mAnimator;
    private float mFraction;

    public MDToggle(Context context) {
        this(context, null);
    }

    public MDToggle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MDToggle(Context context, AttributeSet attrs,
                    int defStyle) {
        super(context, attrs, defStyle);
        initView(context, attrs);
    }

    @SuppressWarnings("unused")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MDToggle(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    @SuppressWarnings("deprecation")
    private void initView(Context context, AttributeSet attrs) {
        this.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.md_toggle_layout, this, true);

        this.mBtnMovable = (Button) v.findViewById(R.id.movable_button);
        this.mBtnOn = (Button) v.findViewById(R.id.button_left);
        this.mBtnOff = (Button) v.findViewById(R.id.button_right);

        this.mBtnOn.setOnClickListener(this);
        this.mBtnOn.setOnLongClickListener(this);
        this.mBtnOff.setOnClickListener(this);
        this.mBtnOff.setOnLongClickListener(this);

        this.mDuration = attrs.getAttributeIntValue(ANDROID_NAMESPACE, ATTRIBUTE_DURATION, DEFAULT_DURATION);
        this.mPosition  = attrs.getAttributeBooleanValue(ANDROID_NAMESPACE, ATTRIBUTE_CHECKED, DEFAULT_CHECKED) ?
                SWITCH_ON : SWITCH_OFF;

        TypedArray attr = context.obtainStyledAttributes(attrs,
                R.styleable.ToggleSwitch, 0, 0);

        try {
            this.mBtnOn.setTextColor(
                    attr.getColor(
                            R.styleable.ToggleSwitch_onTextColor,
                            DEFAULT_TEXT_COLOR_ID
                    )
            );

            this.mBtnOff.setTextColor(
                    attr.getColor(
                            R.styleable.ToggleSwitch_offTextColor,
                            DEFAULT_TEXT_COLOR_ID
                    )
            );

            this.mBtnOn.setText(
                    attr.getResourceId(
                            R.styleable.ToggleSwitch_onText,
                            DEFAULT_SLIDER_ON_TEXT_ID
                    )
            );
            this.mBtnOff.setText(
                    attr.getResourceId(
                            R.styleable.ToggleSwitch_offText,
                            DEFAULT_SLIDER_OFF_TEXT_ID
                    )
            );

            this.setBackgroundDrawable(ContextCompat.getDrawable(getContext(),
                            attr.getResourceId(
                                    R.styleable.ToggleSwitch_sliderBackground, DEFAULT_SLIDER_BACKGROUND_ID
                            )
                    )
            );

            this.mMovableButtonBackgroundId[SWITCH_ON] = attr
                    .getResourceId(R.styleable.ToggleSwitch_onBackground, DEFAULT_BUTTON_BACKGROUND_ID);
            this.mMovableButtonBackgroundId[SWITCH_OFF] = attr
                    .getResourceId(R.styleable.ToggleSwitch_offBackground, DEFAULT_BUTTON_BACKGROUND_ID);

        } finally {
            attr.recycle();
        }

        for (int i = 0; i < this.mMovableButtonBackgroundId.length; i++) {
            this.movableButtonBackground[i] = ContextCompat.getDrawable(getContext(), this.mMovableButtonBackgroundId[i]);
        }

        this.mBtnMovable.setBackgroundDrawable(this.movableButtonBackground[this.mPosition]);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (this.mLayoutChanged) {
            this.mLayoutChanged = false;
            if (this.mPosition == SWITCH_ON) {
                animateButton(SWITCH_ON, this.mBtnOn, 0);
            } else {
                animateButton(SWITCH_OFF, this.mBtnOff, 0);
            }
        }

    }

    @Override
    public boolean onLongClick(View v) {
        handleClick(v);
        return true;
    }

    @Override
    public void onClick(View v) {
        handleClick(v);
    }

    private void handleClick(View v) {
        if (this.mInMotion) {
            if (this.mAnimator != null) {
                this.mAnimator.cancel();
            }
        } else {
            if (this.mPosition == SWITCH_OFF) {
                animateButton(SWITCH_ON, this.mBtnOn, this.mDuration);
            } else {
                animateButton(SWITCH_OFF, this.mBtnOff, this.mDuration);
            }
        }
    }

    @SuppressWarnings("deprecation")
    private void animateButton(final int position, final Button button, final int duration) {
        final float startPosition = this.mBtnMovable.getX();

        this.mAnimator = ObjectAnimator.ofFloat(this.mBtnMovable, "x", button.getX());
        this.mAnimator.addListener(new SimpleAnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                MDToggle.this.mInMotion = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                MDToggle.this.mInMotion = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                if (MDToggle.this.mFraction <= 0.5) {
                    MDToggle.this.mBtnMovable.setX(startPosition);
                } else {
                    MDToggle.this.mBtnMovable.setX(button.getX());
                }
                if (MDToggle.this.mOnToggleListener != null) {
                    MDToggle.this.mOnToggleListener.onToggle(MDToggle.this.mPosition);
                }
            }
        });
        this.mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                MDToggle.this.mFraction = animation.getAnimatedFraction();
                if (MDToggle.this.mPosition != position) {
                    if (MDToggle.this.mFraction > 0.5) {
                        MDToggle.this.mPosition = position;
                        MDToggle.this.mBtnMovable.setBackgroundDrawable(
                                MDToggle.this.movableButtonBackground[position]
                        );
                        if (MDToggle.this.mOnToggleListener != null) {
                            MDToggle.this.mOnToggleListener.onToggle(MDToggle.this.mPosition);
                        }
                    }
                }
            }
        });
        this.mAnimator.setDuration(duration);
        this.mAnimator.start();
    }

    public void setOnToggleListener(OnToggleListener onToggleListener) {
        this.mOnToggleListener = onToggleListener;
    }

    private static class SimpleAnimatorListener implements Animator.AnimatorListener {

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {

        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }

    @SuppressWarnings("unused")
    public void setDuration(int duration) {
        this.mDuration = duration;
    }

    @SuppressWarnings("unused")
    public void setTextSize(float size) {
        this.mBtnOn.setTextSize(size);
        this.mBtnOff.setTextSize(size);
    }

    @SuppressWarnings("unused")
    public void setTextSize(int unit, float size) {
        this.mBtnOn.setTextSize(unit, size);
        this.mBtnOff.setTextSize(unit, size);
    }

    @SuppressWarnings("unused")
    public final void setText(int buttonIndex, CharSequence text) {
        switch (buttonIndex) {
            case SWITCH_ON:
                this.mBtnOn.setText(text);
                break;
            case SWITCH_OFF:
                this.mBtnOff.setText(text);
                break;
        }
    }

    @SuppressWarnings("unused")
    public void setText(int buttonIndex, CharSequence text, TextView.BufferType type) {
        switch (buttonIndex) {
            case SWITCH_ON:
                this.mBtnOn.setText(text, type);
                break;
            case SWITCH_OFF:
                this.mBtnOff.setText(text, type);
                break;
        }
    }

    @SuppressWarnings("unused")
    public final void setText(int buttonIndex, char[] text, int start, int len) {
        switch (buttonIndex) {
            case SWITCH_ON:
                this.mBtnOn.setText(text, start, len);
                break;
            case SWITCH_OFF:
                this.mBtnOff.setText(text, start, len);
                break;
        }
    }

    @SuppressWarnings("unused")
    public final void setText(int buttonIndex, @StringRes int resId) {
        switch (buttonIndex) {
            case SWITCH_ON:
                this.mBtnOn.setText(resId);
                break;
            case SWITCH_OFF:
                this.mBtnOff.setText(resId);
                break;
        }
    }

    @SuppressWarnings("unused")
    public final void setText(int buttonIndex, @StringRes int resId, TextView.BufferType type) {
        switch (buttonIndex) {
            case SWITCH_ON:
                this.mBtnOn.setText(resId, type);
                break;
            case SWITCH_OFF:
                this.mBtnOff.setText(resId, type);
                break;
        }
    }

    @SuppressWarnings("unused")
    public CharSequence getText(int buttonIndex) {
        switch (buttonIndex) {
            case SWITCH_ON:
                return this.mBtnOn.getText();
            case SWITCH_OFF:
                this.mBtnOff.getText();
        }
        return null;
    }

    @SuppressWarnings("unused")
    public CharSequence getText() {
        if (this.mPosition == SWITCH_ON) {
            return this.mBtnOn.getText();
        }
        return this.mBtnOff.getText();
    }

    @SuppressWarnings("unused")
    @Override
    public void setEnabled(boolean enabled) {
        if (!enabled) {
            if (this.mAnimator != null) {
                this.mAnimator.cancel();
                this.mAnimator = null;
            }
        }
        super.setEnabled(enabled);
        this.mBtnOn.setEnabled(enabled);
        this.mBtnOff.setEnabled(enabled);
        this.mBtnMovable.setEnabled(enabled);
    }

    @SuppressWarnings("unused")
    public void setChecked(boolean checked) {
        setChecked(checked, 0);
    }

    @SuppressWarnings("unused")
    public void setChecked(boolean checked, int duration) {
        if (isEnabled()) {
            if (this.mInMotion) {
                if (this.mAnimator != null) {
                    this.mAnimator.cancel();
                }
            } else {
                if (checked) {
                    animateButton(SWITCH_ON, this.mBtnOn, duration);
                } else {
                    animateButton(SWITCH_OFF, this.mBtnOff, duration);
                }
            }
        } else {
            if (MDToggle.this.mOnToggleListener != null) {
                MDToggle.this.mOnToggleListener.onToggle(MDToggle.this.mPosition);
            }
        }
    }

    @SuppressWarnings("unused")
    public boolean isChecked() {
        return (this.mPosition == SWITCH_ON);
    }

    @Override
    protected void dispatchSetPressed(boolean pressed) {
        super.dispatchSetPressed(pressed);
    }

    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
        this.mBtnMovable.setPressed(pressed);
    }

}