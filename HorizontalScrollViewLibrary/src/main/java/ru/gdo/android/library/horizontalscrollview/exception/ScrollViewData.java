package ru.gdo.android.library.horizontalscrollview.exception;

import android.content.Context;
import android.view.View;

import ru.gdo.android.library.horizontalscrollview.interfaces.IScrollViewData;

/**
 * @author Danil Gudkov <d_n_l@mail.ru>
 * @copyrights Danil Gudkov, 2015
 * @since 23.07.15.
 */

public class ScrollViewData implements IScrollViewData {

    protected View view;
    protected int viewId;
    protected  Context mContext;


    public ScrollViewData(int viewId) {
        this.viewId = viewId;
    }

    @Override
    public View initViews(Context context) {
        this.mContext = context;
        if (this.view == null) {
            this.view = inflateView();
        }
        if (this.view != null) {
            this.view.setTag(this);
        }
        return this.view;
    }

    @Override
    public View cloneView() {
        return inflateView();
    }

    private View inflateView() {
        return View.inflate(this.mContext, this.viewId, null);
    }
}
