package ru.gdo.android.library.materialdesign.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Property;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Button;

import ru.gdo.android.library.materialdesign.R;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 30.09.15.
 */

public class MDRippleButton extends Button implements OnTouchListener {

    private static final int     DEFAULT_DURATION        = 350;
    private static final int     DEFAULT_FADE_DURATION   = 75;
    private static final float   DEFAULT_DIAMETER_DP     = 35;
    private static final float   DEFAULT_ALPHA           = 0.2f;
    private static final int     DEFAULT_COLOR           = Color.BLACK;
    private static final int     DEFAULT_BACKGROUND      = Color.TRANSPARENT;
    private static final boolean DEFAULT_HOVER           = true;
    private static final boolean DEFAULT_DELAY_CLICK     = true;
    private static final boolean DEFAULT_PERSISTENT      = false;
    private static final boolean DEFAULT_SEARCH_ADAPTER  = false;
    private static final boolean DEFAULT_RIPPLE_OVERLAY  = false;
    private static final int     DEFAULT_ROUNDED_CORNERS = 0;

    private static final int  FADE_EXTRA_DELAY = 50;
    private static final long HOVER_DURATION   = 2500;

    private final Rect mBounds = new Rect();

    private Point mPreviousCoords = new Point();

    private boolean mPrepressed;
    private boolean mEventCancelled;

    private GestureDetector mGestureDetector;
    private PressedEvent    mPendingPressEvent;

//    private View mChildView;

    private int mRippleDuration;
    private int mRippleFadeDuration;
    private int mRippleDiameter;
    private int mRippleAlpha;
    private int mRippleColor;
    private Drawable mRippleBackground;
    private boolean mRippleHover;
    private boolean mRippleDelayClick;
    private boolean mRipplePersistent;
    private boolean mRippleInAdapter;
    private boolean mRippleOverlay;
    private float mRippleRoundedCorners;

    private float mRadius;

    private boolean mHasPerformedLongPress;

    private GestureDetector.SimpleOnGestureListener mLongClickListener = new GestureDetector.SimpleOnGestureListener() {
        public void onLongPress(MotionEvent e) {
            mHasPerformedLongPress = performLongClick();
            if (mHasPerformedLongPress) {
                if (mRippleHover) {
                    startRipple(null);
                }
                cancelPressedEvent();
            }
        }

        @Override
        public boolean onDown(MotionEvent e) {
            MDRippleButton.this.mHasPerformedLongPress = false;
            return super.onDown(e);
        }
    };

    public MDRippleButton(Context context) {
        this(context, null);
    }

    public MDRippleButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MDRippleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MDRippleButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    protected void initView(Context context, AttributeSet attrs) {

        setWillNotDraw(false);
        this.mGestureDetector = new GestureDetector(context, this.mLongClickListener);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RippleLayout);

        this.mRippleDuration = a.getInt(R.styleable.RippleLayout_rl_Duration, DEFAULT_DURATION);
        this.mRippleFadeDuration = a.getInteger(R.styleable.RippleLayout_rl_FadeDuration, DEFAULT_FADE_DURATION);
        this.mRippleDiameter = a.getDimensionPixelSize(
                R.styleable.RippleLayout_rl_Dimension,
                (int) dpToPx(getResources(), DEFAULT_DIAMETER_DP)
        );
        this.mRippleAlpha = (int) (255 * a.getFloat(R.styleable.RippleLayout_rl_Alpha, DEFAULT_ALPHA));
        this.mRippleColor = a.getColor(R.styleable.RippleLayout_rl_Color, DEFAULT_COLOR);
        this.mRippleBackground = new ColorDrawable(a.getColor(R.styleable.RippleLayout_rl_Background, DEFAULT_BACKGROUND));
        this.mRippleHover = a.getBoolean(R.styleable.RippleLayout_rl_Hover, DEFAULT_HOVER);
        this.mRippleDelayClick = a.getBoolean(R.styleable.RippleLayout_rl_DelayClick, DEFAULT_DELAY_CLICK);
        this.mRipplePersistent = a.getBoolean(R.styleable.RippleLayout_rl_Persistent, DEFAULT_PERSISTENT);
        this.mRippleInAdapter = a.getBoolean(R.styleable.RippleLayout_rl_InAdapter, DEFAULT_SEARCH_ADAPTER);
        this.mRippleOverlay = a.getBoolean(R.styleable.RippleLayout_rl_Overlay, DEFAULT_RIPPLE_OVERLAY);
        this.mRippleRoundedCorners = a.getDimensionPixelSize(R.styleable.RippleLayout_rl_RoundedCorners, DEFAULT_ROUNDED_CORNERS);

