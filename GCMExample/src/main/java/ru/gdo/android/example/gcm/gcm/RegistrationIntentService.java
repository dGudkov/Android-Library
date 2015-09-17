package ru.gdo.android.example.gcm.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 10.09.15.
 */

public class RegistrationIntentService extends IntentService {

    public static String deviceToken;

    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global", "local"};

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try {
            // [START register_for_gcm]
            // Initially this call goes out to the network to retrieve the token, subsequent calls
            // are local.
            // [START get_token]
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(GcmConfiguration.SENDER_ID,
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            deviceToken = token;
            Log.i(TAG, "GCM Registration Token: " + token);

            sendRegistrationToServer(token);
            subscribeTopics(token);

            // [END register_for_gcm]
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
//        Intent registrationComplete = new Intent(GcmPreferences.REGISTRATION_COMPLETE);
//        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendUnRegistrationToServer(InstanceID instanceID, String token) throws IOException {
        if (token != null) {
            instanceID.deleteToken(token, GoogleCloudMessaging.INSTANCE_ID_SCOPE);
        }

    }

    /**
     * Persist registration to third-party servers.
     *
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
    }

    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @param token GCM token
     * @throws IOException if unable to reach the GCM PubSub service
     */
    // [START subscribe_topics]
    private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        for (String topic : TOPICS) {
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
    // [END subscribe_topics]

    private void unSubscribeTopics(String token) throws IOException {
        if (token != null) {
            GcmPubSub pubSub = GcmPubSub.getInstance(this);
            for (String topic : TOPICS) {
                pubSub.unsubscribe(token, "/topics/" + topic);
            }
        }
    }

}
