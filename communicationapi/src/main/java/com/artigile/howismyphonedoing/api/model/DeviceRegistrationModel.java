package com.artigile.howismyphonedoing.api.model;

/**
 * @author IoaN, 6/3/13 9:13 PM
 */
public class DeviceRegistrationModel {
    private String userEmail;

    private String deviceUuid;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getDeviceUuid() {
        return deviceUuid;
    }

    public void setDeviceUuid(String deviceUuid) {
        this.deviceUuid = deviceUuid;
    }
}
