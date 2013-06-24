package com.artigile.howismyphonedoing.api.model;

import com.artigile.howismyphonedoing.api.model.battery.BatteryHealthType;
import com.artigile.howismyphonedoing.api.model.battery.BatteryPluggedType;
import com.artigile.howismyphonedoing.api.model.battery.BatteryStatusType;

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

    Float getBatteryLevel();

    void setBatteryLevel(Float batteryLevel);

    BatteryHealthType getBatteryHealthType();

    void setBatteryHealthType(BatteryHealthType batteryHealthType);

    BatteryPluggedType getBatteryPluggedType();

    void setBatteryPluggedType(BatteryPluggedType batteryPluggedType);

    BatteryStatusType getBatteryStatusType();

    void setBatteryStatusType(BatteryStatusType batteryStatusType);
}
