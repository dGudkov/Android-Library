package ru.gdo.android.library.materialdesign.widget;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 29.10.15.
 */

public class MDTouchCheckBox extends View {

    private Paint mCirclePaint;
    private Paint mFlagPaint;
    private int mRadius;                    // Radius of the circle
    private int width, height;             // Control width and height
    private int cx, cy;                    // x, y coordinates of the center
    private float[] points = new float[6]; // Coordinate checkmark three points
    private float flagProgress;
    private boolean mChecked;
    private boolean isAnim;
    private int mAnimDuration = 1000;

    private OnCheckedChangeListener listener;
    private int mUnCheckedColor = Color.GRAY;
    private int mCheckedColor = Color.BLUE;

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

        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(Color.RED);
        mCirclePaint.setStyle(Paint.Style.FILL);

        mFlagPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFlagPaint.setColor(Color.WHITE);
        mFlagPaint.setStyle(Paint.Style.FILL);
        mFlagPaint.setStrokeWidth(dip2px(context, 2));

        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        if (mChecked) {
                            showFlag(false);
                        } else {
                            showAsChecked(true);
                        }
                        break;
                }
                return true;
            }
        });
    }

    /**
     * Set the current selected
     *
     * @param checked boolean
     */
    public void setChecked(boolean checked) {
        if (this.mChecked && !checked) {
            showFlag(false);
        } else if (!this.mChecked && checked) {
            showAsChecked(true);
        }
    }

    /**
     * Returns the current selected
     *
     * @return boolean
     */
    public boolean ismChecked() {
        return mChecked;
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
        height = width = Math.min(w - getPaddingLeft() - getPaddingRight(), h - getPaddingBottom() - getPaddingTop());
        cx = width / 2;
        cy = height / 2;

        float r = height / 2f;
        points[0] = r / 2f + getPaddingLeft();
        points[1] = r + getPaddingTop();

        points[2] = r * 5f / 6f + getPaddingLeft();
        points[3] = r + r / 3f + getPaddingTop();

        points[4] = r * 1.5f + getPaddingLeft();
        points[5] = r - r / 3f + getPaddingTop();
        mRadius = (int) (height * 0.125f);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {

        float f = (mRadius - height * 0.125f) / (height * 0.5f); // Current progress
        if (f < 0) {
            f = 0.0f;
        }
        Log.d("onDraw()", "f = " + f);
        mCirclePaint.setColor(evaluateColor(f, mUnCheckedColor, mCheckedColor));
//        canvas.drawCircle(cx, cy, mRadius, mCirclePaint); // Circle
        canvas.drawRoundRect(
                new RectF(cx - mRadius , cy - mRadius , cx + mRadius, cy + mRadius),
                25 * f,
                25 * f,
                mCirclePaint);

        if (flagProgress > 0) {
            if (flagProgress < 1 / 3f) {
                float x = points[0] + (points[2] - points[0]) * flagProgress;
                float y = points[1] + (points[3] - points[1]) * flagProgress;
                canvas.drawLine(points[0], points[1], x, y, mFlagPaint);
            } else {
                float x = points[2] + (points[4] - points[2]) * flagProgress;
                float y = points[3] + (points[5] - points[3]) * flagProgress;
                canvas.drawLine(points[0], points[1], points[2], points[3], mFlagPaint);
                canvas.drawLine(points[2], points[3], x, y, mFlagPaint);
            }
        }
    }

    /**
     * Set round color
     *
     * @param color color
     */
    public void setCheckedColor(int color) {
        this.mCheckedColor = color;
    }

    /**
     * Set the color when not selected
     *
     * @param color color
     */
    public void setUnCheckedColor(int color) {
        this.mUnCheckedColor = color;
    }

    /**
     * Set checkmark color
     *
     * @param color color
     */
    public void setFlagColor(int color) {
        mFlagPaint.setColor(color);
    }

    private int evaluateColor(float fraction, int startValue, int endValue) {
        int startA = (startValue >> 24) & 0xff;
        int startR = (startValue >> 16) & 0xff;
        int startG = (startValue >> 8) & 0xff;
        int startB = startValue & 0xff;

        int endA = (endValue >> 24) & 0xff;
        int endR = (endValue >> 16) & 0xff;
        int endG = (endValue >> 8) & 0xff;
        int endB = endValue & 0xff;

        return ((startA + (int) (fraction * (endA - startA))) << 24)
                | ((startR + (int) (fraction * (endR - startR))) << 16)
                | ((startG + (int) (fraction * (endG - startG))) << 8)
                | ((startB + (int) (fraction * (endB - startB))));
    }

    private void showAsChecked(final boolean checked) {
        if (isAnim) {
            return;
        }
        isAnim = true;
        final ValueAnimator va = ValueAnimator.ofFloat(0, 1).setDuration(mAnimDuration);
        va.setInterpolator(new LinearInterpolator());
        va.start();
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue(); // 0f ~ 1f
                Log.d("showAsChecked()", "Value = " + value);
                mRadius = (int) ((checked ? value : 1 - value) * height * 0.375f + height * 0.125f);
                if (value >= 1) {
                    mChecked = checked;
                    isAnim = false;
                    if (listener != null) {
                        listener.onCheckedChanged(MDTouchCheckBox.this, mChecked);
                    }
                    if (mChecked) {
                        showFlag(mChecked);
                    }
                }
                invalidate();
            }
        });
    }

    private void showFlag(final boolean isVisible) {
        if (isAnim) {
            return;
        }
        isAnim = true;
        ValueAnimator va = ValueAnimator.ofFloat(0, 1).setDuration(mAnimDuration);
        va.setInterpolator(new LinearInterpolator());
        va.start();
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue(); // 0f ~ 1f
                Log.d("showAsChecked()", "Value = " + value);
                flagProgress = isVisible ? value : 1 - value;
                invalidate();
                if (value >= 1) {
                    isAnim = false;
                    if (!isVisible) {
                        showAsChecked(false);
                    }
                }
            }
        });
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.listener = listener;
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(View buttonView, boolean isChecked);
    }

    /**
     * According to the resolution of the mobile phone unit from dp turn became px (pixels)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * According to the resolution of the phone from px (pixel) units turn become dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}