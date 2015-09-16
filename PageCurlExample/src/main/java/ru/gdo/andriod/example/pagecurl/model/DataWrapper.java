package ru.gdo.andriod.example.pagecurl.model;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 14.09.15.
 */

public abstract class DataWrapper<T> {

    protected T mValue;

    public DataWrapper() {
        this.mValue = null;
    }

    public T getValue() {
        return mValue;
    }

    public abstract void increment(int val);

    public abstract void decrement(int val);

}
