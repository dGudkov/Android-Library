package ru.gdo.android.library.pagecurl.handler;

import android.os.Handler;
import android.os.Message;

import ru.gdo.android.library.pagecurl.interfaces.IHandlerSetData;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 04.09.15.
 */

public class MessageHandler extends Handler {

    public static final int PRE_EXECUTE = 1;
    public static final int POST_EXECUTE = 2;
    public static final int POST_LEFT_MODEL = 3;
    public static final int POST_MIDDLE_MODEL = 4;
    public static final int POST_RIGHT_MODEL = 5;
    public static final int POST_PREPARE_MODEL = 6;
    public static final int POST_FILL_MODEL = 7;
    public static final int POST_INDEX = 8;

    private IHandlerSetData recepient;

    public MessageHandler(IHandlerSetData recepient) {
        this.recepient = recepient;
    }

    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        recepient.setHandlerData(msg.what, msg.obj);
    }

    public void sendMessage(int what, Object object) {
        Message message = obtainMessage(what, object);
        sendMessage(message);
    }
}