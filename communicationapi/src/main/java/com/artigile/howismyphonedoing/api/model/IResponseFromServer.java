package com.artigile.howismyphonedoing.api.model;

import java.io.Serializable;

/**
 * User: ioanbsu
 * Date: 6/13/13
 * Time: 9:04 AM
 */
public interface IResponseFromServer extends Serializable{
    MessageType getMessageType();

    void setMessageType(MessageType messageType);

    String getSerializedObject();

    void setSerializedObject(String serializedObject);
}
