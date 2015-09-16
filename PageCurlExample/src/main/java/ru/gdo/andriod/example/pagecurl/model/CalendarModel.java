package ru.gdo.andriod.example.pagecurl.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;

import ru.gdo.andriod.example.pagecurl.R;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 16.09.15.
 */

public class CalendarModel<W extends CalendarDataWrapper> extends IModel<W> {

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

    public static String repeat(String str, int times) {
        return new String(new char[times]).replace("\0", str);
    }

    @Override
    public void fillContent() {
        super.fillContent();

        Calendar c = (Calendar) this.dataWrapper.getValue().clone();
        c.add(Calendar.DAY_OF_MONTH, index);

        this.mTextView.setText(c.getTime().toString());
    }

}
