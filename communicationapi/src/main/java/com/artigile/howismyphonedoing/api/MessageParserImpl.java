package com.artigile.howismyphonedoing.api;

import com.artigile.howismyphonedoing.api.model.*;
import com.google.gson.*;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * User: ioanbsu
 * Date: 6/7/13
 * Time: 12:13 PM
 */
public class MessageParserImpl implements MessageParser {
    private Gson gson;

    public MessageParserImpl() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(IDeviceModel.class, new IDeviceModelDeserializer());
        gson = gsonBuilder.create();
    }

    @Override
    public <T extends Serializable> T parse(MessageType messageType, String serializedString) {
        try {
            if (messageType == MessageType.DEVICE_INFO) {
                return (T) gson.getAdapter(DeviceModel.class).fromJson(serializedString);
            } else if (messageType == MessageType.REGISTER_DEVICE) {
                return (T) gson.getAdapter(DeviceRegistrationModel.class).fromJson(serializedString);
            } else if (messageType == MessageType.UNREGISTER_DEVICE) {
                return (T) gson.getAdapter(DeviceRegistrationModel.class).fromJson(serializedString);
            } else if (messageType == MessageType.MESSAGE_TO_DEVICE) {
                return (T) gson.getAdapter(MessageToDeviceModel.class).fromJson(serializedString);
            } else if (messageType == MessageType.GET_DEVICE_LOCATION) {
                return (T) gson.getAdapter(DeviceRegistrationModel.class).fromJson(serializedString);
            } else if (messageType == MessageType.MESSAGE_TYPE_IS_NOT_SUPPORTED) {
                return (T) gson.getAdapter(String.class).fromJson(serializedString);
            }
            return (T) gson.getAdapter(messageType.getDeserializedClass()).fromJson(serializedString);

        } catch (IOException e) {

        }

        return (T) serializedString;
    }

    @Override
    public String serialize(Serializable object) {
        return gson.toJson(object);
    }

    public class IDeviceModelDeserializer implements JsonDeserializer<IDeviceModel> {

        @Override
        public IDeviceModel deserialize(JsonElement json, Type typeofT, JsonDeserializationContext context) throws JsonParseException {
            String strObject = json.toString();
            try {
                IDeviceModel deviceModel = gson.getAdapter(DeviceModel.class).fromJson(strObject);
                return deviceModel;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    }


}
