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
import com.artigile.howismyphonedoing.api.model.MessageType;

import javax.inject.Inject;
import javax.inject.Singleton;

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
    private CommonUtilities commonUtilities;

    public GCMIntentService() {
        super(SENDER_ID);
    }

    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i(TAG, "Device registered: regId = " + registrationId);
        commonUtilities.displayMessage(context, getString(R.string.gcm_registered, registrationId));
        deviceRegistrationService.register(context, registrationId);
    }

    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");
        commonUtilities.displayMessage(context, getString(R.string.gcm_unregistered));
        deviceRegistrationService.unregister(context, registrationId);
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
        Log.i(TAG, "Received message. Extras: " + intent.getExtras());
        messageReceiver.processMessage(MessageType.valueOf(intent.getStringExtra(CommonConstants.MESSAGE_EVENT_TYPE)),
                intent.getStringExtra(CommonConstants.SERIALIZED_OBJECT));
    }

    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
        String message = getString(R.string.gcm_deleted, total);
        commonUtilities.displayMessage(context, message);
        // notifies user
        //at some point we might analyze it ...
    }

    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
        commonUtilities.displayMessage(context, getString(R.string.gcm_error, errorId));
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        commonUtilities.displayMessage(context, getString(R.string.gcm_recoverable_error,
                errorId));
        return super.onRecoverableError(context, errorId);
    }

}
