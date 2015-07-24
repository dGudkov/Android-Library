package ru.gdo.android.example.horizontalscrollview.data;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import ru.gdo.android.example.horizontalscrollview.R;

import java.util.Calendar;
import java.util.Locale;

import ru.gdo.android.library.horizontalscrollview.exception.ScrollViewData;

/**
 * @author Danil Gudkov <d_n_l@mail.ru>
 * @copyrights Danil Gudkov, 2015
 * @since 23.07.15.
 */

public class ViewData_Calendar extends ScrollViewData {

    private String dateText, monthText, dayWeekText;
    private TextView textDate, textMonth, textDayWeek;
    private int textDateId, textMonthId, textDayWeekId;

//    private String text;

    public ViewData_Calendar(Calendar date, int viewId) {
        super(viewId);
        this.dateText = String.valueOf(date.get(Calendar.DAY_OF_MONTH));
        this.monthText = date.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
        this.dayWeekText = date.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.US);

        this.textDateId = R.id.calendarDate;
        this.textMonthId = R.id.calendarMonth;
        this.textDayWeekId = R.id.calendarDayWeek;
    }

    @Override
    public View initViews(Context context) {
        View view = super.initViews(context);
        if (view != null) {
            if (this.textDate == null) {
                this.textDate = (TextView) view.findViewById(this.textDateId);
            }
            if (this.textDate != null) {
                this.textDate.setText(this.dateText);
            }
            if (this.textMonth == null) {
                this.textMonth = (TextView) view.findViewById(this.textMonthId);
            }
            if (this.textMonth != null) {
                this.textMonth.setText(this.monthText);
            }
            if (this.textDayWeek == null) {
                this.textDayWeek = (TextView) view.findViewById(this.textDayWeekId);
            }
            if (this.textDayWeek != null) {
                this.textDayWeek.setText(this.dayWeekText);
            }
        }
        return view;
    }

    @Override
    public View cloneView() {
        View view = super.cloneView();
        if (view != null) {
            TextView textDate = (TextView) view.findViewById(this.textDateId);
            if (textDate != null) {
                textDate.setText(this.dateText);
            }
            TextView textMonth = (TextView) view.findViewById(this.textMonthId);
            if (textMonth != null) {
                textMonth.setText(this.monthText);
            }
            TextView textDayWeek = (TextView) view.findViewById(this.textDayWeekId);
            if (textDayWeek != null) {
                textDayWeek.setText(this.dayWeekText);
            }
        }
        return view;
    }

}
