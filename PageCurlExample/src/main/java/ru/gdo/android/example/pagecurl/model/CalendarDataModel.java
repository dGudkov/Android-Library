package ru.gdo.android.example.pagecurl.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

import ru.gdo.android.example.pagecurl.R;
import ru.gdo.android.library.pagecurl.model.DataModel;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 16.09.15.
 */

public class CalendarDataModel<W extends CalendarDataAdapter> extends DataModel<W> {

    private TextView mDataTextView;
    private TextView mMonthTextView;
    private TextView mWeeklyTextView;
    private Calendar calendar = Calendar.getInstance();

    @Override
    public View getView(Context context) {
        if (this.mView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            this.mView = inflater.inflate(R.layout.calendarmodel_layout, null);

            this.mContent = (RelativeLayout) this.mView.findViewById(R.id.content);
            this.mContentOverlay = (RelativeLayout) this.mView.findViewById(R.id.contentOverlay);

            this.mDataTextView = (TextView) this.mContent.findViewById(R.id.textdate);
            this.mMonthTextView = (TextView) this.mContent.findViewById(R.id.textmonth);
            this.mWeeklyTextView = (TextView) this.mContent.findViewById(R.id.textweekday);

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

        this.calendar.setTime(this.dataWrapper.getValue().getTime());
        this.calendar.add(Calendar.DAY_OF_MONTH, index);

        this.mDataTextView.setText(String.valueOf(this.calendar.get(Calendar.DAY_OF_MONTH)));
        this.mMonthTextView.setText(this.calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US));
        this.mWeeklyTextView.setText(this.calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.US));

    }

}
