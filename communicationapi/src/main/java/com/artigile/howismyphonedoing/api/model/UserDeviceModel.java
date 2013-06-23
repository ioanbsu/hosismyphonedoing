package com.artigile.howismyphonedoing.api.model;

/**
 * User: ioanbsu
 * Date: 6/11/13
 * Time: 4:35 PM
 */
public class UserDeviceModel implements IUserDeviceModel {

    private String deviceId;

    private String humanReadableName;

    private Float batteryLevel;

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
}
