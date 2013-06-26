package com.artigile.howismyphonedoing.api.model;

import java.io.Serializable;

/**
 * User: ioanbsu
 * Date: 6/25/13
 * Time: 5:47 PM
 */
public interface IDeviceSettingsModel extends Serializable{
    RingerMode getRingerMode();

    void setRingerMode(RingerMode ringerMode);
}
