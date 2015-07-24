package com.gdo.android.example.horizontalscrollview.data;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.gdo.android.example.horizontalscrollview.R;

import ru.gdo.android.library.horizontalscrollview.exception.ScrollViewData;

/**
 * @author Danil Gudkov <d_n_l@mail.ru>
 * @copyrights Danil Gudkov, 2015
 * @since 23.07.15.
 */

public class ViewData_Images extends ScrollViewData {

    private ImageView imageView;
    private int imageViewId;
    private int imageResourceId;

    public ViewData_Images(int imageResourceId, int viewId) {
        super(viewId);
        this.imageResourceId = imageResourceId;

        this.imageViewId = R.id.imageView;
    }

    @Override
    public View initViews(Context context) {
        View view = super.initViews(context);
        if (view != null) {
            if (this.imageView == null) {
                this.imageView = (ImageView) view.findViewById(this.imageViewId);
            }
            if (this.imageView != null) {
                this.imageView.setImageResource(this.imageResourceId);
            }
            this.view.setTag(this);
        }
        return view;
    }

    @Override
    public View cloneView() {
        View view = super.cloneView();
        if (view != null) {
            ImageView imageView = (ImageView) view.findViewById(this.imageViewId);
            if (imageView != null) {
                imageView.setImageResource(this.imageResourceId);
            }
        }
        return view;
    }
}
