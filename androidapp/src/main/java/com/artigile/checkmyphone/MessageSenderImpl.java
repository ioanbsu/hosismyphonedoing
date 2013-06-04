package com.artigile.checkmyphone;

import android.util.Log;
import com.artigile.howismyphonedoing.api.CommonContants;
import com.artigile.howismyphonedoing.api.MessageSender;
import com.artigile.howismyphonedoing.api.MessageType;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.artigile.checkmyphone.CommonUtilities.SERVER_URL;
import static com.artigile.checkmyphone.CommonUtilities.TAG;

/**
 * @author IoaN, 6/3/13 9:08 PM
 */
public class MessageSenderImpl implements MessageSender<String> {
    private static final String serverUrl = SERVER_URL + "/register";

    @Override
    public void sendMessage(String key, MessageType messageType, String message) throws IOException {
        Map<String, String> params = new HashMap<String, String>();
        params.put(CommonContants.REG_ID, key);
        params.put(CommonContants.MESSAGE_EVENT_TYPE, messageType.toString());
        params.put(CommonContants.SERIALIZED_OBJECT, message);
        post(params);
    }

    /**
     * Issue a POST request to the server.
     *
     * @param params request parameters.
     * @throws java.io.IOException propagated from POST.
     */
    public void post(Map<String, String> params)
            throws IOException {
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}
