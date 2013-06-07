package com.artigile.howismyphonedoing.api;

import com.artigile.howismyphonedoing.api.model.DeviceModel;
import com.artigile.howismyphonedoing.api.model.DeviceRegistrationModel;
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
    public Serializable parse(MessageType messageType, String serializedString) {
        try {
            if (messageType == MessageType.DEVICE_INFO) {
                return gson.getAdapter(DeviceModel.class).fromJson(serializedString);
            } else if (messageType == MessageType.REGISTER_DEVICE) {
                return gson.getAdapter(DeviceRegistrationModel.class).fromJson(serializedString);
            } else if (messageType == MessageType.UNREGISTER_DEVICE) {
                return gson.getAdapter(DeviceRegistrationModel.class).fromJson(serializedString);
            } else if (messageType == MessageType.NOTIFY_PHONE) {
                return gson.getAdapter(DeviceRegistrationModel.class).fromJson(serializedString);
            } else if (messageType == MessageType.GET_DEVICE_LOCATION) {
                return gson.getAdapter(DeviceRegistrationModel.class).fromJson(serializedString);
            }
            return gson.getAdapter(messageType.getDeserializedClass()).fromJson(serializedString);

        } catch (IOException e) {

        }

        return serializedString;
    }

    @Override
    public String serialize( Serializable object) {
        return gson.toJson(object);
    }

}
