package com.artigile.howismyphonedoing.server.entity;

/**
 * @author IoaN, 5/28/13 9:25 PM
 */
public class UserDevice {
    private String userEmail;

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

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDevice that = (UserDevice) o;

        if (registeredId != null ? !registeredId.equals(that.registeredId) : that.registeredId != null) return false;
        if (userEmail != null ? !userEmail.equals(that.userEmail) : that.userEmail != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userEmail != null ? userEmail.hashCode() : 0;
        result = 31 * result + (registeredId != null ? registeredId.hashCode() : 0);
        return result;
    }
}
