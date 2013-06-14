package com.artigile.howismyphonedoing.api.model;

import java.io.Serializable;

/**
 * User: ioanbsu
 * Date: 6/13/13
 * Time: 1:32 PM
 */
public interface IUserDeviceModel extends Serializable {
    String getDeviceId();

    void setDeviceId(String deviceId);

    String getHumanReadableName();

    void setHumanReadableName(String humanReadableName);
}
