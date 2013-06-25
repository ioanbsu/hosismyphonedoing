package com.artigile.checkmyphone.service;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import com.artigile.howismyphonedoing.api.model.UserDeviceModel;
import com.artigile.howismyphonedoing.api.model.battery.BatteryHealthType;
import com.artigile.howismyphonedoing.api.model.battery.BatteryPluggedType;
import com.artigile.howismyphonedoing.api.model.battery.BatteryStatusType;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Date: 6/23/13
 * Time: 6:50 PM
 *
 * @author ioanbsu
 */

@Singleton
public class DeviceDetailsReader {

    @Inject
    private Context context;


    public UserDeviceModel getUserDeviceDetails(UserDeviceModel userDeviceModel) {
        if (userDeviceModel == null) {
            userDeviceModel = new UserDeviceModel();
        }
        populateBatteryData(userDeviceModel);
        return userDeviceModel;
    }

    private void populateBatteryData(UserDeviceModel userDeviceModel) {
        Intent batteryIntent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        int batteryPlugged = batteryIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        int batteryHealth = batteryIntent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);
        int batteryStatus = batteryIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

        //=====Battery Health======
        if (batteryHealth == BatteryManager.BATTERY_HEALTH_COLD) {
            userDeviceModel.setBatteryHealthType(BatteryHealthType.BATTERY_HEALTH_COLD);
        } else if (batteryHealth == BatteryManager.BATTERY_HEALTH_DEAD) {
            userDeviceModel.setBatteryHealthType(BatteryHealthType.BATTERY_HEALTH_DEAD);
        } else if (batteryHealth == BatteryManager.BATTERY_HEALTH_GOOD) {
            userDeviceModel.setBatteryHealthType(BatteryHealthType.BATTERY_HEALTH_GOOD);
        } else if (batteryHealth == BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE) {
            userDeviceModel.setBatteryHealthType(BatteryHealthType.BATTERY_HEALTH_OVER_VOLTAGE);
        } else if (batteryHealth == BatteryManager.BATTERY_HEALTH_OVERHEAT) {
            userDeviceModel.setBatteryHealthType(BatteryHealthType.BATTERY_HEALTH_OVERHEAT);
        } else if (batteryHealth == BatteryManager.BATTERY_HEALTH_UNKNOWN) {
            userDeviceModel.setBatteryHealthType(BatteryHealthType.BATTERY_HEALTH_UNKNOWN);
        } else if (batteryHealth == BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE) {
            userDeviceModel.setBatteryHealthType(BatteryHealthType.BATTERY_HEALTH_UNSPECIFIED_FAILURE);
        }

        //=====Battery Plugged======
        if (batteryPlugged == BatteryManager.BATTERY_PLUGGED_AC) {
            userDeviceModel.setBatteryPluggedType(BatteryPluggedType.BATTERY_PLUGGED_AC);
        } else if (batteryPlugged == BatteryManager.BATTERY_PLUGGED_USB) {
            userDeviceModel.setBatteryPluggedType(BatteryPluggedType.BATTERY_PLUGGED_USB);
        } else if (batteryPlugged == BatteryManager.BATTERY_PLUGGED_WIRELESS) {
            userDeviceModel.setBatteryPluggedType(BatteryPluggedType.BATTERY_PLUGGED_WIRELESS);
        }

        //=====Battery Status======
        if (batteryStatus == BatteryManager.BATTERY_STATUS_CHARGING) {
            userDeviceModel.setBatteryStatusType(BatteryStatusType.BATTERY_STATUS_CHARGING);
        } else if (batteryStatus == BatteryManager.BATTERY_STATUS_DISCHARGING) {
            userDeviceModel.setBatteryStatusType(BatteryStatusType.BATTERY_STATUS_DISCHARGING);
        } else if (batteryStatus == BatteryManager.BATTERY_STATUS_FULL) {
            userDeviceModel.setBatteryStatusType(BatteryStatusType.BATTERY_STATUS_FULL);
        } else if (batteryStatus == BatteryManager.BATTERY_STATUS_NOT_CHARGING) {
            userDeviceModel.setBatteryStatusType(BatteryStatusType.BATTERY_STATUS_NOT_CHARGING);
        } else if (batteryStatus == BatteryManager.BATTERY_STATUS_UNKNOWN) {
            userDeviceModel.setBatteryStatusType(BatteryStatusType.BATTERY_STATUS_UNKNOWN);
        }

        // Error checking that probably isn't needed but I added just in case.
        if (level == -1 || scale == -1) {
            userDeviceModel.setBatteryLevel(50.0f);
        }
        userDeviceModel.setBatteryLevel(((float) level / (float) scale) * 100.0f);
    }
}
