package com.artigile.howismyphonedoing.api.model;

import com.artigile.howismyphonedoing.api.model.battery.BatteryHealthType;
import com.artigile.howismyphonedoing.api.model.battery.BatteryPluggedType;
import com.artigile.howismyphonedoing.api.model.battery.BatteryStatusType;

/**
 * User: ioanbsu
 * Date: 6/11/13
 * Time: 4:35 PM
 */
public class UserDeviceModel implements IUserDeviceModel {

    private String deviceId;

    private String humanReadableName;

    private Float batteryLevel;

    private BatteryHealthType batteryHealthType;

    private BatteryPluggedType batteryPluggedType;

    private BatteryStatusType batteryStatusType;

    private boolean wifiEnabled;

    private boolean bluetoothEnabled;

    private String operator;

    private NetworkType networkType;

    @Override
    public String getDeviceId() {
        return deviceId;
    }

    @Override
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public String getHumanReadableName() {
        return humanReadableName;
    }

    @Override
    public void setHumanReadableName(String humanReadableName) {
        this.humanReadableName = humanReadableName;
    }

    @Override
    public Float getBatteryLevel() {
        return batteryLevel;
    }

    @Override
    public void setBatteryLevel(Float batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    @Override
    public BatteryHealthType getBatteryHealthType() {
        return batteryHealthType;
    }

    @Override
    public void setBatteryHealthType(BatteryHealthType batteryHealthType) {
        this.batteryHealthType = batteryHealthType;
    }

    @Override
    public BatteryPluggedType getBatteryPluggedType() {
        return batteryPluggedType;
    }

    @Override
    public void setBatteryPluggedType(BatteryPluggedType batteryPluggedType) {
        this.batteryPluggedType = batteryPluggedType;
    }

    @Override
    public BatteryStatusType getBatteryStatusType() {
        return batteryStatusType;
    }

    @Override
    public void setBatteryStatusType(BatteryStatusType batteryStatusType) {
        this.batteryStatusType = batteryStatusType;
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

    @Override
    public String getOperator() {
        return operator;
    }

    @Override
    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Override
    public NetworkType getNetworkType() {
        return networkType;
    }

    @Override
    public void setNetworkType(NetworkType networkType) {
        this.networkType = networkType;
    }


}
