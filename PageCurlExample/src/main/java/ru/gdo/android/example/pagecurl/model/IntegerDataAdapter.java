package ru.gdo.android.example.pagecurl.model;

import android.content.Context;

import ru.gdo.android.library.pagecurl.model.DataAdapter;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 14.09.15.
 */

public class IntegerDataAdapter extends DataAdapter<Integer, IntegerDataModel> {

    public IntegerDataAdapter(
            Context context,
            int modelCount) throws IllegalAccessException, InstantiationException {
        super(context, modelCount, IntegerDataModel.class);
        this.mValue = 0;
    }


    public void increment(int val) {
        this.mValue += val;
    }

    public void decrement(int val) {
        this.mValue -= val;
    }
}
