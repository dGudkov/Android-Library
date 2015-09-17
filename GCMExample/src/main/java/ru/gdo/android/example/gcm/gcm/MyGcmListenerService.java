package ru.gdo.android.example.gcm.gcm;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import ru.gdo.android.example.gcm.GcmActivity;
import ru.gdo.android.example.gcm.notification.INotificationUtils;
import ru.gdo.android.example.gcm.notification.NotificationData;
import ru.gdo.android.example.gcm.notification.NotificationUtils;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 10.09.15.
 */

public class MyGcmListenerService extends GcmListenerService implements INotificationUtils {

    private static final String TAG = "MyGcmListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);

        if (from.startsWith("/topics/global")) {
            NotificationUtils.sendNotification(
                    this,
                    new GcmNotificationData(
                            GcmActivity.class,
                            NotificationUtils.NOTIFICATION_ID_TOPIC_GLOBAL,
                            message,
                            from
                    ),
                    getBaseContext()
            );
        } else if (from.startsWith("/topics/local")) {
            NotificationUtils.sendNotification(
                    this,
                    new GcmNotificationData(
                            GcmActivity.class,
                            NotificationUtils.NOTIFICATION_ID_TOPIC_LOCAL,
                            message,
                            from
                    ),
                    getBaseContext()
            );
        } else {
            NotificationUtils.sendNotification(
                    this,
                    new GcmNotificationData(
                            GcmActivity.class,
                            NotificationUtils.NOTIFICATION_ID_TOPIC_STREAM,
                            message,
                            "Stream"
                    ),
                    getBaseContext()
            );
        }
    }

    @Override
    public NotificationCompat.InboxStyle getInboxStyle(NotificationData data, Context context) {
        if (data != null) {
            GcmNotificationData gcmData = (GcmNotificationData) data;

            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

            inboxStyle.setBigContentTitle("Android library: GCM Message");

            inboxStyle.addLine(gcmData.getMessage() + " from " + gcmData.getFrom());

            return inboxStyle;
        }

        return null;
    }

    @Override
    public NotificationData getNotificationData() {
        return null;
    }

    private class GcmNotificationData extends NotificationData {

        private final String message;
        private final String from;

        public GcmNotificationData(Class<?> intentClass, int notificationId, String message, String from) {
            super(intentClass, notificationId);
            this.message = message;
            this.from = from;
        }

        public String getMessage() {
            return this.message;
        }

        public String getFrom() {
            return this.from;
        }
    }

}
