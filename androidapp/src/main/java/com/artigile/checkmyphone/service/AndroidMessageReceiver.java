package com.artigile.checkmyphone.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import com.artigile.checkmyphone.MainActivity;
import com.artigile.checkmyphone.R;
import com.artigile.checkmyphone.TextToSpeechService;
import com.artigile.howismyphonedoing.api.AndroidMessageProcessor;
import com.artigile.howismyphonedoing.api.MessageParser;
import com.artigile.howismyphonedoing.api.model.DeviceLocationModel;
import com.artigile.howismyphonedoing.api.model.DeviceModel;
import com.artigile.howismyphonedoing.api.model.MessageType;
import com.google.android.gms.location.LocationListener;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;


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
    @Inject
    private TextToSpeechService textToSpeechService;
    @Inject
    private LocationService locationService;
    @Inject
    private MessageParser messageParser;
    @Inject
    private CommonUtilities commonUtilities;
    private String TAG = "AndroidMessageReceiver";

    private static DeviceModel buildPhoneModel() {
        DeviceModel deviceModel = new DeviceModel();
        deviceModel.setBoard(Build.BOARD);
        deviceModel.setModel(Build.MODEL);
        deviceModel.setBootLoader(Build.BOOTLOADER);
        deviceModel.setBrand(Build.BRAND);
        deviceModel.setCpuAbi(Build.CPU_ABI);
        deviceModel.setCpuAbi2(Build.CPU_ABI2);
        deviceModel.setDevice(Build.DEVICE);
        deviceModel.setDisplay(Build.DISPLAY);
        deviceModel.setFingerprint(Build.FINGERPRINT);
        deviceModel.setHardware(Build.HARDWARE);
        deviceModel.setHost(Build.HOST);
        deviceModel.setId(Build.ID);
        deviceModel.setManufacturer(Build.MANUFACTURER);
        deviceModel.setModel(Build.MODEL);
        deviceModel.setProduct(Build.PRODUCT);
        deviceModel.setSerial(Build.SERIAL);
        deviceModel.setTags(Build.TAGS);
        deviceModel.setTime(Build.TIME);
        deviceModel.setType(Build.TYPE);
        deviceModel.setUnknown(Build.UNKNOWN);
        deviceModel.setUser(Build.USER);
        deviceModel.setRadioVersion(Build.getRadioVersion());
        return deviceModel;
    }

    @Override
    public String processMessage(final MessageType messageType, String message) {
        try {
            if (messageType == MessageType.DEVICE_INFO) {
                DeviceModel deviceModel = buildPhoneModel();
                Gson gson = new Gson();
                messageSender.processMessage(MessageType.DEVICE_INFO, gson.toJson(deviceModel));
            } else if (messageType == MessageType.NOTIFY_PHONE) {
                String messageStr = (String) messageParser.parse(messageType, message);
                textToSpeechService.talk(messageStr);
            } else if (messageType == MessageType.GET_DEVICE_LOCATION) {
                Log.v(TAG, "got request to return phone location.");
                locationService.getLocation(new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        DeviceLocationModel deviceLocationModel = new DeviceLocationModel();
                        deviceLocationModel.setAccuracy(location.getAccuracy());
                        deviceLocationModel.setAltitude(location.getAltitude());
                        deviceLocationModel.setBearing(location.getBearing());
                        deviceLocationModel.setElapsedRealtimeNanos(location.getElapsedRealtimeNanos());
                        deviceLocationModel.setHasAccuracy(location.hasAccuracy());
                        deviceLocationModel.setHasAltitude(location.hasAltitude());
                        deviceLocationModel.setHasSpeed(location.hasSpeed());
                        deviceLocationModel.setProvider(location.getProvider());
                        deviceLocationModel.setTime(location.getTime());
                        deviceLocationModel.setLatitude(location.getLatitude());
                        deviceLocationModel.setLongitude(location.getLongitude());
                        deviceLocationModel.setSpeed(location.getSpeed());
                        deviceLocationModel.setHasBearing(location.hasBearing());
                        try {
                            messageSender.processMessage(messageType, messageParser.serialize(deviceLocationModel));
                        } catch (IOException e) {

                        }
                    }
                });
            } else {
                commonUtilities.displayMessage(context, message);
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
        } catch (IOException e) {
            e.printStackTrace();
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
