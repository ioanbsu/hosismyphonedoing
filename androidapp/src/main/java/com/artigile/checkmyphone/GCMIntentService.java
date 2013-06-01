package com.artigile.checkmyphone;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import com.artigile.checkmyphone.util.GCMBaseIntentService;
import com.artigile.howismyphonedoing.api.CommonContants;
import com.artigile.howismyphonedoing.api.EventType;
import com.artigile.howismyphonedoing.api.model.PhoneModel;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.artigile.checkmyphone.CommonUtilities.SENDER_ID;
import static com.artigile.checkmyphone.CommonUtilities.SERVER_URL;
import static com.artigile.checkmyphone.CommonUtilities.displayMessage;

/**
 * User: ioanbsu
 * Date: 5/21/13
 * Time: 9:55 AM
 */
@Singleton
public class GCMIntentService extends GCMBaseIntentService {

    @Inject
    private WebServerUtilities webServerUtilities;
    @SuppressWarnings("hiding")
    private static final String TAG = "GCMIntentService";

    public GCMIntentService() {
        super(SENDER_ID);
    }

    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    private void generateNotification(Context context, String message) {
        int icon = R.drawable.ic_stat_gcm;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(icon, message, when);
        String title = context.getString(R.string.app_name);
        Intent notificationIntent = new Intent(context, MainActivity.class);
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent =
                PendingIntent.getActivity(context, 0, notificationIntent, 0);
        notification.setLatestEventInfo(context, title, message, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, notification);
    }

    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i(TAG, "Device registered: regId = " + registrationId);
        displayMessage(context, getString(R.string.gcm_registered, registrationId));
        webServerUtilities.register(context, registrationId);
    }

    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");
        displayMessage(context, getString(R.string.gcm_unregistered));
        webServerUtilities.unregister(context, registrationId);
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
        Log.i(TAG, "Received message. Extras: " + intent.getExtras());
        if (EventType.PHONE_INFO.of(intent.getStringExtra(CommonContants.MESSAGE_EVENT_TYPE))) {
            String serverUrl = SERVER_URL + "/register";
            PhoneModel phoneModel = buildPhoneModel();
                Gson gson = new Gson();
            Map<String, String> params = new HashMap<String, String>();
            params.put(CommonContants.MESSAGE_EVENT_TYPE, EventType.PHONE_INFO.toString());
            params.put("phoneInfo", gson.toJson(phoneModel));
            try {
                webServerUtilities.post(serverUrl, params);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else{
            String message = intent.getStringExtra("mydata");//getString(R.string.gcm_message);
            displayMessage(context, message);
            TextToSpeech textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {

                }
            });
            textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null);
            textToSpeech.speak(message, TextToSpeech.QUEUE_ADD, null);
            // notifies user
            generateNotification(context, message);
        }
    }

    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
        String message = getString(R.string.gcm_deleted, total);
        displayMessage(context, message);
        // notifies user
        generateNotification(context, message);
    }

    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
        displayMessage(context, getString(R.string.gcm_error, errorId));
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        displayMessage(context, getString(R.string.gcm_recoverable_error,
                errorId));
        return super.onRecoverableError(context, errorId);
    }





    private static PhoneModel buildPhoneModel() {
        PhoneModel phoneModel = new PhoneModel();
        phoneModel.setBoard(Build.BOARD);
        phoneModel.setModel(Build.MODEL);
        phoneModel.setBootLoader(Build.BOOTLOADER);
        phoneModel.setBrand(Build.BRAND);
        phoneModel.setCpuAbi(Build.CPU_ABI);
        phoneModel.setCpuAbi2(Build.CPU_ABI2);
        phoneModel.setDevice(Build.DEVICE);
        phoneModel.setDisplay(Build.DISPLAY);
        phoneModel.setFingerprint(Build.FINGERPRINT);
        phoneModel.setHardware(Build.HARDWARE);
        phoneModel.setHost(Build.HOST);
        phoneModel.setId(Build.ID);
        phoneModel.setManufacturer(Build.MANUFACTURER);
        phoneModel.setModel(Build.MODEL);
        phoneModel.setProduct(Build.PRODUCT);
        phoneModel.setSerial(Build.SERIAL);
        phoneModel.setTags(Build.TAGS);
        phoneModel.setTime(Build.TIME);
        phoneModel.setType(Build.TYPE);
        phoneModel.setUnknown(Build.UNKNOWN);
        phoneModel.setUser(Build.USER);
        phoneModel.setRadioVersion(Build.getRadioVersion());
        return phoneModel;
    }

}
