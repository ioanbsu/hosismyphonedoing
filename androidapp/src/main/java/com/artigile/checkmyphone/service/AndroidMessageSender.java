package com.artigile.checkmyphone.service;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.artigile.howismyphonedoing.api.AndroidMessageProcessor;
import com.artigile.howismyphonedoing.api.CommonConstants;
import com.artigile.howismyphonedoing.api.model.MessageType;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.artigile.checkmyphone.service.CommonUtilities.TAG;

/**
 * @author IoaN, 6/3/13 9:08 PM
 */
@Singleton
public class AndroidMessageSender implements AndroidMessageProcessor<String> {
    private String serverUrl;
    @Inject
    private Context context;
    @Inject
    private DeviceUuidResolver deviceUuidResolver;
    @Inject
    private CommonUtilities commonUtilities;

    @Override
    public String processMessage(MessageType messageType, String message) throws IOException {
        if (serverUrl == null) {
            serverUrl = commonUtilities.getServerUrl(CommonConstants.SERVER_URL_PARAM_NAME) + CommonConstants.MESSAGES_COMMUNICATION_URL;
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put(CommonConstants.UUID, deviceUuidResolver.getDeviceUuid(context).toString());
        params.put(CommonConstants.MESSAGE_EVENT_TYPE, messageType.toString());
        params.put(CommonConstants.SERIALIZED_OBJECT, message);
        return post(params);
    }

    /**
     * Issue a POST request to the server.
     *
     * @param params request parameters.
     * @throws java.io.IOException propagated from POST.
     */
    private String post(Map<String, String> params)
            throws IOException {
        AsyncTask<Map<String, String>, Void, String> asyncSendResponseToWebServer = new AsyncTask<Map<String, String>, Void, String>() {
            @Override
            protected String doInBackground(Map<String, String>... paramsArray) {
                for (Map<String, String> params : paramsArray) {
                    URL url;
                    try {
                        url = new URL(serverUrl);
                    } catch (MalformedURLException e) {
                        throw new IllegalArgumentException("invalid url: " + serverUrl);
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
                        return status + "";
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "Message failed to be sent.";
                    } finally {
                        if (conn != null) {
                            conn.disconnect();
                        }
                    }
                }
                return "no messages were sent";
            }
        };
        asyncSendResponseToWebServer.execute(params);
        return "message sent asynchronously";
    }

    private String getUserEmail(Context context) {
        Account[] accounts = AccountManager.get(context).getAccountsByType("com.google");
        return accounts[0].name;
    }

}
