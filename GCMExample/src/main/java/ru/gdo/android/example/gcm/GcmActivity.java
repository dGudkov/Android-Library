package ru.gdo.android.example.gcm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ru.gdo.android.example.gcm.gcm.GcmConfiguration;
import ru.gdo.android.example.gcm.gcm.RegistrationIntentService;

public class GcmActivity extends Activity {

    private Button button_1;
    private Button button_2;
    private Button button_3;
    private Button button_4;
    private EditText eText_1;
    private EditText eText_2;
    private EditText eText_3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gcm);
        if (checkPlayServices(this)) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }

        button_1 = (Button) findViewById(R.id.sendbutton_1);
        button_2 = (Button) findViewById(R.id.sendbutton_2);
        button_3 = (Button) findViewById(R.id.sendbutton_3);
        button_4 = (Button) findViewById(R.id.sendbutton_4);

        eText_1 = (EditText) findViewById(R.id.text_1);
        eText_2 = (EditText) findViewById(R.id.text_2);
        eText_3 = (EditText) findViewById(R.id.text_3);

        eText_1.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                button_1.setEnabled(((s != null) && (!s.equals(""))));
                button_4.setEnabled((!eText_1.getText().toString().equals("")) ||
                                (!eText_2.getText().toString().equals("")) ||
                                (!eText_3.getText().toString().equals(""))
                );
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        eText_2.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                button_2.setEnabled(((s != null) && (!s.equals(""))));
                button_4.setEnabled((!eText_1.getText().toString().equals("")) ||
                                (!eText_2.getText().toString().equals("")) ||
                                (!eText_3.getText().toString().equals(""))
                );
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        eText_3.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                button_3.setEnabled(((s != null) && (!s.equals(""))));
                button_4.setEnabled((!eText_1.getText().toString().equals("")) ||
                                (!eText_2.getText().toString().equals("")) ||
                                (!eText_3.getText().toString().equals(""))
                );
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToGlobalTopic();
            }
        });

        button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToLocalTopic();
            }
        });

        button_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToThisDeviceTopic();
            }
        });

        button_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToGlobalTopic();
                sendToLocalTopic();
                sendToThisDeviceTopic();
            }
        });
    }


    private void sendToGlobalTopic() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!eText_1.getText().toString().equals("")) {
                        sendRequest("{\"to\":\"/topics/global\",\"data\":{\"message\":\"" + eText_1.getText().toString() + "\"}}");
                    }
                } catch (IOException e) {
                    System.out.println("Unable to send GCM message.");
                    System.out.println("Please ensure that API_KEY has been replaced by the server " +
                            "API key, and that the device's registration token is correct (if specified).");
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private void sendToLocalTopic() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!eText_2.getText().toString().equals("")) {
                        sendRequest("{\"to\":\"/topics/local\",\"data\":{\"message\":\"" + eText_2.getText().toString() + "\"}}");
                    }
                } catch (IOException e) {
                    System.out.println("Unable to send GCM message.");
                    System.out.println("Please ensure that API_KEY has been replaced by the server " +
                            "API key, and that the device's registration token is correct (if specified).");
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private void sendToThisDeviceTopic() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!eText_3.getText().toString().equals("")) {
                        if (RegistrationIntentService.deviceToken != null) {
                            String token = RegistrationIntentService.deviceToken;
                            sendRequest("{\"to\":\"" + token + "\",\"data\":{\"message\":\"" + eText_3.getText().toString() + "\"}}");
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Unable to send GCM message.");
                    System.out.println("Please ensure that API_KEY has been replaced by the server " +
                            "API key, and that the device's registration token is correct (if specified).");
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private static String sendRequest(String jGcmData) throws IOException {
        // Create connection to send GCM Message request.
        String  url = "https://android.googleapis.com/gcm/send";

        List<Pair<String, String>> pair = new ArrayList<>();
        pair.add(new Pair<>("Authorization", "key=" + GcmConfiguration.API_KEY));
        pair.add(new Pair<>("Content-Type", "application/json"));

        return sendPostRequest(url, pair, jGcmData);

    }

    private static String sendPostRequest(String host, List<Pair<String, String>> requestProperty,
                                         String data) throws IOException {

        OutputStream outputStream = null;
        InputStream inputStream = null;

        try {
            // Create connection to send GCM Message request.
            URL url = new URL(host);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            for (Pair<String, String> entry : requestProperty) {
                conn.setRequestProperty(entry.first, entry.second);
            }
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // Send GCM message content.
            outputStream = conn.getOutputStream();
            outputStream.write(data.getBytes());

            // Read GCM response.
            inputStream = conn.getInputStream();
            return IOUtils.toString(inputStream);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException ignored) {
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ignored) {
                }
            }
        }
    }
    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private static boolean checkPlayServices(Context context) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
//            private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
//            if (apiAvailability.isUserResolvableError(resultCode)) {
//                apiAvailability.getErrorDialog(context, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
//                        .show();
//            } else {
//                Log.i(TAG, "This device is not supported.");
//                finish();
//            }
            return false;
        }
        return true;
    }

}
