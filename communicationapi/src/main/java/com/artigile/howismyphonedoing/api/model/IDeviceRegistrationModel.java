package com.artigile.howismyphonedoing.api.model;

import java.io.Serializable;

/**
 * User: ioanbsu
 * Date: 6/13/13
 * Time: 9:01 AM
 */
public interface IDeviceRegistrationModel extends Serializable {
    String getUserEmail();

    void setUserEmail(String userEmail);

    String getDeviceCloudRegistrationId();

    void setDeviceCloudRegistrationId(String deviceCloudRegistrationId);

    IDeviceModel getDeviceModel();

    void setDeviceModel(IDeviceModel deviceModel);
}
