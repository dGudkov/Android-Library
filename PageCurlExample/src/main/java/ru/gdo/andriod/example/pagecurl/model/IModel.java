package ru.gdo.andriod.example.pagecurl.model;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 04.09.15.
 */

public abstract class IModel<T extends DataWrapper> {

    protected T dataWrapper;
    protected int index;
    protected View mView;
    protected RelativeLayout mContent;
    protected RelativeLayout mContentOverlay;


    public IModel() {
        this.index = 0;
        this.dataWrapper = null;
        this.mView = null;
    }

    public void init(int index, Context context, T dataWrapper) {
        this.index = index;
        this.dataWrapper = dataWrapper;
        this.getView(context);
    }


    public abstract View getView(Context context);

    public View getView() {
        return this.mView;
    }

    public void setView(View view) {
        this.mView = view;
    }

    public boolean executeRequest(Thread thread, int index) {
        this.index = index;
        return !thread.isInterrupted();
    }

    public void fillContent() {
        if (this.mContent != null)
            this.mContent.setVisibility(View.VISIBLE);

        if (this.mContentOverlay != null)
            this.mContentOverlay.setVisibility(View.INVISIBLE);
    }

    public void prepareContent() {
        if (this.mContent != null)
            this.mContent.setVisibility(View.INVISIBLE);

        if (this.mContentOverlay != null)
            this.mContentOverlay.setVisibility(View.VISIBLE);

    }

    public int getIndex() {
        return this.index;
    }

    public void assingModel(IModel iModel) {
        this.fillContent();
    }

}
