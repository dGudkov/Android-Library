package ru.gdo.android.example.pagecurl.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ru.gdo.android.example.pagecurl.R;
import ru.gdo.android.library.pagecurl.model.DataModel;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 04.09.15.
 */

public class IntegerDataModel extends DataModel<IntegerDataAdapter> {

    private TextView mTextView;

    @Override
    public View getView(Context context) {
        if (this.mView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            this.mView = inflater.inflate(R.layout.integermodel_layout, null);

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
    public void fillContent() {
        super.fillContent();
        this.mTextView.setText(String.valueOf(index + this.dataWrapper.getValue()));
    }

}
