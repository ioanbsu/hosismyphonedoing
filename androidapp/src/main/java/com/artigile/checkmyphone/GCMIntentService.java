/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.checkmyphone;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.artigile.checkmyphone.service.AndroidMessageReceiver;
import com.artigile.checkmyphone.service.AndroidMessageSender;
import com.artigile.checkmyphone.service.CommonUtilities;
import com.artigile.checkmyphone.service.DeviceRegistrationServiceImpl;
import com.artigile.checkmyphone.util.GCMBaseIntentService;
import com.artigile.howismyphonedoing.api.CommonConstants;
import com.artigile.howismyphonedoing.api.MessageParser;
import com.artigile.howismyphonedoing.api.model.MessageNotSupportedByDeviceResponseModel;
import com.artigile.howismyphonedoing.api.model.MessageType;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;

import static com.artigile.checkmyphone.service.CommonUtilities.SENDER_ID;

/**
 * User: ioanbsu
 * Date: 5/21/13
 * Time: 9:55 AM
 */
@Singleton
public class GCMIntentService extends GCMBaseIntentService {

    @SuppressWarnings("hiding")
    private static final String TAG = "GCMIntentService";
    @Inject
    private DeviceRegistrationServiceImpl deviceRegistrationService;
    @Inject
    private AndroidMessageSender messageSender;
    @Inject
    private AndroidMessageReceiver messageReceiver;
    @Inject
    private MessageParser messageParser;
    @Inject
    private CommonUtilities commonUtilities;

    public GCMIntentService() {
        super(SENDER_ID);
    }

    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i(TAG, "Device registered: regId = " + registrationId);
        commonUtilities.displayMessage(context, getString(R.string.gcm_registered, registrationId), CommonUtilities.LOG_MESSAGE_TYPE);
        deviceRegistrationService.register(context, registrationId);
        commonUtilities.displayMessage(context, getString(R.string.device_registered), CommonUtilities.REGISTRATION_STATUS_MESSAGE_TYPE);

    }

    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");
        commonUtilities.displayMessage(context, getString(R.string.gcm_unregistered), CommonUtilities.LOG_MESSAGE_TYPE);
        deviceRegistrationService.unregister(context, registrationId);
        commonUtilities.displayMessage(context, getString(R.string.device_not_registered), CommonUtilities.REGISTRATION_STATUS_MESSAGE_TYPE);
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
        Log.i(TAG, "Received message. Extras: " + intent.getExtras());
        String messageTypeStr = intent.getStringExtra(CommonConstants.MESSAGE_EVENT_TYPE);
        try {
            MessageType messageType = MessageType.valueOf(messageTypeStr);
            messageReceiver.processMessage(messageType, intent.getStringExtra(CommonConstants.SERIALIZED_OBJECT), null);
        } catch (IllegalArgumentException e) {
            try {
                String serializedCallback = messageParser.serialize(new MessageNotSupportedByDeviceResponseModel(messageTypeStr));
                messageSender.processMessage(MessageType.MESSAGE_TYPE_IS_NOT_SUPPORTED, serializedCallback, null);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
        String message = getString(R.string.gcm_deleted, total);
        commonUtilities.displayMessage(context, message, CommonUtilities.LOG_MESSAGE_TYPE);
        // notifies user
        //at some point we might analyze it ...
    }

    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
        commonUtilities.displayMessage(context, getString(R.string.gcm_error, errorId), CommonUtilities.LOG_MESSAGE_TYPE);
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        commonUtilities.displayMessage(context, getString(R.string.gcm_recoverable_error,
                errorId), CommonUtilities.LOG_MESSAGE_TYPE);
        return super.onRecoverableError(context, errorId);
    }

}
