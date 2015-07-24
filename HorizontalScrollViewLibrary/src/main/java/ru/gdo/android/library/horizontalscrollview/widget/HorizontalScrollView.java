package ru.gdo.android.library.horizontalscrollview.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import ru.gdo.android.library.horizontalscrollview.R;
import ru.gdo.android.library.horizontalscrollview.adapter.HorizontalListViewAdapter;
import ru.gdo.android.library.horizontalscrollview.exception.ZeroChildException;

/**
 * @author Danil Gudkov <d_n_l@mail.ru>
 * @copyrights Danil Gudkov, 2015
 * @since 23.07.15.
 */

public class HorizontalScrollView extends android.widget.HorizontalScrollView {

    public enum InitialState {
        ALIGN_LEFT,
        ALIGN_CENTER,
        ALIGN_RIGHT
    }

    /**
     * Default initial state for the component
     */
    private final static InitialState DEFAULT_INITIAL_STATE = InitialState.ALIGN_LEFT;

    private InitialState mInitialState = DEFAULT_INITIAL_STATE;

    private Context mContext;
    private Integer mPrevIndex = null;
    private int mInitialIndex = -1;
    private boolean mFirstLayout = true;
    private HorizontalListViewAdapter mAdapter;

    private int mActiveColor = Color.parseColor("#000000"), mPassiveColor = Color.parseColor("#FFFFFF");

    public HorizontalScrollView(Context context) {
        this(context, null);
    }

    public HorizontalScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.HorizontalScrollView);

            if (ta != null) {
                this.mInitialState = InitialState.values()[ta.getInt(R.styleable.HorizontalScrollView_initialState, DEFAULT_INITIAL_STATE.ordinal())];
                ta.recycle();
            }

        }
        initView(context);
    }

    private void initView(Context context) {
        this.mContext = context;
        this.setSmoothScrollingEnabled(true);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mFirstLayout = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mFirstLayout = true;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (this.mFirstLayout) {
            this.mFirstLayout = false;
            if (this.mInitialIndex == -1) {
                this.mInitialIndex = this.mAdapter.setCenter(this.mInitialState);
            } else {
                this.mAdapter.setCenter(this.mInitialIndex);
            }
        }
    }

    public void setAdapter(HorizontalListViewAdapter adapter) {
        try {
            this.mFirstLayout = true;
            this.mPrevIndex = null;
            this.mAdapter = adapter;
            this.mInitialIndex = -1;
            fillViewWithAdapter();
        } catch (ZeroChildException e) {
            e.printStackTrace();
        }
    }

    private void fillViewWithAdapter() throws ZeroChildException {
        if (this.getChildCount() == 0) {
            throw new ZeroChildException(
                    "CenterLockHorizontalScrollView must have one child");
        }
        if (this.mAdapter == null) {
            return;
        }

        ViewGroup parent = (ViewGroup) getChildAt(0);

        parent.removeAllViews();

        for (int i = 0; i < this.mAdapter.getCount(); i++) {
            parent.addView(this.mAdapter.getView(i, null, parent));
        }
        parent.requestLayout();
    }

    public boolean setCenter(int index) {
        if (!this.mFirstLayout) {
            ViewGroup parent = (ViewGroup) getChildAt(0);
            if (parent != null) {
                if (this.mPrevIndex != null) {
                    View preView = parent.getChildAt(this.mPrevIndex);
                    preView.setBackgroundColor(this.mPassiveColor);
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) preView.getLayoutParams();
                    lp.setMargins(5, 5, 5, 5);
                    preView.setLayoutParams(lp);
                }

                View view = parent.getChildAt(index);
                view.setBackgroundColor(this.mActiveColor);
                LinearLayout.LayoutParams lp1 = (LinearLayout.LayoutParams) view.getLayoutParams();
                lp1.setMargins(0, 0, 0, 0);
                view.setLayoutParams(lp1);

                int screenWidth = ((Activity) this.mContext).getWindowManager()
                        .getDefaultDisplay().getWidth();

                int scrollX = (view.getLeft() - (screenWidth / 2))
                        + (view.getWidth() / 2);
                this.smoothScrollTo(scrollX, 0);
                this.mPrevIndex = index;
                return true;
            }
        }
        return false;
    }

    public void setBorderColors(int activeColor, int passiveColor) {
        this.mActiveColor = getResources().getColor(activeColor);
        this.mPassiveColor = getResources().getColor(passiveColor);
    }

}
