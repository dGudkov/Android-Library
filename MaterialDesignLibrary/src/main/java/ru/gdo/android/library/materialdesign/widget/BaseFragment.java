package ru.gdo.android.library.materialdesign.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.gdo.android.library.materialdesign.exception.ToolBarActivityException;
import ru.gdo.android.library.materialdesign.interfaces.IToolBarInterface;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 23.09.15.
 */

public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    protected final String TITLE_ID = "titleId";

    protected int mLayoutId = -1;
    protected Context mContext;
    protected int mTitleTextId = -1;
    protected Toolbar mToolBar;
    protected IToolBarInterface mToolBarInterface;

    public BaseFragment() {
        this.mLayoutId = -1;
        this.mTitleTextId = -1;
    }

    public static BaseFragment newInstance(Class<? extends BaseFragment> clazz,
                                           Context context,
                                           Toolbar toolBar,
                                           IToolBarInterface onToolBarClickListener) throws ToolBarActivityException {
        try {
            BaseFragment fragment = clazz.newInstance();
            fragment.init(context, toolBar, onToolBarClickListener);
            return fragment;
        } catch (Exception e) {
            throw new ToolBarActivityException("Error fragment initialization", e);
        }
    }

    public void init(Context context, Toolbar toolBar, IToolBarInterface onToolBarClickListener) {
        this.setContext(context);
        this.setToolBar(toolBar);
        this.setToolBarInterface(onToolBarClickListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (this.mLayoutId == -1) {
            throw new ToolBarActivityException("Invalid layoutId");
        }
        View rootView = inflater.inflate(this.mLayoutId, container, false);

        if (savedInstanceState != null) {
            this.mTitleTextId = savedInstanceState.getInt(TITLE_ID);
        }

        this.setTitleText();

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(TITLE_ID, this.mTitleTextId);
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public void setToolBar(Toolbar toolBar) {
        this.mToolBar = toolBar;
    }

    public void setToolBarInterface(IToolBarInterface toolBarInterface) {
        this.mToolBarInterface = toolBarInterface;
    }

    protected void onToolBarClick(View view) {
        if (this.mToolBarInterface != null) {
            this.mToolBarInterface.onToolBarClick(view);
        }
    }

    protected void setToolBarTitle(String title) {
        if (this.mToolBarInterface != null) {
            this.mToolBarInterface.setToolBarTitle(title);
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

    protected void setTitleText() {
        if (this.mTitleTextId != -1) {
            this.setToolBarTitle(this.mContext.getString(this.mTitleTextId));
        }
    }

    public void setTitleTextId(int resourceId) {
        this.mTitleTextId = resourceId;
    }

}
