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
import android.content.SharedPreferences;
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
    private DeviceDetailsReader deviceDetailsReader;
    @Inject
    private DeviceSettingsService deviceConfigurationService;
    @Inject
    private SharedPreferences prefs;
    @Inject
    private AntiTheftService antiTheftService;
    private String TAG = "AndroidMessageReceiver";

    @Override
    public String processMessage(final MessageType messageType, String serializedObject, AndroidMessageResultListener messageResultListener) throws IOException {
        commonUtilities.displayMessage(context, messageType + "", CommonUtilities.LOG_MESSAGE_TYPE);
        String serializedResonseObject = "";
        if (messageType == MessageType.DEVICE_INFO) {
            IDeviceModel deviceModel = deviceInfoService.buildPhoneModel();
            serializedResonseObject = messageParser.serialize(deviceModel);
        } else if (messageType == MessageType.MESSAGE_TO_DEVICE) {
            MessageToDeviceModel messageToTheDevice = messageParser.parse(messageType, serializedObject);
            generateNotification(messageToTheDevice.getMessage());
            Locale locale = parseLocale(messageToTheDevice.getLocale());
            textToSpeechService.say(locale, messageToTheDevice.getMessage());
            serializedResonseObject = serializedObject;
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
                                        + location.getSpeed(), CommonUtilities.LOG_MESSAGE_TYPE);
                    } catch (Exception e) {
                        failsafeSendMessage(MessageType.DEVICE_LOCATION_NOT_POSSIBLE, messageParser.serialize(deviceLocationModel));
                    }
                    failsafeSendMessage(MessageType.DEVICE_LOCATION_UPDATED, messageParser.serialize(deviceLocationModel));
                }
            });
        } else if (messageType == MessageType.DEVICE_DETAILS_INFO) {
            UserDeviceModel userDeviceModel = deviceDetailsReader.getUserDeviceDetails(null);
            userDeviceModel.setiDeviceSettingsModel(deviceConfigurationService.getDeviceSettings());
            serializedResonseObject = messageParser.serialize(userDeviceModel);
        } else if (messageType == MessageType.DEVICE_SETTINGS_UPDATE) {
            DeviceSettingsModel deviceSettingsModel = messageParser.parse(messageType, serializedObject);
            deviceConfigurationService.updateDeviceSettings(deviceSettingsModel);
            serializedResonseObject = serializedObject;
        } else if (messageType == MessageType.DISPLAY_LOGS) {
            prefs.edit().putBoolean(Constants.DISPLAY_LOGS_FLAG, true).commit();
            commonUtilities.displayMessage(context, serializedObject, CommonUtilities.SHOW_LOGS_STATE_UPDATED);
            return "success";
        } else if (messageType == MessageType.HIDE_LOGS) {
            prefs.edit().putBoolean(Constants.DISPLAY_LOGS_FLAG, false).commit();
            commonUtilities.displayMessage(context, serializedObject, CommonUtilities.SHOW_LOGS_STATE_UPDATED);
            return "success";
        } else if (messageType == MessageType.LOCK_DEVICE) {
            try {
                LockDeviceScreenModel lockDeviceScreenModel=messageParser.parse(messageType, serializedObject);
                antiTheftService.lockDevice(lockDeviceScreenModel);
            } catch (DeviceAdminIsNotEnabledException e) {
                failsafeSendMessage(MessageType.DEVICE_ADMIN_IS_NOT_ENABLED, "");
                return "failed to lock the device. not supported.";
            }
        }else if (messageType == MessageType.TAKE_PICTURE) {
            try {
                TakePictureModel lockDeviceScreenModel=messageParser.parse(messageType, serializedObject);
                antiTheftService.takePicture(lockDeviceScreenModel);
            }catch (DeviceHasNoCameraException e) {
                //todo: process here
            }
        } else {
            commonUtilities.displayMessage(context, serializedObject, CommonUtilities.LOG_MESSAGE_TYPE);
            // notifies user
            generateNotification(serializedObject);
        }
        failsafeSendMessage(messageType, serializedResonseObject);

        return "message had been successfully sent";
    }

    private void failsafeSendMessage(MessageType messageType, String selializedObject) {
        try {
            messageSender.processMessage(messageType, selializedObject, null);
        } catch (IOException e) {
            commonUtilities.displayMessage(context, "Failed to send a message to server: " + messageType, CommonUtilities.LOG_MESSAGE_TYPE);
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
