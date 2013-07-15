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
    private Gson gsonParser;
    private Gson gsonSerializer = new Gson();

    public MessageParserImpl() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(IDeviceModel.class, new IDeviceModelDeserializer());
        gsonBuilder.registerTypeAdapter(IDeviceSettingsModel.class, new IDeviceSettingsModelDeserializer());
        gsonBuilder.registerTypeAdapter(ITakePictureModel.class, new ITakePictureModelModelDeserializer());
        gsonParser = gsonBuilder.create();
    }

    @Override
    public <T extends Serializable> T parse(MessageType messageType, String serializedString) {
        try {
            if (messageType == MessageType.DEVICE_INFO) {
                return (T) gsonParser.getAdapter(DeviceModel.class).fromJson(serializedString);
            } else if (messageType == MessageType.REGISTER_DEVICE) {
                return (T) gsonParser.getAdapter(DeviceRegistrationModel.class).fromJson(serializedString);
            } else if (messageType == MessageType.UNREGISTER_DEVICE) {
                return (T) gsonParser.getAdapter(DeviceRegistrationModel.class).fromJson(serializedString);
            } else if (messageType == MessageType.MESSAGE_TO_DEVICE) {
                return (T) gsonParser.getAdapter(MessageToDeviceModel.class).fromJson(serializedString);
            } else if (messageType == MessageType.GET_DEVICE_LOCATION) {
                return (T) gsonParser.getAdapter(DeviceRegistrationModel.class).fromJson(serializedString);
            } else if (messageType == MessageType.MESSAGE_TYPE_IS_NOT_SUPPORTED) {
                return (T) gsonParser.getAdapter(String.class).fromJson(serializedString);
            } else if (messageType == MessageType.LOCK_DEVICE) {
                return (T) gsonParser.getAdapter(LockDeviceScreenModel.class).fromJson(serializedString);
            } else if (messageType == MessageType.TAKE_PICTURE) {
                return (T) gsonParser.getAdapter(TakePictureModel.class).fromJson(serializedString);
            }
            return (T) gsonParser.getAdapter(messageType.getDeserializedClass()).fromJson(serializedString);

        } catch (IOException e) {

        }

        return (T) serializedString;
    }

    @Override
    public String serialize(Serializable object) {
        return gsonSerializer.toJson(object);
    }

    public class IDeviceModelDeserializer implements JsonDeserializer<IDeviceModel> {

        @Override
        public IDeviceModel deserialize(JsonElement json, Type typeofT, JsonDeserializationContext context) throws JsonParseException {
            String strObject = json.toString();
            try {
                IDeviceModel deviceModel = gsonParser.getAdapter(DeviceModel.class).fromJson(strObject);
                return deviceModel;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class IDeviceSettingsModelDeserializer implements JsonDeserializer<IDeviceSettingsModel> {

        @Override
        public IDeviceSettingsModel deserialize(JsonElement json, Type typeofT, JsonDeserializationContext context) throws JsonParseException {
            String strObject = json.toString();
            try {
                IDeviceSettingsModel iDeviceSettingsModel = gsonParser.getAdapter(DeviceSettingsModel.class).fromJson(strObject);
                return iDeviceSettingsModel;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class ITakePictureModelModelDeserializer implements JsonDeserializer<ITakePictureModel> {

        @Override
        public ITakePictureModel deserialize(JsonElement json, Type typeofT, JsonDeserializationContext context) throws JsonParseException {
            String strObject = json.toString();
            try {
                ITakePictureModel takePictureModel = gsonParser.getAdapter(TakePictureModel.class).fromJson(strObject);
                return takePictureModel;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


}
