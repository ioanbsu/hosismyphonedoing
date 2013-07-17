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

/**
 * User: ioanbsu
 * Date: 5/21/13
 * Time: 9:39 AM
 */
@Singleton
public class CommonUtilities {
    /**
     * Google API project id registered to use GCM.
     */
    public static final String SENDER_ID = "983271462651";
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
     * Intent used to display a message in the screen.
     */
    public static final String PICTURE_TAKEN_ACTION =
            "com.google.android.gcm.demo.app.PICTURE_TAKEN";
    /**
     * Intent's extra that contains the message to be displayed.
     */
    public static final String MESSAGE = "message";
    public static final String MESSAGE_TYPE = "messageType";
    public static final int LOG_MESSAGE_TYPE = 0;
    public static final int REGISTRATION_STATUS_MESSAGE_TYPE = 1;
    public static final int SHOW_LOGS_STATE_UPDATED = 2;
    @InjectResource(R.string.remote_application_url)
    private String serverUrl;

    /**
     * Notifies UI to display a message.
     * <p/>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    public void displayMessage(Context context, String message, int messageType) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(MESSAGE, message);
        intent.putExtra(MESSAGE_TYPE, messageType);
        context.sendBroadcast(intent);
    }

    public void firePictureTakenEvent(Context context, byte[] data) {
        Intent intent = new Intent(CommonUtilities.PICTURE_TAKEN_ACTION);
        intent.putExtra(MESSAGE, data);
        context.sendBroadcast(intent);
    }

    public String getServerUrl() {
        return serverUrl;
    }
}
