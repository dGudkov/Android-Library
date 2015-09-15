package ru.gdo.andriod.example.pagecurl.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ru.gdo.andriod.example.pagecurl.R;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 04.09.15.
 */

public class SimpleModel implements IModel {

    private SimpleDataWrapper dataWrapper;
    private int index;
    private View mView;
    private RelativeLayout mContent;
    private RelativeLayout mContentOverlay;
    private TextView mTextView;


    public SimpleModel(int index, Context context, DataWrapper dataWrapper) {
        this.index = index;
        this.dataWrapper = (SimpleDataWrapper) dataWrapper;
        this.getView(context);
    }

    @Override
    public View getView(Context context) {
        if (this.mView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            this.mView = inflater.inflate(R.layout.simplemodel_layout, null);

            this.mContent = (RelativeLayout) this.mView.findViewById(R.id.content);
            this.mContentOverlay = (RelativeLayout) this.mView.findViewById(R.id.contentOverlay);

            this.mTextView = (TextView) this.mContent.findViewById(R.id.textView);

            if (this.mContent != null)
                this.mContent.setVisibility(View.INVISIBLE);
            if (this.mContentOverlay != null)
                this.mContentOverlay.setVisibility(View.VISIBLE);
        }

        return this.mView;
    }

    @Override
    public View getView() {
        return this.mView;
    }

    @Override
    public void setView(View view) {
        this.mView = view;

    }

    @Override
    public boolean executeRequest(Thread thread, int index) {
        this.index = index;
        return !Thread.currentThread().isInterrupted();
    }

    public static String repeat(String str, int times) {
        return new String(new char[times]).replace("\0", str);
    }

    @Override
    public void fillContent() {
        if (this.mContent != null)
            this.mContent.setVisibility(View.VISIBLE);

        if (this.mContentOverlay != null)
            this.mContentOverlay.setVisibility(View.INVISIBLE);

        this.mTextView.setText(repeat(String.valueOf(index + this.dataWrapper.getValue()) + "_", 20));
    }

    @Override
    public void prepareContent() {
        if (this.mContent != null)
            this.mContent.setVisibility(View.INVISIBLE);

        if (this.mContentOverlay != null)
            this.mContentOverlay.setVisibility(View.VISIBLE);

    }

    @Override
    public int getIndex() {
        return this.index;
    }

    @Override
    public void assingModel(IModel iModel) {
//        this.index = iModel.getIndex();
//        View view = iModel.getView();
//        iModel.setView(this.mView);
//        this.mView = view;
        this.fillContent();
    }

}
