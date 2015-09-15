package ru.gdo.andriod.example.pagecurl.controller;

import android.content.Context;
import android.view.View;

import ru.gdo.andriod.example.pagecurl.handler.MessageHandler;
import ru.gdo.andriod.example.pagecurl.interfaces.IAdapter;
import ru.gdo.andriod.example.pagecurl.interfaces.IHandlerSetData;
import ru.gdo.andriod.example.pagecurl.model.IModel;
import ru.gdo.andriod.example.pagecurl.model.SimpleDataWrapper;
import ru.gdo.andriod.example.pagecurl.model.SimpleModel;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 04.09.15.
 */

public class ModelController implements IAdapter, IHandlerSetData {

    private static int MODEL_COUNT = 4;
    private final MessageHandler mHandler;
    private final SimpleDataWrapper dataWrapper;

    private Context mContext;
    private IModel[] mModel;
    private int mModelsCount;
    private int mIndex;
    private Thread thread;

    public ModelController() {
        this(null, MODEL_COUNT);
    }

    public ModelController(Context context) {
        this(context, MODEL_COUNT);
    }

    public ModelController(Context context, int modelCount) {
        this.mContext = context;
        if (modelCount < 3) {
            modelCount = MODEL_COUNT;
        }
        this.mModel = initModel(modelCount);
        this.mModelsCount = this.mModel.length;
        if (modelCount == 3) {
            this.mIndex = 1;
        } else {
            this.mIndex = (this.mModelsCount / 2);
        }
        this.mHandler = new MessageHandler(this);
        this.dataWrapper = new SimpleDataWrapper();
        this.dataWrapper.setValue(0);
    }

    @Override
    public void refreshHistoricallyData(int shift) {
        if (this.refreshModels(shift)) {
            this.execute(shift);
            int modelIndex = this.getIndex();
        }
    }

