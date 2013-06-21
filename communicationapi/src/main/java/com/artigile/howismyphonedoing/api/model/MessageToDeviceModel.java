package com.artigile.howismyphonedoing.api.model;

import java.io.Serializable;

/**
 * User: ioanbsu
 * Date: 6/13/13
 * Time: 10:51 AM
 */
public class MessageToDeviceModel implements IMessageToDeviceModel {

    private String message;
    private String deviceId;
    private GwtLocale locale;
    private long timestamp;

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String getDeviceId() {
        return deviceId;
    }

    @Override
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public GwtLocale getLocale() {
        return locale;
    }

    @Override
    public void setLocale(GwtLocale locale) {
        this.locale = locale;
    }

}
