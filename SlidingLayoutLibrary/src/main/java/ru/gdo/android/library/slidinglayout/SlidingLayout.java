package ru.gdo.android.library.slidinglayout;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

import ru.gdo.android.library.foldinglayout.interfaces.IAnimationListener;
import ru.gdo.android.library.foldinglayout.interfaces.IAnimationNotifier;
import ru.gdo.android.library.slidinglayout.R;

/**
 * @author Danil Gudkov <d_n_l@mail.ru>
 * @copyrights Danil Gudkov, 2015
 * @since 23.07.15.
 */

public class SlidingLayout
        extends LinearLayout
        implements View.OnClickListener,
        Animator.AnimatorListener,
        IAnimationNotifier {

    private int _offset, _delta;
    Handler mHandler;

    public enum PanelState {
        EXPANDED,
        COLLAPSED
    }

    private static final int START_ANIMATION_VALUE = 1;
    private static final int END_ANIMATION_VALUE = 100;
    private static final int ANIMATION_LENGTH = END_ANIMATION_VALUE - START_ANIMATION_VALUE + 1;

    private static final int DEFAULT_DURATION_TIME = 750;
    private static PanelState DEFAULT_PANEL_STATE = PanelState.COLLAPSED;
    private static boolean DEFAULT_ENABLE_COLLAPSE_ALL_PANEL = true;

    private static final int DEFAULT_LAYOUT_WEIGHT_SUM = 1000;
    private static final float DEFAULT_EXPANDED_PERCENT_SIZE = 45f;

    private static final int DEFAULT_LAYOUT_ORIENTATION = LinearLayout.VERTICAL;

    private static final int[] DEFAULT_ATTRS = new int[]{
            android.R.attr.orientation,
            android.R.attr.weightSum
    };

    private float layoutWeightSum = DEFAULT_LAYOUT_WEIGHT_SUM;
    private int layoutOrientation = DEFAULT_LAYOUT_ORIENTATION;
    private float collapsedSize, normalSize, expandedSize;
    private float expandedPercentSize = DEFAULT_EXPANDED_PERCENT_SIZE;

    private boolean enableCollapseAllPanel = DEFAULT_ENABLE_COLLAPSE_ALL_PANEL;

    private int duration_Time = DEFAULT_DURATION_TIME;

    private PanelState mPanelState = DEFAULT_PANEL_STATE;
    private View mExpandedPanel, mCollapsedPanel;
    private ValueAnimator mCurrentAnimator;
    private ValueAnimator mExpandAnimator, mCollapseAnimator;

    private ArrayList<IAnimationListener> animationListeners = new ArrayList<>();

    public SlidingLayout(Context context) {
        this(context, null);
    }

    public SlidingLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {

        if (attrs != null) {
            TypedArray defAttrs = context.obtainStyledAttributes(attrs, DEFAULT_ATTRS);

            if (defAttrs != null) {

                setLayoutOrientation(defAttrs.getInt(0, DEFAULT_LAYOUT_ORIENTATION));
                this.layoutWeightSum = defAttrs.getFloat(1, DEFAULT_LAYOUT_WEIGHT_SUM);

                defAttrs.recycle();

                TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SlidingLayoutStyles);

                if (ta != null) {
                    this.duration_Time = ta.getInt(R.styleable.SlidingLayoutStyles_duration, DEFAULT_DURATION_TIME);
                    this.expandedPercentSize = ta.getFloat(R.styleable.SlidingLayoutStyles_expandedPercentSize, DEFAULT_EXPANDED_PERCENT_SIZE);
                    this.enableCollapseAllPanel = ta.getBoolean(R.styleable.SlidingLayoutStyles_enableCollapseAllPanel, DEFAULT_ENABLE_COLLAPSE_ALL_PANEL);
                    ta.recycle();
                }
            }
        }

        this.mExpandAnimator = ValueAnimator.ofInt(START_ANIMATION_VALUE, END_ANIMATION_VALUE);
        this.mCollapseAnimator = ValueAnimator.ofInt(START_ANIMATION_VALUE, END_ANIMATION_VALUE);

        this.mExpandAnimator.addListener(this);
        this.mCollapseAnimator.addListener(this);

        this.mExpandAnimator.setDuration(this.duration_Time);
        this.mCollapseAnimator.setDuration(this.duration_Time);

        this.mExpandAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                onExpand(val);
            }
        });

        this.mCollapseAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                onCollapse(val);
            }
        });

    }

    private void onCollapse(int val) {
        float animationWeight = SlidingLayout.this.expandedSize - SlidingLayout.this.normalSize;
        float animationStep = animationWeight / ANIMATION_LENGTH;
        float offset = (val / (float) ANIMATION_LENGTH);
        final int childCount = SlidingLayout.this.getChildCount();

        if (offset == 1) {
            Log.d("setFoldFactor", "One:" + offset);
        }
        LayoutParams layoutParams = null;
        for (int i = 0; i < childCount; i++) {
            ViewGroup child = (ViewGroup) SlidingLayout.this.getChildAt(i);
            if (child == SlidingLayout.this.mCollapsedPanel) {
                layoutParams = (LayoutParams) child.getLayoutParams();
                layoutParams.weight = SlidingLayout.this.expandedSize - animationStep * val;
                notifyListenters(child, 1 - offset);
                break;
            }
        }

        if (layoutParams != null) {
            for (int i = 0; i < childCount; i++) {
                View child = SlidingLayout.this.getChildAt(i);
                if (child != SlidingLayout.this.mCollapsedPanel) {
                    LayoutParams otherLayoutParams = (LayoutParams) child.getLayoutParams();
                    otherLayoutParams.weight = (SlidingLayout.this.layoutWeightSum - layoutParams.weight) / (childCount - 1);
                }
            }
        }
        arrangeLayoutHeight();
        SlidingLayout.this.requestLayout();
    }

    private void onExpand(int val) {
        float sizeShift;

        if (SlidingLayout.this.mCollapsedPanel != null) {
            sizeShift = SlidingLayout.this.collapsedSize;
        } else {
            sizeShift = SlidingLayout.this.normalSize;
        }

        float animationExpandWeight = SlidingLayout.this.expandedSize - sizeShift;
        float animationStep = animationExpandWeight / ANIMATION_LENGTH;
        float offset = (val / (float) ANIMATION_LENGTH);
        final int childCount = SlidingLayout.this.getChildCount();

        LayoutParams layoutParams = null;
        for (int i = 0; i < childCount; i++) {
            ViewGroup child = (ViewGroup) SlidingLayout.this.getChildAt(i);
            if (child == SlidingLayout.this.mExpandedPanel) {
                layoutParams = (LayoutParams) child.getLayoutParams();
                layoutParams.weight = sizeShift + animationStep * val;
                notifyListenters(child, offset);
                break;
            }
        }

        if (layoutParams != null) {
            for (int i = 0; i < childCount; i++) {
                ViewGroup child = (ViewGroup) SlidingLayout.this.getChildAt(i);
                if (child != SlidingLayout.this.mExpandedPanel) {
                    if (child == SlidingLayout.this.mCollapsedPanel) {
                        LayoutParams collapsableLayoutParams = (LayoutParams) child.getLayoutParams();
                        collapsableLayoutParams.weight = SlidingLayout.this.expandedSize - val * animationStep;
                        for (int j = 0; j < child.getChildCount(); j++) {
                            View foldingChild = child.getChildAt(j);
                            if (foldingChild instanceof IAnimationListener) {
                                IAnimationListener fl = (IAnimationListener) foldingChild;
                                fl.onAnimationProgress(1 - offset);
                            }
                        }
                    } else {
                        if (SlidingLayout.this.mCollapsedPanel == null) {
                            LayoutParams otherLayoutParams = (LayoutParams) child.getLayoutParams();
                            otherLayoutParams.weight = (SlidingLayout.this.layoutWeightSum - layoutParams.weight) / (childCount - 1);
                        }
                    }
                }

            }
        }
        arrangeLayoutHeight();
        SlidingLayout.this.requestLayout();
    }

    private void arrangeLayoutHeight() {
        float total;
        LayoutParams layoutParams;
        total = this.layoutWeightSum;
        for (int i = 0; i < SlidingLayout.this.getChildCount(); i++) {
            View child = SlidingLayout.this.getChildAt(i);
            layoutParams = (LayoutParams) child.getLayoutParams();
            if (i != (SlidingLayout.this.getChildCount() - 1)) {
                total -= layoutParams.weight;
            } else {
                layoutParams.weight = total;
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        final int childCount = getChildCount();

        if (this.expandedPercentSize < (100.0f / childCount)) {
            this.expandedSize = (this.layoutWeightSum / childCount) * 2.0f;
        } else {
            this.expandedSize = this.layoutWeightSum * this.expandedPercentSize / 100.0f;
        }

        this.collapsedSize = (this.layoutWeightSum - this.expandedSize) / (childCount - 1);
        this.normalSize = this.layoutWeightSum / childCount;

        float childWeight = this.layoutWeightSum / childCount;

        float restWeight = this.layoutWeightSum;
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            LayoutParams lP = (i != childCount - 1) ?
                    this.generateChildLayoutParams(childWeight) :
                    this.generateChildLayoutParams(restWeight);
            child.setLayoutParams(lP);
            restWeight -= childWeight;
        }
        this.setClickListeners(this);
    }

    private LayoutParams generateChildLayoutParams(float childWeight) {
        LayoutParams lP;
        switch (this.layoutOrientation) {
            case LinearLayout.HORIZONTAL:
                lP = new LayoutParams(0, LayoutParams.MATCH_PARENT, childWeight);
                break;
            default:
                lP = new LayoutParams(LayoutParams.MATCH_PARENT, 0, childWeight);
        }
        return lP;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        for (IAnimationListener listener : animationListeners) {
            listener.onRemoveFromListener();
        }
        animationListeners.clear();
        this.setClickListeners(null);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    public void onClick(View v) {
        switch (this.mPanelState) {
            case EXPANDED:
                if (v == this.mExpandedPanel) {
                    if (this.enableCollapseAllPanel) {
                        this.mCurrentAnimator = this.mCollapseAnimator;
                        setCollapsedPanel(v);
                        setClickListeners(null);
                        this.mCurrentAnimator.start();
//                        useHandler(-10);
                    }
                } else {
                    this.mCurrentAnimator = this.mExpandAnimator;
                    setExpandedPanel(v);
                    setClickListeners(null);
                    this.mCurrentAnimator.start();
//                    useHandler(10);
                }
                break;
            case COLLAPSED:
                setClickListeners(null);
                this.mCurrentAnimator = this.mExpandAnimator;
                setExpandedPanel(v);
//                useHandler(10);
                this.mCurrentAnimator.start();
                break;
        }
    }

    private void setClickListeners(OnClickListener activity) {
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            getChildAt(i).setOnClickListener(activity);
        }
    }

    private void setExpandedPanel(View v) {
        if (isChild(v)) {
            if (this.mExpandedPanel != null) {
                this.mCollapsedPanel = this.mExpandedPanel;
            } else {
                this.mCollapsedPanel = null;
            }
            this.mExpandedPanel = v;

        } else {
            this.mExpandedPanel = null;
        }
    }

    private void setCollapsedPanel(View v) {
        if (isChild(v)) {
            this.mCollapsedPanel = v;
            this.mExpandedPanel = null;
        } else {
            this.mCollapsedPanel = null;
        }
    }

    private boolean isChild(View v) {
        for (int i = 0; i < SlidingLayout.this.getChildCount(); i++) {
            View child = SlidingLayout.this.getChildAt(i);
            if (v.getId() == child.getId()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onAnimationStart(Animator animation) {
        Log.d("anim", "onAnimationStart");
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        Log.d("anim", "onAnimationEnd");
        if (this.mCurrentAnimator != null) {
            if (this.mCurrentAnimator == this.mExpandAnimator) {
                this.mPanelState = PanelState.EXPANDED;
            }
            if (this.mCurrentAnimator == this.mCollapseAnimator) {
                this.mPanelState = PanelState.COLLAPSED;
                this.mExpandedPanel = null;
            }
        }
        this.mCurrentAnimator = null;
        this.mCollapsedPanel = null;
        this.setClickListeners(this);
    }

    @Override
    public void onAnimationCancel(Animator animation) {
    }

    @Override
    public void onAnimationRepeat(Animator animation) {
    }


    public void setLayoutOrientation(int layoutOrientation) {
        this.layoutOrientation = layoutOrientation;
        super.setOrientation(layoutOrientation);
    }

    public void setDuration_Time(int duration_Time) {
        this.duration_Time = duration_Time;
        this.mExpandAnimator.setDuration(this.duration_Time);
        this.mCollapseAnimator.setDuration(this.duration_Time);
    }

    public void setEnableCollapseAllPanel(boolean enableCollapseAllPanel) {
        this.enableCollapseAllPanel = enableCollapseAllPanel;
    }

    public void useHandler(int shift) {
        mHandler = new Handler();
        if (shift > 0) {
            _offset = 0;
            _delta = 5;
        } else {
            _offset = 0;
            _delta = -5;
        }
        mHandler.postDelayed(mRunnable, 200);
    }

    private Runnable mRunnable = new Runnable() {

        @Override
        public void run() {
            if ((_offset > 100) || (_offset < 0)){
                if (_delta > 0) {
                    SlidingLayout.this.mPanelState = PanelState.EXPANDED;
                }
                if (_delta < 0) {
                    SlidingLayout.this.mPanelState = PanelState.COLLAPSED;
                    SlidingLayout.this.mExpandedPanel = null;
                }

                SlidingLayout.this.mCurrentAnimator = null;
                SlidingLayout.this.mCollapsedPanel = null;
                SlidingLayout.this.setClickListeners(SlidingLayout.this);
                return;
            }
            if (_delta > 0) {
                _offset += _delta;
                onExpand(_offset);
            } else {
                _offset -= _delta;
                onCollapse(_offset);
            }
            mHandler.postDelayed(mRunnable, 100);
        }
    };

    @Override
    public void addListener(IAnimationListener listener) {
        if (listener != null) {
            this.animationListeners.add(listener);
        }
    }

    @Override
    public void removeListener(IAnimationListener listener) {
        if (listener != null) {
            listener.onRemoveFromListener();
            this.animationListeners.remove(listener);
        }
    }

    @Override
    public void notifyListenters(ViewGroup child, float value) {
        for (IAnimationListener listener : animationListeners) {
            if (listener.hasParent((View) listener, child)) {
                listener.onAnimationProgress(value);
            }
        }
    }


}
