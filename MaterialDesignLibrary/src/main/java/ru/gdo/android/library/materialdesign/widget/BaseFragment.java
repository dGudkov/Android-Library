package ru.gdo.android.library.materialdesign.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.gdo.android.library.materialdesign.exception.ToolBarActivityException;
import ru.gdo.android.library.materialdesign.interfaces.IOnToolBarClickListener;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 23.09.15.
 */

public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    protected Context mContext;
    protected String mTitleText;
    protected Toolbar mToolBar;
    protected IOnToolBarClickListener mOnToolBarClickListener;

    public BaseFragment() {
        // Required empty public constructor
    }

    public static BaseFragment newInstance(Class<? extends BaseFragment> clazz,
                                           Context context,
                                           Toolbar toolBar,
                                           IOnToolBarClickListener onToolBarClickListener) throws ToolBarActivityException {
        try {
            BaseFragment fragment = clazz.newInstance();
            fragment.init(context, toolBar, onToolBarClickListener);
            return fragment;
        } catch (Exception e) {
            throw new ToolBarActivityException("Error fragment initialization", e);
        }
    }

    public void init(Context context, Toolbar toolBar, IOnToolBarClickListener onToolBarClickListener) {
        this.setContext(context);
        this.setToolBar(toolBar);
        this.setOnToolBarClickListener(onToolBarClickListener);
        this.setTitleText();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public String getTitleText() {
        return mTitleText;
    }

    public void setToolBar(Toolbar toolBar) {
        this.mToolBar = toolBar;
    }

    public void setOnToolBarClickListener(IOnToolBarClickListener onToolBarClickListener) {
        this.mOnToolBarClickListener = onToolBarClickListener;
    }

    protected void onToolBarClick(View view) {
        if (this.mOnToolBarClickListener != null) {
            this.mOnToolBarClickListener.onToolBarClick(view);
        }
    }

    @Override
    public void onClick(View v) {
        if (isToolBarComponent(v)) {
            this.onToolBarClick(v);
        }
    }

    protected boolean isToolBarComponent(View v) {
        return false;
    }

    public abstract void setTitleText();

}
