package ru.gdo.android.library.materialdesign.widget;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Checkable;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 29.10.15.
 */

public class MDTouchCheckBox extends View implements Checkable {

    private Paint mPaint;
    private Paint mBorderPaint;
    private Paint mFlagPaint;
    private Path mPath = new Path();
    private int mWidth, mHeight;            // Control width and mHeight
    private int mCx, mCy;                  // x, y coordinates of the center
    private float[] mFlagPoints = new float[6]; // Coordinate checkmark three mFlagPoints
    private boolean mChecked = true;
    private boolean mInAnimation;

    private int mAnimDuration = 1000;
    private float mAnimationProgress = 1;

    private OnCheckedChangeListener mCheckedChangeListener;
    private int mCheckedColor = Color.GREEN;
    private int mCheckedBorderColor = Color.RED;
    private int mUnCheckedColor = Color.BLUE;
    private int mUnCheckedBorderColor = Color.YELLOW;
    private ValueAnimator mAnimator;
    private boolean mBroadCasting = false;
    private float mRadius;

    public MDTouchCheckBox(Context context) {
        this(context, null);
    }

    public MDTouchCheckBox(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MDTouchCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MDTouchCheckBox(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    /**
     * Initialization
     *
     * @param context Context
     */
    private void init(Context context) {

        this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.mPaint.setStyle(Paint.Style.FILL);

        this.mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.mBorderPaint.setStyle(Paint.Style.STROKE);
        this.mBorderPaint.setStrokeWidth(MDTools.dip2px(context, 3));

        this.mFlagPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.mFlagPaint.setColor(Color.WHITE);
        this.mFlagPaint.setStyle(Paint.Style.STROKE);
        this.mFlagPaint.setStrokeWidth(MDTools.dip2px(context, 5));

        this.mAnimator = ValueAnimator.ofFloat(0, 1).
                setDuration(this.mAnimDuration);
        this.mAnimator.setInterpolator(new LinearInterpolator());

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
//        this.mHeight = this.mWidth = Math.min(
//                w - getPaddingLeft() - getPaddingRight(),
//                h - getPaddingBottom() - getPaddingTop());

        this.mHeight = h - getPaddingBottom() - getPaddingTop();

        this.mWidth = w - getPaddingLeft() - getPaddingRight();

        this.mCx = this.mWidth / 2 + getPaddingLeft();
        this.mCy = this.mHeight / 2 + getPaddingTop();

        if (this.mHeight < this.mWidth) {
            this.mRadius = this.mHeight / 2.0f;
        } else {
            this.mRadius = this.mWidth / 2.0f;
        }

        this.mFlagPoints[0] = this.mCx - this.mRadius * 0.58f;
        this.mFlagPoints[1] = this.mCy - this.mRadius * 0.10f;

        this.mFlagPoints[2] = this.mCx;
        this.mFlagPoints[3] = this.mCy + this.mRadius / 2.0f;

        this.mFlagPoints[4] = this.mCx + this.mRadius * 0.70f;
        this.mFlagPoints[5] = this.mCy - this.mRadius * 0.45f;

    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {

        this.mPaint.setColor(MDTools.evaluateColor(this.mAnimationProgress, this.mUnCheckedColor, this.mCheckedColor));
        this.mBorderPaint.setColor(MDTools.evaluateColor(this.mAnimationProgress, this.mUnCheckedBorderColor, this.mCheckedBorderColor));

        canvas.drawCircle(mCx, mCy, this.mRadius, mPaint); // Circle
        canvas.drawCircle(mCx, mCy, this.mRadius, mBorderPaint); // Circle border
//        canvas.drawRoundRect(
//                new RectF(
//                        this.mCx - (this.mWidth * 0.5f),
//                        this.mCy - (this.mHeight * 0.5f),
//                        this.mCx + (this.mWidth * 0.5f),
//                        this.mCy + (this.mHeight * 0.5f)
//                ),
//                25,
//                25,
//                this.mPaint);
//
        if (this.mAnimationProgress > 0) {
            float x, y;
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
            canvas.drawPath(this.mPath, this.mFlagPaint);
        }
    }

    private void showAsChecked() {
        if (this.mInAnimation) {
            return;
        }
        this.mInAnimation = true;
        this.mAnimator.setFloatValues(0, 1);
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
    public boolean isChecked() {
        return this.mChecked;
    }

    /**
     * Set the color when selected or not selected
     *
     * @param color color
     */
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
    public void setFlagColor(int color) {
        this.mFlagPaint.setColor(color);
        invalidate();
    }

    public int getAnimDuration() {
        return this.mAnimDuration;
    }

    public void setAnimDuration(int animDuration) {
        this.mAnimDuration = animDuration;
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.mCheckedChangeListener = listener;
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(MDTouchCheckBox checkBox, boolean isChecked);
    }

}