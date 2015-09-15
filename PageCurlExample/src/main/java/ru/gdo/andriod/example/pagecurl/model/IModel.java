package ru.gdo.andriod.example.pagecurl.model;

import android.content.Context;
import android.view.View;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 04.09.15.
 */

public interface IModel {

    View getView(Context context);

    View getView();

    void setView(View mView);

    boolean executeRequest(Thread thread, int index);

    void fillContent();

    void prepareContent();

    int getIndex();

    void assingModel(IModel iModel);

}
