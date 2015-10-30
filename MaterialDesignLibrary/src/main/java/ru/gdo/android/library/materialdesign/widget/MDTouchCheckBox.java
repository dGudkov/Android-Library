package ru.gdo.android.library.materialdesign.widget;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Checkable;

import ru.gdo.android.library.materialdesign.R;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 29.10.15.
 */

public class MDTouchCheckBox extends View implements Checkable {

    private static final Mode DEFAULT_MODE = Mode.rect;
    private static final boolean DEFAULT_CHECKED = true;
    private static final int DEFAULT_DURATION = 100;
    private static final int DEFAULT_FLAG_WIDTH = 2;
    private static final int DEFAULT_FLAG_COLOR = Color.BLACK;
    private static final int DEFAULT_CHECKED_COLOR = Color.WHITE;
    private static final int DEFAULT_UNCHECKED_COLOR = Color.WHITE;
    private static final int DEFAULT_CHECKED_BORDER_COLOR = Color.BLACK;
    private static final int DEFAULT_UNCHECKED_BORDER_COLOR = Color.BLACK;
    private static final int DEFAULT_DISABLED_BORDER_COLOR = Color.DKGRAY;
    private static final int DEFAULT_DISABLED_COLOR = Color.GRAY;
    private static final int DEFAULT_BORDER_WIDTH = 2;

    private Paint mPaint;
    private RectF mPaintRect;
    private Path mPath = new Path();
    private int mCx, mCy;                  // x, y coordinates of the center
    private float[] mFlagPoints = new float[6]; // Coordinate checkmark three mFlagPoints
    private boolean mInAnimation;
    private float mAnimationProgress = 1;
    private ValueAnimator mAnimator;
    private OnCheckedChangeListener mCheckedChangeListener;
    private boolean mBroadCasting = false;
    private float mRadius;

    private Mode mMode;
    private boolean mChecked;
    private int mDuration;
    private int mFlagWidth;
    private int mFlagColor;
    private int mBorderWidth;
    private int mCheckedColor;
    private int mUnCheckedColor;
    private int mCheckedBorderColor;
    private int mUnCheckedBorderColor;
    private int mDisabledColor;
    private int mDisabledBorderColor;

    public MDTouchCheckBox(Context context) {
        this(context, null);
    }

