/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.checkmyphone.service;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.artigile.howismyphonedoing.api.AndroidMessageProcessor;
import com.artigile.howismyphonedoing.api.AndroidMessageResultListener;
import com.artigile.howismyphonedoing.api.CommonConstants;
import com.artigile.howismyphonedoing.api.MessageSendResultType;
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


/**
 * @author IoaN, 6/3/13 9:08 PM
 */
@Singleton
public class AndroidMessageSender implements AndroidMessageProcessor<String> {
    private static final String TAG = "AndroidMessageSender";
    private String serverUrl;
    @Inject
    private Context context;
    @Inject
    private DeviceUuidResolver deviceUuidResolver;
    @Inject
    private CommonUtilities commonUtilities;
    private AndroidMessageResultListener messageResultListener;

    @Override
    public String processMessage(MessageType messageType, String message, AndroidMessageResultListener messageResultListener) throws IOException {
        this.messageResultListener = messageResultListener;
        if (serverUrl == null) {
            serverUrl = commonUtilities.getServerUrl() + CommonConstants.MESSAGES_COMMUNICATION_URL;
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
        AsyncTask<Map<String, String>, Void, MessageSendResultType> asyncSendResponseToWebServer = new AsyncTask<Map<String, String>, Void, MessageSendResultType>() {
            @Override
            protected MessageSendResultType doInBackground(Map<String, String>... paramsArray) {
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
                            return MessageSendResultType.FAILED;
                        }
                        return MessageSendResultType.SUCCESS;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return MessageSendResultType.FAILED;
                    } finally {
                        if (conn != null) {
                            conn.disconnect();
                        }
                    }
                }
                return MessageSendResultType.NO_MESSAGES_TO_SEND;
            }

            @Override
            protected void onPostExecute(MessageSendResultType messageSendResult) {
                if(messageResultListener!=null){
                    messageResultListener.onMessageResult(messageSendResult);
                }
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
