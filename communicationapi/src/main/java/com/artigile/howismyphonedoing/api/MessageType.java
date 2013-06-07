package com.artigile.howismyphonedoing.api;

import com.artigile.howismyphonedoing.api.model.DeviceRegistrationModel;
import com.artigile.howismyphonedoing.api.model.PhoneModel;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.Serializable;

/**
 * User: ioanbsu
 * Date: 5/29/13
 * Time: 5:22 PM
 */
public enum MessageType {
    PHONE_INFO {
        public Serializable getValue(String serializedString) throws IOException {
            return new Gson().getAdapter(PhoneModel.class).fromJson(serializedString);
        }
    },
    REGISTER_DEVICE {
        public Serializable getValue(String serializedString) throws IOException {
            return new Gson().getAdapter(DeviceRegistrationModel.class).fromJson(serializedString);
        }
    },
    UNREGISTER_DEVICE {
        public Serializable getValue(String serializedString) throws IOException {
            return new Gson().getAdapter(DeviceRegistrationModel.class).fromJson(serializedString);
        }
    },
    NOTIFY_PHONE {
        @Override
        public Serializable getValue(String serializedString) throws IOException {
            return serializedString;
        }
    },
    GET_PHONE_LOCATION {
        @Override
        public Serializable getValue(String serializedString) throws IOException {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }
    };

    public boolean of(String stringValue) {
        if (stringValue == null) {
            return false;
        }
        return MessageType.valueOf(stringValue) == this;
    }

    public abstract Serializable getValue(String serializedString) throws IOException;

    public String convertModelToString(Serializable object) {
        return new Gson().toJson(object);

    }
}
