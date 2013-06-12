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

import android.content.Context;
import android.content.Intent;
import com.artigile.checkmyphone.R;
import roboguice.inject.InjectResource;

import javax.inject.Singleton;
import java.io.IOException;
import java.util.Properties;

/**
 * User: ioanbsu
 * Date: 5/21/13
 * Time: 9:39 AM
 */
@Singleton
public class CommonUtilities {
    @InjectResource(R.string.remote_application_url)
    private String serverUrl;
    /**
     * Google API project id registered to use GCM.
     */
    public static final String SENDER_ID = "861640283566";
    /**
     * Tag used on log messages.
     */
    public static final String TAG = "GCMDemo";
    /**
     * Intent used to display a message in the screen.
     */
    public static final String DISPLAY_MESSAGE_ACTION =
            "com.google.android.gcm.demo.app.DISPLAY_MESSAGE";
    /**
     * Intent's extra that contains the message to be displayed.
     */
    public static final String EXTRA_MESSAGE = "message";

    /**
     * Notifies UI to display a message.
     * <p/>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    public void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }

    public String getServerUrl(String propertyName) {
        return serverUrl;
    }
}
