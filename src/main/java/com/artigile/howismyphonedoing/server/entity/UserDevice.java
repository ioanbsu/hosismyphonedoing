package com.artigile.howismyphonedoing.server.entity;

/**
 * @author IoaN, 5/28/13 9:25 PM
 */
public class UserDevice {

    private String registeredId;

    private String deviceName;

    private String deviceModel;

    public String getRegisteredId() {
        return registeredId;
    }

    public void setRegisteredId(String registeredId) {
        this.registeredId = registeredId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDevice that = (UserDevice) o;

        if (registeredId != null ? !registeredId.equals(that.registeredId) : that.registeredId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return registeredId != null ? registeredId.hashCode() : 0;
    }
}
