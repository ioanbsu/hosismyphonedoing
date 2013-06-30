package com.artigile.checkmyphone.service;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import com.artigile.howismyphonedoing.api.model.NetworkType;
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
    private SignalStrength signalStrength;

    public UserDeviceModel getUserDeviceDetails(UserDeviceModel userDeviceModel) {
        signalStrength = null;
        if (userDeviceModel == null) {
            userDeviceModel = new UserDeviceModel();
        }
        populateBatteryData(userDeviceModel);
        TelephonyManager tel = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        userDeviceModel.setOperator(detectSignalStrenghtLevel(tel));
        userDeviceModel.setNetworkType(detectNetworkType(tel));
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

    public String detectSignalStrenghtLevel(TelephonyManager tel) {
        return tel.getSimOperatorName();
    }

    private NetworkType detectNetworkType(TelephonyManager tel) {
        int networkType = tel.getNetworkType();
        if (networkType == TelephonyManager.NETWORK_TYPE_1xRTT) {
            return NetworkType.NETWORK_TYPE_1xRTT;
        } else if (networkType == TelephonyManager.NETWORK_TYPE_CDMA) {
            return NetworkType.NETWORK_TYPE_CDMA;
        } else if (networkType == TelephonyManager.NETWORK_TYPE_EDGE) {
            return NetworkType.NETWORK_TYPE_EDGE;
        } else if (networkType == TelephonyManager.NETWORK_TYPE_EHRPD) {
            return NetworkType.NETWORK_TYPE_EHRPD;
        } else if (networkType == TelephonyManager.NETWORK_TYPE_EVDO_0) {
            return NetworkType.NETWORK_TYPE_EVDO_0;
        } else if (networkType == TelephonyManager.NETWORK_TYPE_EVDO_A) {
            return NetworkType.NETWORK_TYPE_EVDO_A;
        } else if (networkType == TelephonyManager.NETWORK_TYPE_EVDO_B) {
            return NetworkType.NETWORK_TYPE_EVDO_B;
        } else if (networkType == TelephonyManager.NETWORK_TYPE_GPRS) {
            return NetworkType.NETWORK_TYPE_GPRS;
        } else if (networkType == TelephonyManager.NETWORK_TYPE_HSDPA) {
            return NetworkType.NETWORK_TYPE_HSDPA;
        } else if (networkType == TelephonyManager.NETWORK_TYPE_HSPA) {
            return NetworkType.NETWORK_TYPE_HSPA;
        } else if (networkType == TelephonyManager.NETWORK_TYPE_HSPAP) {
            return NetworkType.NETWORK_TYPE_HSPAP;
        } else if (networkType == TelephonyManager.NETWORK_TYPE_HSUPA) {
            return NetworkType.NETWORK_TYPE_HSUPA;
        } else if (networkType == TelephonyManager.NETWORK_TYPE_IDEN) {
            return NetworkType.NETWORK_TYPE_LTE;
        } else if (networkType == TelephonyManager.NETWORK_TYPE_UMTS) {
            return NetworkType.NETWORK_TYPE_UMTS;
        } else if (networkType == TelephonyManager.NETWORK_TYPE_UNKNOWN) {
            return NetworkType.NETWORK_TYPE_UNKNOWN;
        }
        return NetworkType.NETWORK_TYPE_UNKNOWN;
    }


}
