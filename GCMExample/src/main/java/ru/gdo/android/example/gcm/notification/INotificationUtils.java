package ru.gdo.android.example.gcm.notification;

import android.content.Context;
import android.support.v4.app.NotificationCompat;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 11.09.15.
 */

public interface INotificationUtils {

    NotificationCompat.InboxStyle getInboxStyle(NotificationData data, Context context);

    NotificationData getNotificationData();

}
