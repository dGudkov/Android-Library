package ru.gdo.android.library.materialdesign.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import ru.gdo.android.library.materialdesign.R;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 30.09.15.
 */

public class MDShadowButton extends FrameLayout {

    private static final int DEFAULT_SHADOW_RADIUS = 4;
    private static final int DEFAULT_CORNER_RADIUS = 4;
    private static final int DEFAULT_SHADOW_DX = 2;
    private static final int DEFAULT_SHADOW_DY = 2;
    private static final String DEFAULT_FILL_COLOR = "#88757575";
    private static final int DEFAULT_DURATION = 30;
    private static final boolean DEFAULT_ANIMATION_ENABLED = true;
    private static final int DEFAULT_NULL_RESOURCES_ID = -1;

    private int mShadow;
    private float mShadowRadius;
    private float mCornerRadius;
    private float mDx;
    private float mDy;
    private int mDuration;
    private boolean mAnimationEnabled;

    private boolean mInvalidateShadowOnSizeChanged = true;
    private boolean mForceInvalidateShadow = false;
    private final Rect bounds = new Rect();
    private TranslateAnimation anim1;
    private long startTime;
    private MDRippleButton mChildButton;

    public MDShadowButton(Context context) {
        this(context, null);
    }

    public MDShadowButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MDShadowButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.getChildCount() > 0) {
            this.removeAllViews();
        }

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        this.addView(this.mChildButton, lp);
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

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        boolean superOnTouchEvent = super.onTouchEvent(event);

        if (!isEnabled() || !this.mChildButton.isEnabled()) return superOnTouchEvent;

        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (this.mAnimationEnabled) {
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
                    anim.setDuration(this.mDuration);
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
                            setBackgroundCompat(MDShadowButton.this.bounds.width(), MDShadowButton.this.bounds.height());
                        }
                    });
                    this.startAnimation(anim);
                }
                this.mChildButton.onTouch(this, event);
                return true;
            case MotionEvent.ACTION_DOWN:
                this.clearAnimation();
                if (this.mAnimationEnabled) {
                    anim1 = new TranslateAnimation(0, mDx, 0, mDy);
                    anim1.setDuration(this.mDuration);
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
                }

                this.mChildButton.onTouch(this, event);
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

    private void initView(Context context, AttributeSet attrs) {

        this.mChildButton = new MDRippleButton(context, attrs);

        initAttributes(context, attrs);

        int topPadding = (this.mDy < 0 ? (int) (mShadowRadius + Math.abs(mDy)) : 0);
        int bottomPadding = (this.mDy > 0 ? (int) (mShadowRadius + Math.abs(mDy)) : 0);
        int leftPadding = (this.mDx < 0 ? (int) (mShadowRadius + Math.abs(mDx)) : 0);
        int rightPadding = (this.mDx > 0 ? (int) (mShadowRadius + Math.abs(mDx)) : 0);

        setPadding(leftPadding, topPadding, rightPadding, bottomPadding);
    }

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    private void setBackgroundCompat(int w, int h) {
        Bitmap bitmap = createShadowBitmap(w, h, mCornerRadius, mShadowRadius, mDx, mDy, Color.TRANSPARENT);
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
            this.mCornerRadius = attr.getDimension(R.styleable.MDShadowLayout_sl_cornerRadius, DEFAULT_CORNER_RADIUS);
            this.mShadowRadius = attr.getDimension(R.styleable.MDShadowLayout_sl_shadowRadius, DEFAULT_SHADOW_RADIUS);
            this.mDx = attr.getDimension(R.styleable.MDShadowLayout_sl_dx, DEFAULT_SHADOW_DX);
            this.mDy = attr.getDimension(R.styleable.MDShadowLayout_sl_dy, DEFAULT_SHADOW_DY);
            this.mShadow = attr.getResourceId(R.styleable.MDShadowLayout_sl_shadow, DEFAULT_NULL_RESOURCES_ID);
            this.mAnimationEnabled = attr.getBoolean(R.styleable.MDShadowLayout_sl_animEnabled, DEFAULT_ANIMATION_ENABLED);
            this.mDuration = attr.getInt(R.styleable.MDShadowLayout_sl_duration, DEFAULT_DURATION);
            if (this.mDuration < 0) {
                this.mDuration = -1;
                this.mAnimationEnabled = false;
            }
        } finally {
            attr.recycle();
        }
    }

    private TypedArray getTypedArray(Context context, AttributeSet attributeSet, int[] attr) {
        return context.obtainStyledAttributes(attributeSet, attr, 0, 0);
    }

    private Bitmap createShadowBitmap(int shadowWidth, int shadowHeight, float cornerRadius, float shadowRadius,
                                      float dx, float dy, int fillColor) {
        Bitmap output = Bitmap.createBitmap(shadowWidth, shadowHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        if (!isEnabled() || !this.mChildButton.isEnabled()) {
            output.eraseColor(fillColor);
        } else if ((this.mShadow == DEFAULT_NULL_RESOURCES_ID) || ("color".equals(getResources().getResourceTypeName(this.mShadow)))) {

            int shadowColor;

            if (this.mShadow == DEFAULT_NULL_RESOURCES_ID) {
                shadowColor = Color.parseColor(DEFAULT_FILL_COLOR);
            } else {
                shadowColor = ContextCompat.getColor(getContext(), this.mShadow);
            }
            RectF shadowRect = new RectF(
                    shadowRadius,
                    shadowRadius,
                    shadowWidth - shadowRadius - Math.abs(dx),
                    shadowHeight - shadowRadius - Math.abs(dy));

            Paint shadowPaint = new Paint();
            shadowPaint.setAntiAlias(true);
            shadowPaint.setColor(fillColor);
            shadowPaint.setStyle(Paint.Style.FILL);

            if (dx < 0) dx = 0;
            if (dy < 0) dy = 0;
            if (!isInEditMode()) {
                shadowPaint.setShadowLayer(shadowRadius, dx, dy, shadowColor);
            }

            canvas.drawRoundRect(shadowRect, cornerRadius, cornerRadius, shadowPaint);
        } else if ("drawable".equals(getResources().getResourceTypeName(this.mShadow))) {
            output.eraseColor(fillColor);

            if (dx < 0) dx = 0;
            if (dy < 0) dy = 0;
            canvas.drawBitmap(
                    get_ninepatch(
                            this.mShadow,
                            (int) (shadowWidth - shadowRadius - Math.abs(dx)),
                            (int) (shadowHeight - shadowRadius - Math.abs(dy)),
                            getContext()
                    ),
                    dx,
                    dy,
                    new Paint());
        } else {
            output.eraseColor(fillColor);
        }

        return output;
    }

    public static Bitmap get_ninepatch(int resourceId, int x, int y, Context context){

        Bitmap output_bitmap = Bitmap.createBitmap(x, y, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output_bitmap);

        Bitmap bitmap = BitmapFactory.decodeResource(
                context.getResources(), resourceId);

        NinePatchDrawable np_drawable = new NinePatchDrawable(
                context.getResources(),
                bitmap,
                bitmap.getNinePatchChunk(), new Rect(), null);
        np_drawable.setBounds(0, 0,x, y);

        np_drawable.draw(canvas);

        return output_bitmap;
    }

    public void invalidateShadow() {
        mForceInvalidateShadow = true;
        requestLayout();
        invalidate();
    }

    public void setAnimationEnabled(boolean animationEnabled) {
        if (animationEnabled) {
            setDuration(DEFAULT_DURATION);
        } else {
            setDuration(-1);
        }
    }

    public void setDuration(int duration) {
        if (duration < 0) {
            duration = -1;
            this.mAnimationEnabled = false;
        } else {
            this.mAnimationEnabled = true;
        }
        this.mDuration = duration;
    }

    public void setInvalidateShadowOnSizeChanged(boolean invalidateShadowOnSizeChanged) {
        mInvalidateShadowOnSizeChanged = invalidateShadowOnSizeChanged;
    }

}