package ru.gdo.andriod.example.pagecurl.model;

import android.content.Context;

import java.util.Calendar;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 16.09.15.
 */

public class CalendarDataWrapper extends DataWrapper<Calendar, CalendarModel> {

    public CalendarDataWrapper (
            Context context,
            int modelCount) throws IllegalAccessException, InstantiationException {
        super(context, modelCount, CalendarModel.class);
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
