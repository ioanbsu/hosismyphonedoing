package com.artigile.howismyphonedoing.api.model;

import java.io.Serializable;

/**
 * User: ioanbsu
 * Date: 6/11/13
 * Time: 4:35 PM
 */
public class UserDeviceModel implements IUserDeviceModel {

    private String deviceId;

    @Override
    public String getDeviceId() {
        return deviceId;
    }

    @Override
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
