package com.artigile.howismyphonedoing.api.model;

import java.io.Serializable;

/**
 * User: ioanbsu
 * Date: 5/29/13
 * Time: 5:22 PM
 */
public enum MessageType {
    DEVICE_INFO {
        @Override
        public Class<? extends Serializable> getDeserializedClass() {
            return DeviceModel.class;
        }
    },
    REGISTER_DEVICE {
        @Override
        public Class<? extends Serializable> getDeserializedClass() {
            return DeviceRegistrationModel.class;
        }
    },
    UNREGISTER_DEVICE {
        @Override
        public Class<? extends Serializable> getDeserializedClass() {
            return DeviceRegistrationModel.class;
        }
    },
    NOTIFY_PHONE {
        @Override
        public Class<? extends Serializable> getDeserializedClass() {
            return String.class;
        }
    },
    GET_DEVICE_LOCATION {
        @Override
        public Class<? extends Serializable> getDeserializedClass() {
            return DeviceLocationModel.class;
        }
    };

    public boolean of(String stringValue) {
        if (stringValue == null) {
            return false;
        }
        return MessageType.valueOf(stringValue) == this;
    }


    public abstract Class<? extends Serializable> getDeserializedClass();
}
