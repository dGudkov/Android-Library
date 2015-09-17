package ru.gdo.android.example.gcm.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import ru.gdo.android.example.gcm.R;
/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 11.09.15.
 */

public final class NotificationUtils {

    public static final int NOTIFICATION_ID_DAILY = 1;
    public static final int NOTIFICATION_ID_WEEKLY = 2;
    public static final int NOTIFICATION_ID_TOPIC_GLOBAL = 3;
    public static final int NOTIFICATION_ID_TOPIC_LOCAL = 4;
    public static final int NOTIFICATION_ID_TOPIC_STREAM = 5;

    public static void sendNotification(INotificationUtils sender, NotificationData data, Context context) {
        send(sender, data, context);
    }

    private static void send(INotificationUtils sender,  NotificationData data, Context context) {

        NotificationCompat.InboxStyle inboxStyle = sender.getInboxStyle(data, context);

        if (inboxStyle != null) {

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setStyle(inboxStyle);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addNextIntent(new Intent(context, data.getIntentClass()));
            PendingIntent resultIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(resultIntent);
            builder.setAutoCancel(true);
            builder.setWhen(System.currentTimeMillis());

//            RemoteViews expandedView = new RemoteViews(context.getPackageName(),
//                R.layout.notification_custom_remote);

            Notification notification;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                notification = builder.build();
            } else {
                notification = builder.getNotification();
            }
//            notification.bigContentView = expandedView;
            ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(data.getNotificationId(), notification);
        }

    }

}
