package ru.gdo.android.example.gcm.notification;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 11.09.15.
 */

public class NotificationData {

    private final Class<?> intentClass;

    private final int notificationId;

    public NotificationData(Class<?> intentClass, int notificationId) {
        this.intentClass = intentClass;
        this.notificationId = notificationId;
    }

    public Class<?> getIntentClass() {
        return this.intentClass;
    }

    public int getNotificationId() {
        return this.notificationId;
    }

}
