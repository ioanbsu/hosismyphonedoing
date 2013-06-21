package com.artigile.howismyphonedoing.api.model;

import java.io.Serializable;

/**
 * User: ioanbsu
 * Date: 6/20/13
 * Time: 7:55 PM
 */
public interface IMessageToDeviceModel extends Serializable {
    String getMessage();

    void setMessage(String message);

    String getDeviceId();

    void setDeviceId(String deviceId);

    GwtLocale getLocale();

    void setLocale(GwtLocale locale);

}
