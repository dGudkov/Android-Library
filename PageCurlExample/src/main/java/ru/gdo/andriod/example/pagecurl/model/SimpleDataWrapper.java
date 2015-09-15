package ru.gdo.andriod.example.pagecurl.model;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 14.09.15.
 */

public class SimpleDataWrapper implements DataWrapper {
    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void increment(int val) {
        this.value += val;
    }

    public void decrement(int val) {
        this.value -= val;
    }
}
