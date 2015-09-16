package ru.gdo.android.example.horizontalscrollview.data;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import ru.gdo.android.example.horizontalscrollview.R;

import ru.gdo.android.library.horizontalscrollview.data.ScrollViewData;

/**
 * @author Danil Gudkov <d_n_l@mail.ru>
 * @copyrights Danil Gudkov, 2015
 * @since 23.07.15.
 */

public class ViewData_Alphabet extends ScrollViewData {

    private TextView textView;
    private int textViewId;

    private String text;

    public ViewData_Alphabet(String text, int viewId) {
        super(viewId);
        this.text = text;
        this.textViewId = R.id.alphabet;
    }

    @Override
    public View initViews(Context context) {
        View view = super.initViews(context);
        if (view != null) {
            if (this.textView == null) {
                this.textView = (TextView) view.findViewById(this.textViewId);
            }
            if (this.textView != null) {
                this.textView.setText(this.text);
            }
        }
        return view;
    }

    @Override
    public View cloneView () {
        View view = super.cloneView();
        if (view != null) {
            TextView textView = (TextView) view.findViewById(this.textViewId);
            if (textView != null) {
                textView.setText(this.text);
            }
        }
        return view;
    }
}
