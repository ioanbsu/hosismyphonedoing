package com.artigile.howismyphonedoing.api.model;

/**
 * User: ioanbsu
 * Date: 6/13/13
 * Time: 12:57 PM
 */
public class MessageNotSupportedByDeviceResponseModel implements IMessageNotSupportedByDeviceResponseModel {

    private String messageType;
    private String deviceUid;


    public MessageNotSupportedByDeviceResponseModel() {
    }

    public MessageNotSupportedByDeviceResponseModel(String messageType) {
        this.messageType = messageType;
    }

    @Override
    public String getMessageType() {
        return messageType;
    }

    @Override
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
}
