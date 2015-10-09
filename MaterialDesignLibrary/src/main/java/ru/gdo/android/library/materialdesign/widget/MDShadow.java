package ru.gdo.android.library.materialdesign.widget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
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
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.gdo.android.library.materialdesign.R;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 05.10.15.
 */

public abstract class MDShadow extends FrameLayout implements View.OnTouchListener {

    private static final String ANDROID_NAMESPACE = "http://schemas.android.com/apk/res/android";
    private static final String ATTRIBUTE_ENABLED = "enabled";

    private static final boolean DEFAULT_ENABLED = true;

    protected TranslateAnimation mDownAnimation;
    protected long startTime;
    protected View mChildView;

    private GestureDetector mGestureDetector;

    private MDShadowAttributes mShadowAttributes = new MDShadowAttributes();

    public MDShadow(Context context) {
        this(context, null);
    }

    public MDShadow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MDShadow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @SuppressWarnings("unused")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MDShadow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    protected void initView(Context context, AttributeSet attrs) {

        this.mChildView = getChildView(context, attrs);
        initAttributes(context, attrs);

        this.setPadding(
                this.mShadowAttributes.getLeftPadding(),
                this.mShadowAttributes.getTopPadding(),
                this.mShadowAttributes.getRightPadding(),
                this.mShadowAttributes.getBottomPadding()
        );

        setOnTouchListener(this);

        GestureDetector.SimpleOnGestureListener mLongClickListener = new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {
                MDShadow.this.performLongClick();
            }
        };
        this.mGestureDetector = new GestureDetector(context, mLongClickListener);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);

        this.mShadowAttributes.getBounds().set(0, 0, w, h);
        if (w > 0 && h > 0 && (this.getBackground() == null ||
                this.mShadowAttributes.isInvalidateShadowOnSizeChanged() ||
                this.mShadowAttributes.isForceInvalidateShadow())) {
            this.mShadowAttributes.setForceInvalidateShadow(false);
            this.setBackgroundCompat(w, h);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.initLayout();
        {
            for (int i = 0; i < this.getChildCount(); i++) {
                this.getChildAt(i).setEnabled(this.isEnabled());
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (this.mShadowAttributes.isForceInvalidateShadow()) {
            this.mShadowAttributes.setForceInvalidateShadow(false);
            this.setBackgroundCompat(right - left, bottom - top);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return !findClickableViewInChild(this, (int) event.getX(), (int) event.getY());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        boolean superOnTouchEvent = super.onTouchEvent(event);

        if (!isEnabled()) return superOnTouchEvent;

        boolean gestureResult = this.mGestureDetector.onTouchEvent(event);
        if (gestureResult) {
            return true;
        } else {
            try {
                int action = event.getActionMasked();
                switch (action) {
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        if (this.mShadowAttributes.isAnimationEnabled()) {
                            Float xTraveled = this.mShadowAttributes.getDx();
                            Float yTraveled = this.mShadowAttributes.getDy();
                            if (mDownAnimation != null) {
                                Transformation trans = new Transformation();
                                long endTime = mDownAnimation.getStartTime() + System.currentTimeMillis() - startTime;
                                mDownAnimation.getTransformation(endTime, trans);

                                this.clearAnimation();

                                Matrix transformationMatrix = trans.getMatrix();
                                float[] matrixValues = new float[9];
                                transformationMatrix.getValues(matrixValues);
                                xTraveled = matrixValues[2];
                                yTraveled = matrixValues[5];
                            }

                            TranslateAnimation upAnimation = new TranslateAnimation(xTraveled.floatValue(), 0, yTraveled.floatValue(), 0);
                            upAnimation.setDuration(this.mShadowAttributes.getDuration());
                            upAnimation.setFillAfter(true);
                            upAnimation.setAnimationListener(new TranslateAnimation.AnimationListener() {

                                @Override
                                public void onAnimationStart(Animation animation) {
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    MDShadow.this.setBackgroundCompat(
                                            MDShadow.this.mShadowAttributes.getBounds().width(),
                                            MDShadow.this.mShadowAttributes.getBounds().height());
                                }
                            });
                            this.startAnimation(upAnimation);
                        }
                        return true;
                    case MotionEvent.ACTION_DOWN:
                        this.clearAnimation();
                        if (this.mShadowAttributes.isAnimationEnabled()) {
                            mDownAnimation = new TranslateAnimation(0, this.mShadowAttributes.getDx(), 0, this.mShadowAttributes.getDy());
                            mDownAnimation.setDuration(this.mShadowAttributes.getDuration());
                            mDownAnimation.setFillAfter(true);

                            mDownAnimation.setAnimationListener(new TranslateAnimation.AnimationListener() {

                                @Override
                                public void onAnimationStart(Animation animation) {
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    mDownAnimation = null;
                                }
                            });

                            this.startAnimation(mDownAnimation);
                            startTime = System.currentTimeMillis();
                            this.setNullableDrawable();
                        }

                        return true;
                    case MotionEvent.ACTION_MOVE:
                        return true;
                }
            } finally {
                for (int i = 0; i < this.getChildCount(); i++) {
                    this.getChildAt(i).dispatchTouchEvent(event);
                }
            }
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
    public boolean performLongClick() {
        Boolean result = super.performLongClick();

        if (this.mChildView != null) {
            this.mChildView.performLongClick();
        }

        return result;
    }


    @SuppressWarnings("unused")
    public void setAnimationEnabled(boolean animationEnabled) {
        if (animationEnabled) {
            setDuration(MDShadowAttributes.DEFAULT_DURATION);
        } else {
            setDuration(-1);
        }
    }

    public void setDuration(int duration) {
        if (duration < 0) {
            duration = -1;
            this.mShadowAttributes.setAnimationEnabled(false);
        } else {
            this.mShadowAttributes.setAnimationEnabled(true);
        }
        this.mShadowAttributes.setDuration(duration);
    }


    public boolean isViewEnabled() {
        return (isEnabled() && (this.mChildView != null) && (this.mChildView.isEnabled()));
    }

    public abstract View getChildView(Context context, AttributeSet attrs);

    protected void initLayout() {
        if (this.mChildView != null) {
            if (this.getChildCount() > 0) {
                this.removeAllViews();
            }
            this.addView(
                    this.mChildView,
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
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

    private void initAttributes(Context context, AttributeSet attrs) {
        TypedArray attr = getTypedArray(context, attrs, R.styleable.MDShadowFrameLayout);
        if (attr == null) {
            return;
        }

        this.setEnabled(attrs.getAttributeBooleanValue(ANDROID_NAMESPACE, ATTRIBUTE_ENABLED, DEFAULT_ENABLED));

        try {
            this.mShadowAttributes.setCornerRadius(attr.getDimension(R.styleable.MDShadowFrameLayout_sl_cornerRadius, MDShadowAttributes.DEFAULT_CORNER_RADIUS));
            this.mShadowAttributes.setShadowRadius(attr.getDimension(R.styleable.MDShadowFrameLayout_sl_shadowRadius, MDShadowAttributes.DEFAULT_SHADOW_RADIUS));
            this.mShadowAttributes.setDx(attr.getDimension(R.styleable.MDShadowFrameLayout_sl_dx, MDShadowAttributes.DEFAULT_SHADOW_DX));
            this.mShadowAttributes.setDy(attr.getDimension(R.styleable.MDShadowFrameLayout_sl_dy, MDShadowAttributes.DEFAULT_SHADOW_DY));
            this.mShadowAttributes.setShadow(attr.getResourceId(R.styleable.MDShadowFrameLayout_sl_shadow, MDShadowAttributes.DEFAULT_NULL_RESOURCES_ID));
            this.mShadowAttributes.setAnimationEnabled(attr.getBoolean(R.styleable.MDShadowFrameLayout_sl_animEnabled, MDShadowAttributes.DEFAULT_ANIMATION_ENABLED));
            this.mShadowAttributes.setDuration(attr.getInt(R.styleable.MDShadowFrameLayout_sl_duration, MDShadowAttributes.DEFAULT_DURATION));
            if (this.mShadowAttributes.getDuration() < 0) {
                this.mShadowAttributes.setDuration(1);
                this.mShadowAttributes.setAnimationEnabled(false);
            }
        } finally {
            attr.recycle();
        }
    }

    private TypedArray getTypedArray(Context context, AttributeSet attributeSet, int[] attr) {
        return context.obtainStyledAttributes(attributeSet, attr, 0, 0);
    }

    @SuppressWarnings("deprecation")
    private void setNullableDrawable() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
            this.setBackgroundDrawable(null);
        } else {
            this.setBackground(null);
        }
    }

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    private void setBackgroundCompat(int w, int h) {
        Bitmap bitmap = createShadowBitmap(w, h, Color.TRANSPARENT);
        BitmapDrawable drawable = new BitmapDrawable(this.getResources(), bitmap);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
            this.setBackgroundDrawable(drawable);
        } else {
            this.setBackground(drawable);
        }
    }

    private Bitmap createShadowBitmap(int shadowWidth, int shadowHeight,
                                      int fillColor) {
        Bitmap output = Bitmap.createBitmap(shadowWidth, shadowHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        if (!this.isViewEnabled()) {
            output.eraseColor(fillColor);
        } else if ((this.mShadowAttributes.getShadow() == MDShadowAttributes.DEFAULT_NULL_RESOURCES_ID) ||
                ("color".equals(this.getResources().getResourceTypeName(this.mShadowAttributes.getShadow())))) {

            int shadowColor;

            if (this.mShadowAttributes.getShadow() == MDShadowAttributes.DEFAULT_NULL_RESOURCES_ID) {
                shadowColor = Color.parseColor(MDShadowAttributes.DEFAULT_FILL_COLOR);
            } else {
                shadowColor = ContextCompat.getColor(this.getContext(), this.mShadowAttributes.getShadow());
            }
            RectF shadowRect = new RectF(
                    this.mShadowAttributes.getCornerRadius(),
                    this.mShadowAttributes.getCornerRadius(),
                    shadowWidth - this.mShadowAttributes.getCornerRadius() - Math.abs(this.mShadowAttributes.getDx()),
                    shadowHeight - this.mShadowAttributes.getCornerRadius() - Math.abs(this.mShadowAttributes.getDy()));

            Paint shadowPaint = new Paint();
            shadowPaint.setAntiAlias(true);
            shadowPaint.setColor(fillColor);
            shadowPaint.setStyle(Paint.Style.FILL);

            if (this.mShadowAttributes.getDx() < 0) this.mShadowAttributes.setDx(0);
            if (this.mShadowAttributes.getDy() < 0) this.mShadowAttributes.setDy(0);
            if (!this.isInEditMode()) {
                shadowPaint.setShadowLayer(this.mShadowAttributes.getShadowRadius(),
                        this.mShadowAttributes.getDx(),
                        this.mShadowAttributes.getDx(),
                        shadowColor);
            }

            canvas.drawRoundRect(shadowRect, this.mShadowAttributes.getCornerRadius(),
                    this.mShadowAttributes.getCornerRadius(), shadowPaint);
        } else if ("drawable".equals(this.getResources().getResourceTypeName(this.mShadowAttributes.getShadow()))) {
            output.eraseColor(fillColor);

            if (this.mShadowAttributes.getDx() < 0) this.mShadowAttributes.setDx(0);
            if (this.mShadowAttributes.getDy() < 0) this.mShadowAttributes.setDy(0);
            canvas.drawBitmap(
                    get_ninepatch(
                            this.mShadowAttributes.getShadow(),
                            (int) (shadowWidth - this.mShadowAttributes.getCornerRadius() - Math.abs(this.mShadowAttributes.getDx())),
                            (int) (shadowHeight - this.mShadowAttributes.getCornerRadius() - Math.abs(this.mShadowAttributes.getDy())),
                            this.getContext()
                    ),
                    this.mShadowAttributes.getDx(),
                    this.mShadowAttributes.getDy(),
                    new Paint());
        } else {
            output.eraseColor(fillColor);
        }

        return output;
    }

    private Bitmap get_ninepatch(int resourceId, int x, int y, Context context) {

        if (x < 0) x = 0;
        if (y < 0) y = 0;

        Bitmap output_bitmap = Bitmap.createBitmap(x, y, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output_bitmap);

        Bitmap bitmap = BitmapFactory.decodeResource(
                context.getResources(), resourceId);

        NinePatchDrawable np_drawable = new NinePatchDrawable(
                context.getResources(),
                bitmap,
                bitmap.getNinePatchChunk(), new Rect(), null);
        np_drawable.setBounds(0, 0, x, y);

        np_drawable.draw(canvas);

        return output_bitmap;
    }

    @SuppressWarnings("unused")
    protected void invalidateShadow() {
        this.mShadowAttributes.setForceInvalidateShadow(true);
        requestLayout();
        invalidate();
    }

    @SuppressWarnings("unused")
    protected void setInvalidateShadowOnSizeChanged(boolean invalidateShadowOnSizeChanged) {
        this.mShadowAttributes.setInvalidateShadowOnSizeChanged(invalidateShadowOnSizeChanged);
    }

    class MDShadowAttributes {

        public static final int DEFAULT_SHADOW_RADIUS = 4;
        public static final int DEFAULT_CORNER_RADIUS = 4;
        public static final int DEFAULT_SHADOW_DX = 2;
        public static final int DEFAULT_SHADOW_DY = 2;
        public static final String DEFAULT_FILL_COLOR = "#88757575";
        public static final int DEFAULT_DURATION = 30;
        public static final boolean DEFAULT_ANIMATION_ENABLED = true;
        public static final int DEFAULT_NULL_RESOURCES_ID = -1;

        private int mShadow;
        private float mShadowRadius;
        private float mCornerRadius;
        private float mDx;
        private float mDy;
        private int mDuration;
        private boolean mAnimationEnabled;

        private final Rect mBounds = new Rect();
        private boolean mForceInvalidateShadow = false;
        private boolean mInvalidateShadowOnSizeChanged = true;

        public int getShadow() {
            return mShadow;
        }

        public void setShadow(int shadow) {
            this.mShadow = shadow;
        }

        public float getShadowRadius() {
            return mShadowRadius;
        }

        public void setShadowRadius(float shadowRadius) {
            this.mShadowRadius = shadowRadius;
        }

        public float getCornerRadius() {
            return mCornerRadius;
        }

        public void setCornerRadius(float cornerRadius) {
            this.mCornerRadius = cornerRadius;
        }

        public float getDx() {
            return mDx;
        }

        public void setDx(float dx) {
            this.mDx = dx;
        }

        public float getDy() {
            return mDy;
        }

        public void setDy(float dy) {
            this.mDy = dy;
        }

        public int getDuration() {
            return mDuration;
        }

        public void setDuration(int duration) {
            this.mDuration = duration;
        }

        public boolean isAnimationEnabled() {
            return mAnimationEnabled;
        }

        public void setAnimationEnabled(boolean animationEnabled) {
            this.mAnimationEnabled = animationEnabled;
        }

        public int getTopPadding() {
            return (this.mDy < 0) ? (int) (this.mShadowRadius + Math.abs(this.mDy)) : 0;
        }

        public int getBottomPadding() {
            return (this.mDy > 0) ? (int) (this.mShadowRadius + Math.abs(this.mDy)) : 0;
        }

        public int getLeftPadding() {
            return (this.mDx < 0) ? (int) (this.mShadowRadius + Math.abs(this.mDx)) : 0;
        }

        public int getRightPadding() {
            return (this.mDx > 0) ? (int) (this.mShadowRadius + Math.abs(this.mDx)) : 0;
        }

        public Rect getBounds() {
            return mBounds;
        }

        public boolean isForceInvalidateShadow() {
            return mForceInvalidateShadow;
        }

        public void setForceInvalidateShadow(boolean forceInvalidateShadow) {
            this.mForceInvalidateShadow = forceInvalidateShadow;
        }

        public boolean isInvalidateShadowOnSizeChanged() {
            return mInvalidateShadowOnSizeChanged;
        }

        public void setInvalidateShadowOnSizeChanged(boolean invalidateShadowOnSizeChanged) {
            this.mInvalidateShadowOnSizeChanged = invalidateShadowOnSizeChanged;
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (isEnabled() != enabled) {
            super.setEnabled(enabled);
            for (int i = 0; i < this.getChildCount(); i++) {
                this.getChildAt(i).setEnabled(enabled);
            }
            invalidateShadow();
        }
    }

    @SuppressWarnings("unused")
    public void setTextSize(int unit, float size) {
        if ((this.mChildView != null) && (this.mChildView instanceof TextView)) {
            ((TextView) this.mChildView).setTextSize(unit, size);
        }
    }

    @SuppressWarnings("unused")
    public void setTextSize(float size) {
        if ((this.mChildView != null) && (this.mChildView instanceof TextView)) {
            ((TextView) this.mChildView).setTextSize(size);
        }
    }

    @SuppressWarnings("unused")
    public void setText(CharSequence text) {
        if ((this.mChildView != null) && (this.mChildView instanceof TextView)) {
            ((TextView) this.mChildView).setText(text);
        }
    }

    @SuppressWarnings("unused")
    public void setText(CharSequence text, TextView.BufferType type) {
        if ((this.mChildView != null) && (this.mChildView instanceof TextView)) {
            ((TextView) this.mChildView).setText(text, type);
        }
    }

    @SuppressWarnings("unused")
    public void setText(char[] text, int start, int len) {
        if ((this.mChildView != null) && (this.mChildView instanceof TextView)) {
            ((TextView) this.mChildView).setText(text, start, len);
        }
    }

    @SuppressWarnings("unused")
    public void setText(@StringRes int resId) {
        if ((this.mChildView != null) && (this.mChildView instanceof TextView)) {
            ((TextView) this.mChildView).setText(resId);
        }
    }

    @SuppressWarnings("unused")
    public void setText(@StringRes int resId, TextView.BufferType type) {
        if ((this.mChildView != null) && (this.mChildView instanceof TextView)) {
            ((TextView) this.mChildView).setText(resId, type);
        }
    }

}
