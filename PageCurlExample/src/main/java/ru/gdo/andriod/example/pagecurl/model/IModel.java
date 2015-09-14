package ru.gdo.andriod.example.pagecurl.model;

import android.content.Context;
import android.view.View;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 04.09.15.
 */

public interface IModel {

    View getView(int position, Context context);

    boolean executeRequest(Thread thread);

    void setIndex(int index);

    void fillContent();

    void setValue(int value);
}