        a.recycle();

        this.mPaint.setColor(this.mRippleColor);
        this.mPaint.setAlpha(this.mRippleAlpha);

        this.setOnTouchListener(this);

        enableClipPathSupportIfNecessary();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
//        return false;
//    }
//
//
//    @Override
//    public boolean onTouchEvent(@NonNull MotionEvent event) {
//        boolean superOnTouchEvent = super.onTouchEvent(event);

        if (!isEnabled()) return false;

        boolean isEventInBounds = this.mBounds.contains((int) event.getX(), (int) event.getY());

        if (isEventInBounds) {
            this.mPreviousCoords.set(this.mCurrentCoords.x, this.mCurrentCoords.y);
            this.mCurrentCoords.set((int) event.getX(), (int) event.getY());
        }

        boolean gestureResult = this.mGestureDetector.onTouchEvent(event);
        if (gestureResult || this.mHasPerformedLongPress) {
            return true;
        } else {
            int action = event.getActionMasked();
            switch (action) {
                case MotionEvent.ACTION_UP:
                    PerformClickEvent mPendingClickEvent = new PerformClickEvent();

                    if (this.mPrepressed) {
                        this.setPressed(true);
                        postDelayed(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        MDRippleButton.this.setPressed(false);
                                    }
                                }, ViewConfiguration.getPressedStateDuration());
                    }
                    if (isEventInBounds) {
                        startRipple(mPendingClickEvent);
                    } else if (!this.mRippleHover) {
                        setRadius(0);
                    }
                    if (!this.mRippleDelayClick && isEventInBounds) {
                        mPendingClickEvent.run();
                    }
                    cancelPressedEvent();
                    break;
                case MotionEvent.ACTION_DOWN:
                    setPositionInAdapter();
                    this.mEventCancelled = false;
                    this.mPendingPressEvent = new PressedEvent(event);
                    if (isInScrollingContainer()) {
                        cancelPressedEvent();
                        this.mPrepressed = true;
                        postDelayed(this.mPendingPressEvent, ViewConfiguration.getTapTimeout());
                    } else {
                        this.mPendingPressEvent.run();
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                    if (this.mRippleInAdapter) {
                        this.mCurrentCoords.set(this.mPreviousCoords.x, this.mPreviousCoords.y);
                        this.mPreviousCoords = new Point();
                    }
                    if (this.mRippleHover) {
                        if (!this.mPrepressed) {
                            startRipple(null);
                        }
                    } else {
                        this.setPressed(false);
                    }
                    cancelPressedEvent();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (this.mRippleHover) {
                        if (isEventInBounds && !this.mEventCancelled) {
                            invalidate();
                        } else if (!isEventInBounds) {
                            startRipple(null);
                        }
                    }

                    if (!isEventInBounds) {
                        cancelPressedEvent();
                        if (this.mHoverAnimator != null) {
                            this.mHoverAnimator.cancel();
                        }
                        this.mEventCancelled = true;
                    }
                    break;
            }
            return true;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mBounds.set(0, 0, w, h);
        this.mRippleBackground.setBounds(this.mBounds);
    }

    @Override
    public boolean isInEditMode() {
        return true;
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        return MDRippleButton.class.getName();
    }

    private void cancelPressedEvent() {
        if (this.mPendingPressEvent != null) {
            removeCallbacks(this.mPendingPressEvent);
            mPrepressed = false;
        }
    }

    private float getEndRadius() {
        final int width = getWidth();
        final int height = getHeight();

        final int halfWidth = width / 2;
        final int halfHeight = height / 2;

        final float radiusX = halfWidth > this.mCurrentCoords.x ? width - this.mCurrentCoords.x : this.mCurrentCoords.x;
        final float radiusY = halfHeight > this.mCurrentCoords.y ? height - this.mCurrentCoords.y : this.mCurrentCoords.y;

        return (float) Math.sqrt(Math.pow(radiusX, 2) + Math.pow(radiusY, 2)) * 1.2f;
    }

    private boolean isInScrollingContainer() {
        ViewParent p = getParent();
        while (p != null && p instanceof ViewGroup) {
            if (((ViewGroup) p).shouldDelayChildPressedState()) {
                return true;
            }
            p = p.getParent();
        }
        return false;
    }

    /*
     * Drawing
     */
    @Override
    public void draw(@NonNull Canvas canvas) {
        final boolean positionChanged = adapterPositionChanged();
        if (this.mRippleOverlay) {
            if (!positionChanged) {
                this.mRippleBackground.draw(canvas);
            }
            super.draw(canvas);
            if (!positionChanged) {
                if (this.mRippleRoundedCorners != 0) {
                    Path clipPath = new Path();
                    RectF rect = new RectF(0, 0, canvas.getWidth(), canvas.getHeight());
                    clipPath.addRoundRect(rect, this.mRippleRoundedCorners, this.mRippleRoundedCorners, Path.Direction.CW);
                    canvas.clipPath(clipPath);
                }
                canvas.drawCircle(this.mCurrentCoords.x, this.mCurrentCoords.y, this.mRadius, this.mPaint);
            }
        } else {
            if (!positionChanged) {
                this.mRippleBackground.draw(canvas);
                canvas.drawCircle(this.mCurrentCoords.x, this.mCurrentCoords.y, this.mRadius, this.mPaint);
            }
            super.draw(canvas);
        }
    }

    /*
    * Adapters
    */

    private AdapterView mParentAdapter;

    private int mPositionInAdapter;

    private AdapterView findParentAdapterView() {
        if (this.mParentAdapter != null) {
            return this.mParentAdapter;
        }
        ViewParent current = getParent();
        while (true) {
            if (current instanceof AdapterView) {
                this.mParentAdapter = (AdapterView) current;
                return this.mParentAdapter;
            } else {
                try {
                    current = current.getParent();
                } catch (NullPointerException npe) {
                    throw new RuntimeException("Could not find a parent AdapterView");
                }
            }
        }
    }

    private void setPositionInAdapter() {
        if (this.mRippleInAdapter) {
            this.mPositionInAdapter = findParentAdapterView().getPositionForView(MDRippleButton.this);
        }
    }

    private boolean adapterPositionChanged() {
        if (this.mRippleInAdapter) {
            int newPosition = findParentAdapterView().getPositionForView(MDRippleButton.this);
            final boolean changed = newPosition != this.mPositionInAdapter;
            this.mPositionInAdapter = newPosition;
            if (changed) {
                cancelPressedEvent();
                cancelAnimations();
                this.setPressed(false);
                setRadius(0);
            }
            return changed;
        }
        return false;
    }


    /*
    * Animations
    */
    private ObjectAnimator mHoverAnimator;

    private AnimatorSet mRippleAnimator;

    private Property<MDRippleButton, Float> mRadiusProperty
            = new Property<MDRippleButton, Float>(Float.class, "radius") {
        @Override
        public Float get(MDRippleButton object) {
            return object.getRadius();
        }

        @Override
        public void set(MDRippleButton object, Float value) {
            object.setRadius(value);
        }
    };

    private Property<MDRippleButton, Integer> mCircleAlphaProperty
            = new Property<MDRippleButton, Integer>(Integer.class, "rippleAlpha") {
        @Override
        public Integer get(MDRippleButton object) {
            return object.getRippleAlpha();
        }

        @Override
        public void set(MDRippleButton object, Integer value) {
            object.setRippleAlpha(value);
        }
    };

    private float getRadius() {
        return this.mRadius;
    }

    public void setRadius(float radius) {
        this.mRadius = radius;
        invalidate();
    }

    private void startRipple(final Runnable animationEndRunnable) {
        if (mEventCancelled) return;

        float endRadius = getEndRadius();

        cancelAnimations();

        this.mRippleAnimator = new AnimatorSet();
        this.mRippleAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (!MDRippleButton.this.mRipplePersistent) {
                    setRadius(0);
                    setRippleAlpha(MDRippleButton.this.mRippleAlpha);
                }
                if (animationEndRunnable != null && MDRippleButton.this.mRippleDelayClick) {
                    animationEndRunnable.run();
                }
                MDRippleButton.this.setPressed(false);
            }
        });

        ObjectAnimator ripple = ObjectAnimator.ofFloat(this, this.mRadiusProperty, this.mRadius, endRadius);
        ripple.setDuration(this.mRippleDuration);
        ripple.setInterpolator(new DecelerateInterpolator());
        ObjectAnimator fade = ObjectAnimator.ofInt(this, this.mCircleAlphaProperty, this.mRippleAlpha, 0);
        fade.setDuration(this.mRippleFadeDuration);
        fade.setInterpolator(new AccelerateInterpolator());
        fade.setStartDelay(this.mRippleDuration - this.mRippleFadeDuration - FADE_EXTRA_DELAY);

        if (this.mRipplePersistent) {
            this.mRippleAnimator.play(ripple);
        } else if (getRadius() > endRadius) {
            fade.setStartDelay(0);
            this.mRippleAnimator.play(fade);
        } else {
            this.mRippleAnimator.playTogether(ripple, fade);
        }
        this.mRippleAnimator.start();
    }

    public int getRippleAlpha() {
        return this.mPaint.getAlpha();
    }

    public void setRippleAlpha(Integer rippleAlpha) {
        this.mPaint.setAlpha(rippleAlpha);
        invalidate();
    }

    private void startHover() {
        if (this.mEventCancelled) return;

        if (this.mHoverAnimator != null) {
            this.mHoverAnimator.cancel();
        }
        final float radius = (float) (Math.sqrt(Math.pow(getWidth(), 2) + Math.pow(getHeight(), 2)) * 1.2f);
        this.mHoverAnimator = ObjectAnimator.ofFloat(this, this.mRadiusProperty, this.mRippleDiameter, radius)
                .setDuration(HOVER_DURATION);
        this.mHoverAnimator.setInterpolator(new LinearInterpolator());
        this.mHoverAnimator.start();
    }

    private void cancelAnimations() {
        if (this.mRippleAnimator != null) {
            this.mRippleAnimator.cancel();
            this.mRippleAnimator.removeAllListeners();
        }

        if (this.mHoverAnimator != null) {
            this.mHoverAnimator.cancel();
        }
    }

    /*
    * Accessor
    */

    private final Paint mPaint  = new Paint(Paint.ANTI_ALIAS_FLAG);


    private Point mCurrentCoords  = new Point();

    private int mLayerType;

    public void setRippleColor(int rippleColor) {
        this.mRippleColor = rippleColor;
        this.mPaint.setColor(rippleColor);
        this.mPaint.setAlpha(this.mRippleAlpha);
        invalidate();
    }

    public void setRippleOverlay(boolean rippleOverlay) {
        this.mRippleOverlay = rippleOverlay;
    }

    public void setRippleDiameter(int rippleDiameter) {
        this.mRippleDiameter = rippleDiameter;
    }

    public void setRippleDuration(int rippleDuration) {
        this.mRippleDuration = rippleDuration;
    }

    public void setRippleBackground(int color) {
        this.mRippleBackground = new ColorDrawable(color);
        this.mRippleBackground.setBounds(this.mBounds);
        invalidate();
    }

    public void setRippleHover(boolean rippleHover) {
        this.mRippleHover = rippleHover;
    }

    public void setRippleDelayClick(boolean rippleDelayClick) {
        this.mRippleDelayClick = rippleDelayClick;
    }

    public void setRippleFadeDuration(int rippleFadeDuration) {
        this.mRippleFadeDuration = rippleFadeDuration;
    }

    public void setRipplePersistent(boolean ripplePersistent) {
        this.mRipplePersistent = ripplePersistent;
    }

    public void setRippleInAdapter(boolean rippleInAdapter) {
        this.mRippleInAdapter = rippleInAdapter;
    }

    public void setRippleRoundedCorners(int rippleRoundedCorner) {
        this.mRippleRoundedCorners = rippleRoundedCorner;
        enableClipPathSupportIfNecessary();
    }

    public void setDefaultRippleAlpha(int alpha) {
        this.mRippleAlpha = alpha;
        this.mPaint.setAlpha(alpha);
        invalidate();
    }

    public void performRipple() {
        this.mCurrentCoords = new Point(getWidth() / 2, getHeight() / 2);
        startRipple(null);
    }

    public void performRipple(Point anchor) {
        this.mCurrentCoords = new Point(anchor.x, anchor.y);
        startRipple(null);
    }

    /**
     * {@link Canvas#clipPath(Path)} is not supported in hardware accelerated layers
     * before API 18. Use software layer instead
     * <p/>
     * https://developer.android.com/guide/topics/graphics/hardware-accel.html#unsupported
     */
    private void enableClipPathSupportIfNecessary() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (this.mRippleRoundedCorners != 0) {
                this.mLayerType = getLayerType();
                setLayerType(LAYER_TYPE_SOFTWARE, null);
            } else {
                setLayerType(this.mLayerType, null);
            }
        }
    }

    /*
     * Helper
     */
    private class PerformClickEvent implements Runnable {

        @Override
        public void run() {
            if (MDRippleButton.this.mHasPerformedLongPress) return;

            // if parent is an AdapterView, try to call its ItemClickListener
            if (getParent() instanceof AdapterView) {
                clickAdapterView((AdapterView) getParent());
            } else if (MDRippleButton.this.mRippleInAdapter) {
                // find adapter view
                clickAdapterView(findParentAdapterView());
            }
//            else {
                // otherwise, just perform click on child
//                MDButton.this.mChildView.performClick();
//            }
        }

        private void clickAdapterView(AdapterView parent) {
            final int position = parent.getPositionForView(MDRippleButton.this);
            final long itemId = parent.getAdapter() != null
                    ? parent.getAdapter().getItemId(position)
                    : 0;
            if (position != AdapterView.INVALID_POSITION) {
                parent.performItemClick(MDRippleButton.this, position, itemId);
            }
        }
    }

    private final class PressedEvent implements Runnable {

        private final MotionEvent event;

        public PressedEvent(MotionEvent event) {
            this.event = event;
        }

        @Override
        public void run() {
            MDRippleButton.this.mPrepressed = true;
//            MDButton.this.mChildView.setLongClickable(false);//prevent the child's long click,let's the ripple layout call it's performLongClick
//            MDButton.this.mChildView.onTouchEvent(event);
            MDRippleButton.this.setPressed(true);
            if (MDRippleButton.this.mRippleHover) {
                startHover();
            }
        }
    }

    static float dpToPx(Resources resources, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
    }

}
