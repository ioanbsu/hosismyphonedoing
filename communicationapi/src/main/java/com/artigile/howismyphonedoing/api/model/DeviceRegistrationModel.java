package com.artigile.howismyphonedoing.api.model;

import java.io.Serializable;

/**
 * @author IoaN, 6/3/13 9:13 PM
 */
public class DeviceRegistrationModel implements Serializable{
    private String userEmail;

    private String deviceCloudRegistrationId;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getDeviceCloudRegistrationId() {
        return deviceCloudRegistrationId;
    }

    public void setDeviceCloudRegistrationId(String deviceCloudRegistrationId) {
        this.deviceCloudRegistrationId = deviceCloudRegistrationId;
    }
}
