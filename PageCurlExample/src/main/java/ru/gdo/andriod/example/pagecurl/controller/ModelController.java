package ru.gdo.andriod.example.pagecurl.controller;

import android.content.Context;
import android.view.View;

import ru.gdo.andriod.example.pagecurl.interfaces.IAdapter;
import ru.gdo.andriod.example.pagecurl.model.DataWrapper;
import ru.gdo.andriod.example.pagecurl.model.IModel;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 04.09.15.
 */

public class ModelController<W extends DataWrapper, M extends IModel<W>> implements IAdapter {

//    private final W dataWrapper;

    private Context mContext;

    public ModelController(
            Context context,
            int modelCount,
            Class<W> typeClassWrapper,
            Class<M> typeClassModel) throws IllegalAccessException, InstantiationException {
        this.mContext = context;

//        this.dataWrapper = typeClassWrapper.newInstance();
//        this.dataWrapper.init(context, modelCount);
//
//        this.mTypeClassModel = typeClassModel;

    }

    @Override
    public void refreshHistoricallyData(int shift) {
//        if (this.dataWrapper.refreshModels(shift)) {
//            this.dataWrapper.execute(shift);
//        }
    }

    public void interrupt() {
//        this.dataWrapper.interrupt();
    }

    @Override
    public int getIndex() {
//        return this.dataWrapper.getIndex();
        return 0;
    }

    public void setContext(Context context) {
        this.mContext = context;
//        this.dataWrapper.setContext(context);
    }

    @Override
    public int getCount() {
//        return this.dataWrapper.getCount();
        return 0;
    }

    @Override
    public View getView(int position) {
//        return this.dataWrapper.getView(position);
        return null;
    }

}