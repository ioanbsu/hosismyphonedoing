package com.artigile.howismyphonedoing.api;

import com.artigile.howismyphonedoing.api.model.DeviceModel;
import com.artigile.howismyphonedoing.api.model.DeviceRegistrationModel;
import com.artigile.howismyphonedoing.api.model.MessageToDeviceModel;
import com.artigile.howismyphonedoing.api.model.MessageType;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.Serializable;

/**
 * User: ioanbsu
 * Date: 6/7/13
 * Time: 12:13 PM
 */
public class MessageParserImpl implements MessageParser {
    private Gson gson = new Gson();

    @Override
    public <T extends Serializable> T parse(MessageType messageType, String serializedString) {
        try {
            if (messageType == MessageType.DEVICE_INFO) {
                return (T) gson.getAdapter(DeviceModel.class).fromJson(serializedString);
            } else if (messageType == MessageType.REGISTER_DEVICE) {
                return (T)gson.getAdapter(DeviceRegistrationModel.class).fromJson(serializedString);
            } else if (messageType == MessageType.UNREGISTER_DEVICE) {
                return (T)gson.getAdapter(DeviceRegistrationModel.class).fromJson(serializedString);
            } else if (messageType == MessageType.MESSAGE_TO_DEVICE) {
                return (T)gson.getAdapter(MessageToDeviceModel.class).fromJson(serializedString);
            } else if (messageType == MessageType.GET_DEVICE_LOCATION) {
                return (T)gson.getAdapter(DeviceRegistrationModel.class).fromJson(serializedString);
            }else if (messageType == MessageType.MESSAGE_TYPE_IS_NOT_SUPPORTED) {
                return (T)gson.getAdapter(String.class).fromJson(serializedString);
            }
            return (T)gson.getAdapter(messageType.getDeserializedClass()).fromJson(serializedString);

        } catch (IOException e) {

        }

        return (T)serializedString;
    }

    @Override
    public String serialize( Serializable object) {
        return gson.toJson(object);
    }

}
