package ru.gdo.andriod.example.pagecurl.model;

import android.content.Context;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 14.09.15.
 */

public class IntegerDataWrapper extends DataWrapper<Integer, IntegerModel> {

    public IntegerDataWrapper(
            Context context,
            int modelCount) throws IllegalAccessException, InstantiationException {
        super(context, modelCount, IntegerModel.class);
        this.mValue = 0;
    }


    public void increment(int val) {
        this.mValue += val;
    }

    public void decrement(int val) {
        this.mValue -= val;
    }
}
