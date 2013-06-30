package com.artigile.checkmyphone.service;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.util.Log;
import com.artigile.howismyphonedoing.api.model.DeviceSettingsModel;
import com.artigile.howismyphonedoing.api.model.IDeviceSettingsModel;
import com.artigile.howismyphonedoing.api.model.RingerMode;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * User: ioanbsu
 * Date: 6/25/13
 * Time: 5:28 PM
 */
@Singleton
public class DeviceSettingsService {

    public static final String TAG = "DeviceSettingsService";

    @Inject
    private Context context;
    @Inject
    private DeviceDetailsReader deviceDetailsReader;

    public void updateRingerMode(RingerMode ringerMode) {
        if (ringerMode == null) {
            return;
        }
        try {
            AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            if (ringerMode == RingerMode.RINGER_MODE_SILENT) {
                audio.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            } else if (ringerMode == RingerMode.RINGER_MODE_NORMAL) {
                audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            } else if (ringerMode == RingerMode.RINGER_MODE_VIBRATE) {
                audio.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            }
        } catch (Exception e) {
            Log.w("DeviceSettingsService", "Failed to set ringer mode:" + ringerMode);
        }
    }

    public void updateWifiState(boolean enable) {
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            wifiManager.setWifiEnabled(enable);
        } catch (Exception e) {
            Log.w(TAG, "Failed to updated wifi state");
        }
    }

    public boolean isWifiEnabled() {
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            return wifiManager.isWifiEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    public RingerMode getRingerMode() {
        try {
            AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            if (audio.getRingerMode() == AudioManager.RINGER_MODE_SILENT) {
                return RingerMode.RINGER_MODE_SILENT;
            } else if (audio.getRingerMode() == AudioManager.RINGER_MODE_NORMAL) {
                return RingerMode.RINGER_MODE_NORMAL;
            } else if (audio.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE) {
                return RingerMode.RINGER_MODE_VIBRATE;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public void updateBluetoothEnabled(boolean isEnabled) {
        try {
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (isEnabled) {
                if (mBluetoothAdapter != null && !isBluetoothEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    enableBtIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(enableBtIntent);
                }
            } else {
                mBluetoothAdapter.cancelDiscovery();
                mBluetoothAdapter.disable();
            }
        } catch (Exception e) {
            Log.w(TAG, "Failed to update bluetooth state");
        }
    }

    public boolean isBluetoothEnabled() {
        try {
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBluetoothAdapter == null) {
                return false;
            } else {
                return mBluetoothAdapter.isEnabled();
            }
        } catch (Exception e) {
            return false;
        }
    }


    public void setAlarm() {
        /*AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, YourBroadcastReceiver.class);
        intent.setAction(define action here);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, intent, 0);
        alarmManager.set(AlarmManager.RTC, time.getTimeInMillis(),pendingIntent );*/
    }

    public IDeviceSettingsModel getDeviceSettings() {
        IDeviceSettingsModel iDeviceSettingsModel = new DeviceSettingsModel();
        iDeviceSettingsModel.setRingerMode(getRingerMode());
        iDeviceSettingsModel.setWifiEnabled(isWifiEnabled());
        iDeviceSettingsModel.setBluetoothEnabled(isBluetoothEnabled());
        return iDeviceSettingsModel;
    }

    public void updateDeviceSettings(DeviceSettingsModel deviceSettingsModel) {
        updateRingerMode(deviceSettingsModel.getRingerMode());
        updateWifiState(deviceSettingsModel.isWifiEnabled());
        updateBluetoothEnabled(deviceSettingsModel.isBluetoothEnabled());

    }
}
