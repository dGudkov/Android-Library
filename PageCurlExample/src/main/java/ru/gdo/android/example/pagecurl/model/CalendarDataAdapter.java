package ru.gdo.android.example.pagecurl.model;

import android.content.Context;

import java.util.Calendar;

import ru.gdo.android.library.pagecurl.model.DataAdapter;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 16.09.15.
 */

public class CalendarDataAdapter extends DataAdapter<Calendar, CalendarDataModel> {

    public CalendarDataAdapter(
            Context context,
            int modelCount) throws IllegalAccessException, InstantiationException {
        super(context, modelCount, CalendarDataModel.class);
        this.mValue = Calendar.getInstance();;
    }

    @Override
    public void increment(int val) {
        this.changeDate(Calendar.DAY_OF_MONTH, val);
    }

    @Override
    public void decrement(int val) {
        this.changeDate(Calendar.DAY_OF_MONTH, -val);
    }

    private void changeDate(int field, int val) {
        this.mValue.add(field, val);
    }

}
