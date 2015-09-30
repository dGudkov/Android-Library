package ru.gdo.android.library.materialdesign.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import ru.gdo.android.library.materialdesign.R;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 29.09.15.
 */

public class MDShadowLayout extends FrameLayout {

    private static final int DEFAULT_SHADOW_RADIUS = 4;
    private static final int DEFAULT_CORNER_RADIUS = 4;
    private static final int DEFAULT_SHADOW_DX = 2;
    private static final int DEFAULT_SHADOW_DY = 2;
    private static final int DEFAULT_FILL_COLOR = 0xFFFFFFFF;

    private int mShadowColor;
    private float mShadowRadius;
    private float mCornerRadius;
    private float mDx;
    private float mDy;

    private boolean mInvalidateShadowOnSizeChanged = true;
    private boolean mForceInvalidateShadow = false;
    private final Rect  bounds = new Rect();
    private TranslateAnimation anim1;
    private long startTime;

    public MDShadowLayout(Context context) {
        this(context, null);
    }

    public MDShadowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MDShadowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private boolean findClickableViewInChild(View view, int x, int y) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                final Rect rect = new Rect();
                child.getHitRect(rect);
                if (rect.contains(x, y)) {
                    return findClickableViewInChild(child, x - rect.left, y - rect.top);
                }
            }
        }

        return view.isFocusableInTouchMode();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return !findClickableViewInChild(this, (int) event.getX(), (int) event.getY());
    }


    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        boolean superOnTouchEvent = super.onTouchEvent(event);

        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_UP:
                Float xTraveled = mDx;
                Float yTraveled = mDy;
                if (anim1 != null) {
                    Transformation trans = new Transformation();
                    long endTime = anim1.getStartTime() + System.currentTimeMillis() - startTime;
                    anim1.getTransformation(endTime, trans);

                    this.clearAnimation();

                    Matrix transformationMatrix = trans.getMatrix();
                    float[] matrixVals = new float[9];
                    transformationMatrix.getValues(matrixVals);
                    xTraveled = matrixVals[2];
                    yTraveled = matrixVals[5];
                }

                TranslateAnimation anim = new TranslateAnimation(xTraveled.floatValue(), 0, yTraveled.floatValue(), 0);
                anim.setDuration(50);
                anim.setFillAfter(true);
                anim.setAnimationListener(new TranslateAnimation.AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        setBackgroundCompat(MDShadowLayout.this.bounds.width(), MDShadowLayout.this.bounds.height());
                    }
                });
                this.startAnimation(anim);

                return true;
            case MotionEvent.ACTION_DOWN:
                this.clearAnimation();
                anim1 = new TranslateAnimation(0, mDx, 0, mDy);
                anim1.setDuration(50);
                anim1.setFillAfter(true);

                anim1.setAnimationListener(new TranslateAnimation.AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        anim1 = null;
                    }
                });

                this.startAnimation(anim1);
                startTime = System.currentTimeMillis();
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
                    setBackgroundDrawable(null);
                } else {
                    setBackground(null);
                }
                return true;
            case MotionEvent.ACTION_CANCEL:
                setBackgroundCompat(this.bounds.width(), this.bounds.height());
                return true;
            case MotionEvent.ACTION_MOVE:
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
                    setBackgroundDrawable(null);
                } else {
                    setBackground(null);
                }
                return true;
        }
        return superOnTouchEvent;
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return 0;
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return 0;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bounds.set(0, 0, w, h);
        if (w > 0 && h > 0 && (getBackground() == null || mInvalidateShadowOnSizeChanged || mForceInvalidateShadow)) {
            mForceInvalidateShadow = false;
            setBackgroundCompat(w, h);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mForceInvalidateShadow) {
            mForceInvalidateShadow = false;
            setBackgroundCompat(right - left, bottom - top);
        }
    }

    public void setInvalidateShadowOnSizeChanged(boolean invalidateShadowOnSizeChanged) {
        mInvalidateShadowOnSizeChanged = invalidateShadowOnSizeChanged;
    }

    public void invalidateShadow() {
        mForceInvalidateShadow = true;
        requestLayout();
        invalidate();
    }

    private void initView(Context context, AttributeSet attrs) {
        initAttributes(context, attrs);

        int xPadding = (int) (mShadowRadius + Math.abs(mDx));
        int yPadding = (int) (mShadowRadius + Math.abs(mDy));
        setPadding(xPadding, yPadding, xPadding, yPadding);
    }

    @SuppressWarnings("deprecation")
    private void setBackgroundCompat(int w, int h) {
        Bitmap bitmap = createShadowBitmap(w, h, mCornerRadius, mShadowRadius, mDx, mDy, mShadowColor, Color.TRANSPARENT);
        BitmapDrawable drawable = new BitmapDrawable(getResources(), bitmap);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
            setBackgroundDrawable(drawable);
        } else {
            setBackground(drawable);
        }
    }


    private void initAttributes(Context context, AttributeSet attrs) {
        TypedArray attr = getTypedArray(context, attrs, R.styleable.MDShadowLayout);
        if (attr == null) {
            return;
        }

        try {
            mCornerRadius = attr.getDimension(R.styleable.MDShadowLayout_sl_cornerRadius, DEFAULT_CORNER_RADIUS);
            mShadowRadius = attr.getDimension(R.styleable.MDShadowLayout_sl_shadowRadius, DEFAULT_SHADOW_RADIUS);
            mDx = attr.getDimension(R.styleable.MDShadowLayout_sl_dx, DEFAULT_SHADOW_DX);
            mDy = attr.getDimension(R.styleable.MDShadowLayout_sl_dy, DEFAULT_SHADOW_DY);
            mShadowColor = attr.getColor(R.styleable.MDShadowLayout_sl_shadowColor, DEFAULT_FILL_COLOR);
        } finally {
            attr.recycle();
        }
    }

    private TypedArray getTypedArray(Context context, AttributeSet attributeSet, int[] attr) {
        return context.obtainStyledAttributes(attributeSet, attr, 0, 0);
    }

    private Bitmap createShadowBitmap(int shadowWidth, int shadowHeight, float cornerRadius, float shadowRadius,
                                      float dx, float dy, int shadowColor, int fillColor) {

        Bitmap output = Bitmap.createBitmap(shadowWidth, shadowHeight, Bitmap.Config.ALPHA_8);
        Canvas canvas = new Canvas(output);

        RectF shadowRect = new RectF(
                shadowRadius,
                shadowRadius,
                shadowWidth - shadowRadius,
                shadowHeight - shadowRadius);

        if (dy > 0) {
            shadowRect.top += dy;
            shadowRect.bottom -= dy;
        } else if (dy < 0) {
            shadowRect.top += Math.abs(dy);
            shadowRect.bottom -= Math.abs(dy);
        }

        if (dx > 0) {
            shadowRect.left += dx;
            shadowRect.right -= dx;
        } else if (dx < 0) {
            shadowRect.left += Math.abs(dx);
            shadowRect.right -= Math.abs(dx);
        }

        if ((dx != 0) && (dy != 0)) {
            Paint shadowPaint = new Paint();
            shadowPaint.setAntiAlias(true);
            shadowPaint.setColor(fillColor);
            shadowPaint.setStyle(Paint.Style.FILL);

            if (!isInEditMode()) {
                shadowPaint.setShadowLayer(shadowRadius, dx, dy, shadowColor);
            }

            canvas.drawRoundRect(shadowRect, cornerRadius, cornerRadius, shadowPaint);
        }

        return output;
    }


}