    private void execute(final int shift) {
        if (thread != null) {
            thread.interrupt();
            try {
                thread.join();
            } catch (InterruptedException ignored) {
            }
        }

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.sendMessage(MessageHandler.PRE_EXECUTE, null);
                if (shift > 0) {

                    int index = getIndex() + 1;
                    IModel model = getModelByIndex(index);
                    if (model.executeRequest(Thread.currentThread(), index)) {
                        if (!Thread.currentThread().isInterrupted()) {
                            mHandler.sendMessage(MessageHandler.POST_RIGHT_MODEL, null);
                        }
                    }
                    for (int i = index + 1; i < ModelController.this.mModelsCount; i++) {
                        model = getModelByIndex(i);
                        model.executeRequest(Thread.currentThread(), i);
                        if (!Thread.currentThread().isInterrupted()) {
                            mHandler.sendMessage(MessageHandler.POST_FILL_MODEL, model);
                        }
                    }
                } else if (shift == 0) {
                    int index = getIndex();

                    IModel model = getModelByIndex(index);
                    if (model.executeRequest(Thread.currentThread(), index)) {
                        if (!Thread.currentThread().isInterrupted()) {
                            mHandler.sendMessage(MessageHandler.POST_MIDDLE_MODEL, null);
                        }
                    }

                    model = getModelByIndex(index - 1);
                    if (model.executeRequest(Thread.currentThread(), index - 1)) {
                        if (!Thread.currentThread().isInterrupted()) {
                            mHandler.sendMessage(MessageHandler.POST_LEFT_MODEL, null);
                        }
                    }

                    model = getModelByIndex(index + 1);
                    if (model.executeRequest(Thread.currentThread(), index + 1)) {
                        if (!Thread.currentThread().isInterrupted()) {
                            mHandler.sendMessage(MessageHandler.POST_RIGHT_MODEL, null);
                        }
                    }

                    for (int i = index - 2; i >= 0; i--) {
                        model = getModelByIndex(i);
                        model.executeRequest(Thread.currentThread(), i);
                        if (!Thread.currentThread().isInterrupted()) {
                            mHandler.sendMessage(MessageHandler.POST_FILL_MODEL, model);
                        }
                    }
                    for (int i = index + 2; i < mModelsCount; i++) {
                        model = getModelByIndex(i);
                        model.executeRequest(Thread.currentThread(), i);
                        if (!Thread.currentThread().isInterrupted()) {
                            mHandler.sendMessage(MessageHandler.POST_FILL_MODEL, model);
                        }
                    }
                } else {
                    int index = getIndex() - 1;
                    IModel model = getModelByIndex(index);
                    if (model.executeRequest(Thread.currentThread(), index)) {
                        if (!Thread.currentThread().isInterrupted()) {
                            mHandler.sendMessage(MessageHandler.POST_LEFT_MODEL, null);
                        }
                    }
                    for (int i = index - 1; i >= 0; i--) {
                        model = getModelByIndex(i);
                        model.executeRequest(Thread.currentThread(), i);
                        if (!Thread.currentThread().isInterrupted()) {
                            mHandler.sendMessage(MessageHandler.POST_FILL_MODEL, model);
                        }
                    }
                }
                if (!Thread.currentThread().isInterrupted()) {
                    mHandler.sendMessage(MessageHandler.POST_EXECUTE, null);
                }
            }
        });
        thread.start();

    }

    public void interrupt() {
        if (thread != null) {
            if (!thread.isInterrupted()) {
                thread.interrupt();
                try {
                    thread.join();
                } catch (InterruptedException ignored) {
                }
            }
        }
    }
    protected boolean refreshModels(int shift) {

        this.mIndex += shift;

        boolean updated = (
                (this.mModel[1] == null) ||
                        (this.mModel[this.mModelsCount - 2] == null) ||
                        (this.mIndex < 1) ||
                        (this.mIndex > (this.mModelsCount - 2))
        );

        if (updated) {
            if (shift > 0) {
                dataWrapper.increment(this.mModelsCount - 2);
                this.mModel[1].assingModel(this.mModel[this.mModelsCount - 1]);
                this.mModel[0].assingModel(this.mModel[this.mModelsCount - 2]);
                for (int position = 2; position < this.mModelsCount; position++) {
                    mHandler.sendMessage(MessageHandler.POST_PREPARE_MODEL, this.mModel[position]);
//                    this.mModel[position] = getModel(position);
                }
                this.mIndex = 1;
            } else if (shift == 0) {
                for (int position = 0; position < this.mModelsCount; position++) {
                    this.mModel[position] = getModel(position);
                }
            } else {
                dataWrapper.decrement(this.mModelsCount - 2);
                this.mModel[this.mModelsCount - 2].assingModel(this.mModel[0]);
                this.mModel[this.mModelsCount - 1].assingModel(this.mModel[1]);
                for (int position = this.mModelsCount - 3; position >= 0; position--) {
                    mHandler.sendMessage(MessageHandler.POST_PREPARE_MODEL, this.mModel[position]);
//                    this.mModel[position] = getModel(position);
                }
                this.mIndex = this.mModelsCount - 2;
            }
        }

        return updated;
    }

    protected IModel[] initModel(int modelCount) {
        return new IModel[modelCount];
    }

    protected IModel getModel(int index) {
        return new SimpleModel(index, this.mContext, this.dataWrapper);
    }

    public IModel getModelByIndex(int index) {

        return this.mModel[index];
    }

    @Override
    public int getIndex() {
        return this.mIndex;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return this.mModel.length;
    }

    @Override
    public View getView(int position) {
        return this.mModel[position].getView(this.mContext);
    }

    @Override
    public void setHandlerData(int what, Object object) {
        IModel model;
        switch (what) {
            case MessageHandler.PRE_EXECUTE:
//                if (this.footerText != null)
//                    this.footerText.setVisibility(View.INVISIBLE);
//                if (this.footerProgressBar != null)
//                    this.footerProgressBar.setVisibility(View.VISIBLE);
                break;
            case MessageHandler.POST_EXECUTE:
//                if (this.footerText != null)
//                    this.footerText.setVisibility(View.VISIBLE);
//                if (this.footerProgressBar != null)
//                    this.footerProgressBar.setVisibility(View.INVISIBLE);
                break;
            case MessageHandler.POST_LEFT_MODEL:
                this.mModel[this.mIndex-1].fillContent();
                break;
            case MessageHandler.POST_MIDDLE_MODEL:
                this.mModel[this.mIndex].fillContent();
                break;
            case MessageHandler.POST_RIGHT_MODEL:
                this.mModel[this.mIndex+1].fillContent();
                break;
            case MessageHandler.POST_PREPARE_MODEL:
                model = (IModel) object;
                model.prepareContent();
                break;
            case MessageHandler.POST_FILL_MODEL:
                model = (IModel) object;
                model.fillContent();
                break;
            case MessageHandler.POST_INDEX:
//                int dayIndex = (int) object;
//                this.getHistoryData().setDayIndex(dayIndex);
//                this.mCalendarWrapper.setCalendarDate(this.getHistoryData().getHebrewDates(dayIndex).getDate());
//                this.calendarPageModel[1].fillContent(this.getHistoryData().getHebrewDates(dayIndex));
//                this.calendarPageModel[0].fillContent(this.getHistoryData().getHebrewDates(dayIndex - 1));
//                this.calendarPageModel[2].fillContent(this.getHistoryData().getHebrewDates(dayIndex + 1));
                break;
        }

    }
}