    public MDTouchCheckBox(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MDTouchCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MDTouchCheckBox(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    /**
     * Initialization
     */
    private void initView(Context context, AttributeSet attrs) {

        this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        this.mAnimator = ValueAnimator.ofFloat(0, 1);
        this.mAnimator.setInterpolator(new LinearInterpolator());

        this.mChecked = MDTools.getCheckedAttributeValue(attrs, DEFAULT_CHECKED);
        this.mDuration = MDTools.getDurationAttributeValue(attrs, DEFAULT_DURATION);

        this.mAnimationProgress = this.mChecked ? 1 : 0;

        TypedArray attr = context.obtainStyledAttributes(attrs,
                R.styleable.MDTouchCheckBox, 0, 0);

        try {
            this.mMode = Mode.fromId(attr.getInt(R.styleable.MDTouchCheckBox_tcb_mode, DEFAULT_MODE.value));
            this.mFlagWidth = MDTools.dip2px(
                    context,
                    attr.getDimension(
                            R.styleable.MDTouchCheckBox_tcb_flagwidth,
                            DEFAULT_FLAG_WIDTH)
            );

            this.mBorderWidth = MDTools.dip2px(
                    context,
                    attr.getDimension(
                            R.styleable.MDTouchCheckBox_tcb_borderwidth,
                            DEFAULT_BORDER_WIDTH)
            );

            this.mFlagColor = attr.getColor(
                    R.styleable.MDTouchCheckBox_tcb_flagcolor,
                    DEFAULT_FLAG_COLOR
            );

            this.mCheckedColor = attr.getColor(
                    R.styleable.MDTouchCheckBox_tcb_checkedcolor,
                    DEFAULT_CHECKED_COLOR
            );

            this.mUnCheckedColor = attr.getColor(
                    R.styleable.MDTouchCheckBox_tcb_uncheckedcolor,
                    DEFAULT_UNCHECKED_COLOR
            );

            this.mCheckedBorderColor = attr.getColor(
                    R.styleable.MDTouchCheckBox_tcb_checkedbordercolor,
                    DEFAULT_CHECKED_BORDER_COLOR
            );

            this.mUnCheckedBorderColor = attr.getColor(
                    R.styleable.MDTouchCheckBox_tcb_uncheckedbordercolor,
                    DEFAULT_UNCHECKED_BORDER_COLOR
            );

            this.mDisabledBorderColor = attr.getColor(
                    R.styleable.MDTouchCheckBox_tcb_disabledbordercolor,
                    DEFAULT_DISABLED_BORDER_COLOR
            );

            this.mDisabledColor = attr.getColor(
                    R.styleable.MDTouchCheckBox_tcb_disabledcolor,
                    DEFAULT_DISABLED_COLOR
            );

        } finally {
            attr.recycle();
        }

        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        toggle();
                        break;
                }
                return true;
            }
        });

    }

    public void toggle() {
        setChecked(!this.mChecked);
    }

    /**
     * Sized coordinates
     *
     * @param w    - width
     * @param h    - height
     * @param oldw - old width
     * @param oldh - old height
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int height = h - getPaddingBottom() - getPaddingTop();
        int width = w - getPaddingLeft() - getPaddingRight();

        this.mCx = width / 2 + getPaddingLeft();
        this.mCy = height / 2 + getPaddingTop();

        if (height < width) {
            this.mRadius = height / 2.0f;
        } else {
            this.mRadius = width / 2.0f;
        }

        this.mFlagPoints[0] = this.mCx - this.mRadius * 0.58f;
        this.mFlagPoints[1] = this.mCy - this.mRadius * 0.10f;

        this.mFlagPoints[2] = this.mCx;
        this.mFlagPoints[3] = this.mCy + this.mRadius / 2.0f;

        this.mFlagPoints[4] = this.mCx + this.mRadius * 0.70f;
        this.mFlagPoints[5] = this.mCy - this.mRadius * 0.45f;

        float left = this.mCx - (width * 0.5f);
        float top = this.mCy - (height * 0.5f);

        this.mPaintRect = new RectF(
                left,
                top,
                left + width,
                top + height
        );

    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {

        if (this.mMode == Mode.circle) {
            setDrawPaint(this.mAnimationProgress);
            canvas.drawCircle(mCx, mCy, this.mRadius, mPaint); // Circle

            setDrawBorderPaint(this.mAnimationProgress);
            canvas.drawCircle(mCx, mCy, this.mRadius, mPaint); // Circle border
        } else {
            setDrawPaint(this.mAnimationProgress);
            canvas.drawRoundRect(this.mPaintRect, 25, 25, this.mPaint);

            setDrawBorderPaint(this.mAnimationProgress);
            canvas.drawRoundRect(this.mPaintRect, 25, 25, this.mPaint);
        }

        if (this.mAnimationProgress > 0) {
            float x, y;
            setDrawFlagPaint();
            this.mPath.reset();
            this.mPath.moveTo(this.mFlagPoints[0], this.mFlagPoints[1]);
            if (this.mAnimationProgress < 0.33f) {
                x = this.mFlagPoints[0] + (this.mFlagPoints[2] - this.mFlagPoints[0]) * this.mAnimationProgress * 3.33f;
                y = this.mFlagPoints[1] + (this.mFlagPoints[3] - this.mFlagPoints[1]) * this.mAnimationProgress * 3.33f;
            } else {
                x = this.mFlagPoints[2] + (this.mFlagPoints[4] - this.mFlagPoints[2]) * (this.mAnimationProgress - 0.33f) * 1.5f;
                y = this.mFlagPoints[3] + (this.mFlagPoints[5] - this.mFlagPoints[3]) * (this.mAnimationProgress - 0.33f) * 1.5f;
                this.mPath.lineTo(this.mFlagPoints[2], this.mFlagPoints[3]);
            }
            this.mPath.lineTo(x, y);
            canvas.drawPath(this.mPath, this.mPaint);
        }
    }

    public void setDrawPaint(float animationProgress) {
        this.mPaint.setStyle(Paint.Style.FILL);
        if (isEnabled()) {
            this.mPaint.setColor(MDTools.evaluateColor(animationProgress, this.mUnCheckedColor, this.mCheckedColor));
        } else {
            this.mPaint.setColor(this.mDisabledColor);
        }
    }

    public void setDrawBorderPaint(float animationProgress) {
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeWidth(this.mBorderWidth);
        if (isEnabled()) {
            this.mPaint.setColor(MDTools.evaluateColor(animationProgress, this.mUnCheckedBorderColor, this.mCheckedBorderColor));
        } else {
            this.mPaint.setColor(this.mDisabledBorderColor);
        }
    }

    public void setDrawFlagPaint() {
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeWidth(this.mFlagWidth);
        if (isEnabled()) {
            this.mPaint.setColor(this.mFlagColor);
        } else {
            this.mPaint.setColor(DEFAULT_FLAG_COLOR);
        }

    }

    private void showAsChecked() {
        if (this.mInAnimation) {
            return;
        }
        this.mInAnimation = true;
        this.mAnimator.setFloatValues(0, 1);
        this.mAnimator.setDuration(this.mDuration);
        this.mAnimator.start();
        this.mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue(); // 0f ~ 1f
                MDTouchCheckBox.this.mAnimationProgress = (MDTouchCheckBox.this.mChecked ? value : 1 - value);
                if (MDTouchCheckBox.this.mAnimationProgress < 0) { // Current progress
                    MDTouchCheckBox.this.mAnimationProgress = 0.0f;
                }
                invalidate();
                if (value >= 1) {
                    MDTouchCheckBox.this.mInAnimation = false;
                    if (!MDTouchCheckBox.this.mBroadCasting) {
                        if (MDTouchCheckBox.this.mCheckedChangeListener != null) {
                            MDTouchCheckBox.this.mBroadCasting = true;
                            MDTouchCheckBox.this.mCheckedChangeListener.onCheckedChanged(
                                    MDTouchCheckBox.this,
                                    MDTouchCheckBox.this.mChecked
                            );
                            MDTouchCheckBox.this.mBroadCasting = false;
                        }
                    }
                }
            }
        });
    }

    /**
     * Set the current selected
     *
     * @param checked boolean
     */
    @SuppressWarnings("unused")
    public void setChecked(boolean checked) {
        if (this.mChecked != checked) {
            this.mChecked = checked;
            showAsChecked();
        }
    }

    /**
     * Returns the current selected
     *
     * @return boolean
     */
    @SuppressWarnings("unused")
    public boolean isChecked() {
        return this.mChecked;
    }

    /**
     * Set the color when selected or not selected
     *
     * @param color color
     */
    @SuppressWarnings("unused")
    public void setColor(boolean checked, int color) {
        if (checked) {
            this.mCheckedColor = color;
        } else {
            this.mUnCheckedColor = color;
        }
        invalidate();
    }

    /**
     * Set the bordercolor when selected or not selected
     *
     * @param color border color
     */
    @SuppressWarnings("unused")
    public void setBorderColor(boolean checked, int color) {
        if (checked) {
            this.mCheckedBorderColor = color;
        } else {
            this.mUnCheckedBorderColor = color;
        }
        invalidate();
    }

    /**
     * Set flag color
     *
     * @param color color
     */
    @SuppressWarnings("unused")
    public void setFlagColor(int color) {
        this.mFlagColor = color;
        invalidate();
    }

    @SuppressWarnings("unused")
    public int getDuration() {
        return this.mDuration;
    }

    @SuppressWarnings("unused")
    public void setDuration(int duration) {
        this.mDuration = duration;
    }

    @SuppressWarnings("unused")
    public int getBorderWidth() {
        return MDTools.px2dip(getContext(), this.mBorderWidth);
    }

    @SuppressWarnings("unused")
    public void setBorderWidth(int borderWidth) {
        this.mBorderWidth = MDTools.dip2px(getContext(), borderWidth);
        invalidate();
    }

    @SuppressWarnings("unused")
    public int getFlagWidth() {
        return MDTools.px2dip(getContext(), this.mFlagWidth);
    }

    @SuppressWarnings("unused")
    public void setFlagWidth(int flagWidth) {
        this.mFlagWidth = MDTools.dip2px(getContext(), flagWidth);
        invalidate();
    }

    @SuppressWarnings("unused")
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.mCheckedChangeListener = listener;
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(MDTouchCheckBox checkBox, boolean isChecked);
    }

    private enum Mode {
        rect(0), circle(1);
        int value;

        Mode(int value) {
            this.value = value;
        }

        static Mode fromId(int value) {
            for (Mode f : values()) {
                if (f.value == value) return f;
            }
            throw new IllegalArgumentException();
        }
    }

}