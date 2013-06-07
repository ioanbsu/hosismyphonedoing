package com.artigile.checkmyphone;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import com.artigile.howismyphonedoing.api.AndroidMessageProcessor;
import com.artigile.howismyphonedoing.api.MessageType;
import com.artigile.howismyphonedoing.api.model.PhoneModel;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;

import static com.artigile.checkmyphone.CommonUtilities.displayMessage;

/**
 * User: ioanbsu
 * Date: 6/6/13
 * Time: 6:33 PM
 */
@Singleton
public class AndroidMessageReceiver implements AndroidMessageProcessor<String> {
    @Inject
    private AndroidMessageSender messageSender;
    @Inject
    private Context context;

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

    @Override
    public String processMessage(MessageType messageType, String message) {
        if (messageType == MessageType.PHONE_INFO) {
            PhoneModel phoneModel = buildPhoneModel();
            Gson gson = new Gson();
            try {
                messageSender.processMessage( MessageType.PHONE_INFO, gson.toJson(phoneModel));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
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
        return "message had been successfully sent";
    }

    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    private void generateNotification(Context context, String message) {
        int icon = R.drawable.ic_stat_gcm;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(icon, message, when);
        String title = context.getString(R.string.app_name);
        Intent notificationIntent = new Intent(context, MainActivity.class);
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        notification.setLatestEventInfo(context, title, message, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, notification);
    }

}
