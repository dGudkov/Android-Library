package ru.gdo.andriod.example.pagecurl.controller;

import android.content.Context;
import android.view.View;

import ru.gdo.andriod.example.pagecurl.handler.MessageHandler;
import ru.gdo.andriod.example.pagecurl.interfaces.IAdapter;
import ru.gdo.andriod.example.pagecurl.interfaces.IHandlerSetData;
import ru.gdo.andriod.example.pagecurl.model.IModel;
import ru.gdo.andriod.example.pagecurl.model.SimpleModel;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 04.09.15.
 */

public class ModelController implements IAdapter, IHandlerSetData {

    private static int MODEL_COUNT = 4;
    private final MessageHandler mHandler;

    private Context context;
    private IModel[] mModel;
    private int modelsCount;
    private int mIndex;
    private int mValue;
    private Thread thread;

    public ModelController() {
        this(null, MODEL_COUNT);
    }

    public ModelController(Context context) {
        this(context, MODEL_COUNT);
    }

    public ModelController(Context context, int modelCount) {
        this.context = context;
        if (modelCount < 3) {
            modelCount = MODEL_COUNT;
        }
        this.mModel = initModel(modelCount);
        this.modelsCount = this.mModel.length;
        if (modelCount == 3) {
            this.mIndex = 1;
        } else {
            this.mIndex = (this.modelsCount / 2);
        }
        this.mValue = 0;
        this.mHandler = new MessageHandler(this);
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
//                    HistoryData.HebrewDate hebrewDate = historyData.getHebrewDates(index);
//                    if (executeRequest(hebrewDate)) {
//                        if (!Thread.currentThread().isInterrupted()) {
//                            mHandler.sendMessage(MessageHandler.POST_RIGHT_MODEL, hebrewDate);
//                        }
//                    }
//                    for (int i = index + 1; i < 9; i++) {
//                        hebrewDate = historyData.getHebrewDates(i);
//                        executeRequest(hebrewDate);
//                    }
                } else if (shift == 0) {
                    int index = getIndex();

                    IModel model = getModelByIndex(index);
                    if (model.executeRequest(Thread.currentThread())) {
                        if (!Thread.currentThread().isInterrupted()) {
                            mHandler.sendMessage(MessageHandler.POST_MIDDLE_MODEL, mValue);
                        }
                    }

                    model = getModelByIndex(index - 1);
                    if (model.executeRequest(Thread.currentThread())) {
                        if (!Thread.currentThread().isInterrupted()) {
                            mHandler.sendMessage(MessageHandler.POST_LEFT_MODEL, mValue - 1);
                        }
                    }

                    model = getModelByIndex(index + 1);
                    if (model.executeRequest(Thread.currentThread())) {
                        if (!Thread.currentThread().isInterrupted()) {
                            mHandler.sendMessage(MessageHandler.POST_RIGHT_MODEL, mValue + 1);
                        }
                    }

                    for (int i = 0; i < index - 1; i++) {
                        model = getModelByIndex(i);
                        model.executeRequest(Thread.currentThread());
                    }
                    for (int i = index + 2; i < modelsCount ; i++) {
                        model = getModelByIndex(i);
                        model.executeRequest(Thread.currentThread());
                    }
                } else {
//                    int index = historyData.getDayIndex() - 1;
//                    HistoryData.HebrewDate hebrewDate = historyData.getHebrewDates(index);
//                    if (executeRequest(hebrewDate)) {
//                        if (!Thread.currentThread().isInterrupted()) {
//                            mHandler.sendMessage(MessageHandler.POST_LEFT_MODEL, hebrewDate);
//                        }
//                    }
//                    for (int i = 0; i < index; i++) {
//                        hebrewDate = historyData.getHebrewDates(i);
//                        executeRequest(hebrewDate);
//                    }
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
                        (this.mModel[this.modelsCount - 2] == null) ||
                        (this.mIndex < 1) ||
                        (this.mIndex > (this.modelsCount - 2))
        );


        if (updated) {
            if (shift > 0) {
                this.mValue += (this.modelsCount - 2);
                this.mModel[0] = assingModel(this.modelsCount - 2, 0, this.mValue);
                this.mModel[1] = assingModel(this.modelsCount - 1, 1, this.mValue);
                for (int position = 2; position < this.modelsCount; position++) {
                    this.mModel[position] = getModel(position, this.mValue);
                }
                this.mIndex = 1;
            } else if (shift == 0) {
                for (int position = 0; position < this.modelsCount; position++) {
                    this.mModel[position] = getModel(position, this.mValue);
                }
            } else {
                this.mValue -= (this.modelsCount - 2);
                this.mModel[this.modelsCount - 2] = assingModel(0, modelsCount - 2, this.mValue);
                this.mModel[this.modelsCount - 1] = assingModel(1, modelsCount - 1,  this.mValue);
                for (int position = 0; position < (this.modelsCount - 2); position++) {
                    this.mModel[position] = getModel(position, this.mValue);
                }
                this.mIndex = this.modelsCount - 2;
            }
        }

        return updated;
    }

    private IModel assingModel(int modelIndex, int index, int value) {
        IModel model = this.mModel[modelIndex];
        model.setIndex(index);
        model.setValue(value);
        model.fillContent();
        return model;
    }

    protected IModel[] initModel(int modelCount) {
        return new IModel[modelCount];
    }

    protected IModel getModel(int index, int value) {
        return new SimpleModel(index, value);
    }

    public IModel getModelByIndex(int index) {

        return this.mModel[index];
    }

    @Override
    public int getIndex() {
        return this.mIndex;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.mModel.length;
    }

    @Override
    public View getView(int position) {
        return this.mModel[position].getView(position, context);
    }

    @Override
    public void setHandlerData(int what, Object object) {
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
            case MessageHandler.POST_INDEX:
                int dayIndex = (int) object;
//                this.getHistoryData().setDayIndex(dayIndex);
//                this.mCalendarWrapper.setCalendarDate(this.getHistoryData().getHebrewDates(dayIndex).getDate());
//                this.calendarPageModel[1].fillContent(this.getHistoryData().getHebrewDates(dayIndex));
//                this.calendarPageModel[0].fillContent(this.getHistoryData().getHebrewDates(dayIndex - 1));
//                this.calendarPageModel[2].fillContent(this.getHistoryData().getHebrewDates(dayIndex + 1));
                break;
        }

    }
}