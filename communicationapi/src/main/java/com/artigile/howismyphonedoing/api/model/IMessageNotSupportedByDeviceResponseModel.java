package com.artigile.howismyphonedoing.api.model;

import java.io.Serializable;

/**
 * User: ioanbsu
 * Date: 6/13/13
 * Time: 1:02 PM
 */
public interface IMessageNotSupportedByDeviceResponseModel extends Serializable {
    String getMessageType();

    void setMessageType(String messageType);
}
