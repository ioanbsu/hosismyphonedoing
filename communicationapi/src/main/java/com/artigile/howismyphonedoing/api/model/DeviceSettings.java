package com.artigile.howismyphonedoing.api.model;

/**
 * User: ioanbsu
 * Date: 6/25/13
 * Time: 5:45 PM
 */
public class DeviceSettings implements IDeviceSettings {

    private RingerMode ringerMode;

    @Override
    public RingerMode getRingerMode() {
        return ringerMode;
    }

    @Override
    public void setRingerMode(RingerMode ringerMode) {
        this.ringerMode = ringerMode;
    }
}
