package com.artigile.howismyphonedoing.api.model;

/**
 * User: ioanbsu
 * Date: 6/13/13
 * Time: 9:03 AM
 */
public class ResponseFromServer implements IResponseFromServer {

    private MessageType messageType;

    private IUserDeviceModel userDeviceModel;

    private String serializedObject;

    @Override
    public MessageType getMessageType() {
        return messageType;
    }

    @Override
    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    @Override
    public String getSerializedObject() {
        return serializedObject;
    }

    @Override
    public void setSerializedObject(String serializedObject) {
        this.serializedObject = serializedObject;
    }

    public IUserDeviceModel getUserDeviceModel() {
        return userDeviceModel;
    }

    public void setUserDeviceModel(IUserDeviceModel userDeviceModel) {
        this.userDeviceModel = userDeviceModel;
    }
}
