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

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import com.artigile.checkmyphone.MainActivity;
import com.artigile.checkmyphone.R;
import com.artigile.checkmyphone.TextToSpeechService;
import com.artigile.howismyphonedoing.api.AndroidMessageProcessor;
import com.artigile.howismyphonedoing.api.AndroidMessageResultListener;
import com.artigile.howismyphonedoing.api.MessageParser;
import com.artigile.howismyphonedoing.api.model.*;
import com.google.android.gms.location.LocationListener;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.Locale;


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
    @Inject
    private DeviceInfoService deviceInfoService;
    @Inject
    private DeviceDetailsReader UserPhoneDetailsReader;
    @Inject
    private DeviceDetailsReader deviceDetailsReader;
    @Inject
    private DeviceConfigurationService deviceConfigurationService;
    private String TAG = "AndroidMessageReceiver";
    private AndroidMessageResultListener androidMessageResultListener;

    @Override
    public String processMessage(final MessageType messageType, String serializedObject, AndroidMessageResultListener messageResultListener) throws IOException {
        this.androidMessageResultListener = messageResultListener;
        commonUtilities.displayMessage(context, messageType + "");
        if (messageType == MessageType.DEVICE_INFO) {
            IDeviceModel deviceModel = deviceInfoService.buildPhoneModel();
            failsafeSendMessage(MessageType.DEVICE_INFO, messageParser.serialize(deviceModel));
        } else if (messageType == MessageType.MESSAGE_TO_DEVICE) {
            MessageToDeviceModel messageToTheDevice = messageParser.parse(messageType, serializedObject);
            generateNotification(messageToTheDevice.getMessage());
            Locale locale = parseLocale(messageToTheDevice.getLocale());
            textToSpeechService.say(locale, messageToTheDevice.getMessage());
            failsafeSendMessage(messageType, serializedObject);
        } else if (messageType == MessageType.GET_DEVICE_LOCATION) {
            Log.v(TAG, "got request to return phone location.");
            locationService.getLocation(new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    DeviceLocationModel deviceLocationModel = new DeviceLocationModel();
                    try {
                        IDeviceModel deviceModel = deviceInfoService.buildPhoneModel();
                        deviceLocationModel.setDeviceName(deviceModel.getModel());
                        deviceLocationModel.setAccuracy(location.getAccuracy());
                        deviceLocationModel.setAltitude(location.getAltitude());
                        deviceLocationModel.setBearing(location.getBearing());
//                        deviceLocationModel.setElapsedRealtimeNanos(location.getElapsedRealtimeNanos());
                        deviceLocationModel.setHasAccuracy(location.hasAccuracy());
                        deviceLocationModel.setHasAltitude(location.hasAltitude());
                        deviceLocationModel.setHasSpeed(location.hasSpeed());
                        deviceLocationModel.setProvider(location.getProvider());
                        deviceLocationModel.setTime(location.getTime());
                        deviceLocationModel.setLatitude(location.getLatitude());
                        deviceLocationModel.setLongitude(location.getLongitude());
                        deviceLocationModel.setSpeed(location.getSpeed());
                        deviceLocationModel.setHasBearing(location.hasBearing());
                        commonUtilities.displayMessage(context,
                                "Accuracy: " + location.getAccuracy() + ", Bearing:" + location.getBearing() + ", Speed:"
                                        + location.getSpeed());
                    } catch (Exception e) {
                        failsafeSendMessage(MessageType.DEVICE_LOCATION_NOT_POSSIBLE, messageParser.serialize(deviceLocationModel));
                    }
                    failsafeSendMessage(messageType, messageParser.serialize(deviceLocationModel));
                }
            });
        } else if (messageType == MessageType.DEVICE_DETAILS_INFO) {
            UserDeviceModel userDeviceModel = deviceDetailsReader.getUserDeviceDetails(null);
            failsafeSendMessage(messageType, messageParser.serialize(userDeviceModel));
        } else if (messageType == MessageType.DEVICE_SETTINGS_UPDATE) {
            DeviceSettings deviceSettings= messageParser.parse(messageType, serializedObject);
            if(deviceSettings.getRingerMode()==RingerMode.RINGER_MODE_SILENT){
                deviceConfigurationService.enableSilentMode(true);
            }else if(deviceSettings.getRingerMode()==RingerMode.RINGER_MODE_NORMAL){
                deviceConfigurationService.enableSilentMode(false);
            }
        } else {
            commonUtilities.displayMessage(context, serializedObject);
            // notifies user
            generateNotification(serializedObject);
        }

        return "message had been successfully sent";
    }

    private void failsafeSendMessage(MessageType messageType, String selializedObject) {
        try {
            messageSender.processMessage(messageType, selializedObject, null);
        } catch (IOException e) {
            commonUtilities.displayMessage(context, "Failed to send a message to server: " + messageType);
        }
    }

    private Locale parseLocale(GwtLocale locale) {
        if (locale == GwtLocale.US) {
            return Locale.US;
        } else if (locale == GwtLocale.ENGLISH) {
            return Locale.ENGLISH;
        } else if (locale == GwtLocale.CANADA) {
            return Locale.CANADA;
        } else if (locale == GwtLocale.FRENCH) {
            return Locale.FRENCH;
        } else if (locale == GwtLocale.GERMAN) {
            return Locale.GERMAN;
        } else if (locale == GwtLocale.ITALIAN) {
            return Locale.ITALIAN;
        } /*else if (locale == GwtLocale.JAPANESE) {
            return Locale.JAPANESE;
        } else if (locale == GwtLocale.KOREAN) {
            return Locale.KOREAN;
        } else if (locale == GwtLocale.CHINESE) {
            return Locale.CHINESE;
        } else if (locale == GwtLocale.SIMPLIFIED_CHINESE) {
            return Locale.SIMPLIFIED_CHINESE;
        } else if (locale == GwtLocale.TAIWAN) {
            return Locale.TAIWAN;
        }*/ else if (locale == GwtLocale.UK) {
            return Locale.UK;
        } else if (locale == GwtLocale.CANADA_FRENCH) {
            return Locale.CANADA_FRENCH;
        }
        return Locale.getDefault();
    }

    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    private void generateNotification(String message) {
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
