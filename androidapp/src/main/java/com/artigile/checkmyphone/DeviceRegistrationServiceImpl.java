package com.artigile.checkmyphone;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.util.Log;
import com.artigile.checkmyphone.util.GCMRegistrar;
import com.artigile.howismyphonedoing.api.AndroidMessageProcessor;
import com.artigile.howismyphonedoing.api.MessageType;
import com.artigile.howismyphonedoing.api.model.DeviceRegistrationModel;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import static com.artigile.checkmyphone.CommonUtilities.*;

/**
 * Helper class used to communicate with the demo server.
 */
@Singleton
public final class DeviceRegistrationServiceImpl {

    private static final int MAX_ATTEMPTS = 5;
    private static final int BACKOFF_MILLI_SECONDS = 2000;
    private static final Random random = new Random();
    @Inject
    private DeviceUuidResolver deviceUuidResolver;
    @Inject
    private AndroidMessageSender messageSender;

    /**
     * Register this account/device pair within the server.
     */
    public void register(final Context context, final String regId) {
        Log.i(TAG, "registering device (regId = " + regId + ")");

        long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
        // Once GCM returns a registration id, we need to register it in the
        // demo server. As the server might be down, we will retry it a couple
        // times.
        String deviceRegistrationModelConverted = createRegisterMap(context, regId);
        for (int i = 1; i <= MAX_ATTEMPTS; i++) {
            Log.d(TAG, "Attempt #" + i + " to register");
            try {
                displayMessage(context, context.getString(R.string.server_registering, i, MAX_ATTEMPTS));
                messageSender.processMessage( MessageType.REGISTER_DEVICE, deviceRegistrationModelConverted);
                GCMRegistrar.setRegisteredOnServer(context, true);
                String message = context.getString(R.string.server_registered);
                displayMessage(context, message);
                return;
            } catch (IOException e) {
                // Here we are simplifying and retrying on any error; in a real
                // application, it should retry only on unrecoverable errors
                // (like HTTP error code 503).
                Log.e(TAG, "Failed to register on attempt " + i + ":" + e);
                if (i == MAX_ATTEMPTS) {
                    break;
                }
                try {
                    Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
                    Thread.sleep(backoff);
                } catch (InterruptedException e1) {
                    // Activity finished before we complete - exit.
                    Log.d(TAG, "Thread interrupted: abort remaining retries!");
                    Thread.currentThread().interrupt();
                    return;
                }
                // increase backoff exponentially
                backoff *= 2;
            }
        }
        String message = context.getString(R.string.server_register_error,
                MAX_ATTEMPTS);
        displayMessage(context, message);
    }

    private String createRegisterMap(Context context, String cloudRegistrationId) {
        DeviceRegistrationModel deviceRegistrationModel = new DeviceRegistrationModel();
        String userEmail = getUserEmail(context);
        deviceRegistrationModel.setUserEmail(userEmail);
        deviceRegistrationModel.setDeviceCloudRegistrationId(cloudRegistrationId);
        return new Gson().toJson(deviceRegistrationModel);
    }

    private String getUserEmail(Context context) {
        Account[] accounts = AccountManager.get(context).getAccountsByType("com.google");
        return accounts[0].name;
    }

    /**
     * Unregister this account/device pair within the server.
     */
    public void unregister(final Context context, final String regId) {
        Log.i(TAG, "unregistering device (regId = " + regId + ")");
        String deviceRegistrationModelConverted = createRegisterMap(context, regId);
        try {
            messageSender.processMessage(MessageType.UNREGISTER_DEVICE, deviceRegistrationModelConverted);
            GCMRegistrar.setRegisteredOnServer(context, false);
            String message = context.getString(R.string.server_unregistered);
            displayMessage(context, message);
        } catch (IOException e) {
            // At this point the device is unregistered from GCM, but still
            // registered in the server.
            // We could try to unregister again, but it is not necessary:
            // if the server tries to send a message to the device, it will get
            // a "NotRegistered" error message and should unregister the device.
            String message = context.getString(R.string.server_unregister_error,
                    e.getMessage());
            displayMessage(context, message);
        }
    }

    /**
     * Issue a POST request to the server.
     *
     * @param endpoint POST address.
     * @param params   request parameters.
     * @throws IOException propagated from POST.
     */
    public void post(String endpoint, Map<String, String> params)
            throws IOException {
        URL url;
        try {
            url = new URL(endpoint);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url: " + endpoint);
        }
        StringBuilder bodyBuilder = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        // constructs the POST body using the parameters
        while (iterator.hasNext()) {
            Map.Entry<String, String> param = iterator.next();
            bodyBuilder.append(param.getKey()).append('=')
                    .append(param.getValue());
            if (iterator.hasNext()) {
                bodyBuilder.append('&');
            }
        }
        String body = bodyBuilder.toString();
        Log.v(TAG, "Posting '" + body + "' to " + url);
        byte[] bytes = body.getBytes();
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setFixedLengthStreamingMode(bytes.length);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded;charset=UTF-8");
            // post the request
            OutputStream out = conn.getOutputStream();
            out.write(bytes);
            out.close();
            // handle the response
            int status = conn.getResponseCode();
            if (status != 200) {
                throw new IOException("Post failed with error code " + status);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}