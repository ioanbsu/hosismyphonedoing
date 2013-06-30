package com.artigile.howismyphonedoing.api.model;

/**
 * User: ioanbsu
 * Date: 6/25/13
 * Time: 5:45 PM
 */
public class DeviceSettingsModel implements IDeviceSettingsModel {

    private RingerMode ringerMode;

    private boolean wifiEnabled;

    private boolean bluetoothEnabled;

    @Override
    public RingerMode getRingerMode() {
        return ringerMode;
    }

    @Override
    public void setRingerMode(RingerMode ringerMode) {
        this.ringerMode = ringerMode;
    }

    @Override
    public boolean isWifiEnabled() {
        return wifiEnabled;
    }

    @Override
    public void setWifiEnabled(boolean wifiEnabled) {
        this.wifiEnabled = wifiEnabled;
    }

    @Override
    public boolean isBluetoothEnabled() {
        return bluetoothEnabled;
    }

    @Override
    public void setBluetoothEnabled(boolean bluetoothEnabled) {
        this.bluetoothEnabled = bluetoothEnabled;
    }
}
