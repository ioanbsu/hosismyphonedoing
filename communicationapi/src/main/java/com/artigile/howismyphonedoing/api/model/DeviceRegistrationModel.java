package com.artigile.howismyphonedoing.api.model;

/**
 * @author IoaN, 6/3/13 9:13 PM
 */
public class DeviceRegistrationModel implements IDeviceRegistrationModel {
    private String userEmail;
    private String deviceCloudRegistrationId;

    private IDeviceModel deviceModel;

    @Override
    public String getUserEmail() {
        return userEmail;
    }

    @Override
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @Override
    public String getDeviceCloudRegistrationId() {
        return deviceCloudRegistrationId;
    }

    @Override
    public void setDeviceCloudRegistrationId(String deviceCloudRegistrationId) {
        this.deviceCloudRegistrationId = deviceCloudRegistrationId;
    }

    @Override
    public IDeviceModel getDeviceModel() {
        return deviceModel;
    }

    @Override
    public void setDeviceModel(IDeviceModel deviceModel) {
        this.deviceModel = deviceModel;
    }
}
