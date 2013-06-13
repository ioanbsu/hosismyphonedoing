package com.artigile.howismyphonedoing.api.model;

import java.io.Serializable;

/**
 * User: ioanbsu
 * Date: 6/13/13
 * Time: 10:51 AM
 */
public class MessageToDeviceModel implements Serializable {

    private String message;
    private String deviceId;
    private GwtLocale locale;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public GwtLocale getLocale() {
        return locale;
    }

    public void setLocale(GwtLocale locale) {
        this.locale = locale;
    }
}
