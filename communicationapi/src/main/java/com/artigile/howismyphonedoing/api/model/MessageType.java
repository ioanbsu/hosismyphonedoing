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
    MESSAGE_TO_DEVICE {
        @Override
        public Class<? extends Serializable> getDeserializedClass() {
            return MessageToDeviceModel.class;
        }
    },
    GET_DEVICE_LOCATION {
        @Override
        public Class<? extends Serializable> getDeserializedClass() {
            return DeviceLocationModel.class;
        }
    },
    DEVICE_LOCATION_UPDATED {
        @Override
        public Class<? extends Serializable> getDeserializedClass() {
            return DeviceLocationModel.class;
        }
    },
    DEVICE_LOCATION_NOT_POSSIBLE {
        @Override
        public Class<? extends Serializable> getDeserializedClass() {
            return DeviceLocationModel.class;
        }
    },
    DEVICE_DETAILS_INFO {
        @Override
        public Class<? extends Serializable> getDeserializedClass() {
            return UserDeviceModel.class;
        }
    },
    DEVICE_SETTINGS_UPDATE {
        @Override
        public Class<? extends Serializable> getDeserializedClass() {
            return DeviceSettingsModel.class;
        }
    },
    DISPLAY_LOGS {//displays logs on the device
        @Override
        public Class<? extends Serializable> getDeserializedClass() {
            return String.class;
        }
    },
    HIDE_LOGS {//hides logs on the device
        @Override
        public Class<? extends Serializable> getDeserializedClass() {
            return String.class;
        }
    },
    LOCK_DEVICE {//hides logs on the device
        @Override
        public Class<? extends Serializable> getDeserializedClass() {
            return String.class;
        }
    },
    DEVICE_ADMIN_IS_NOT_ENABLED {//hides logs on the device
        @Override
        public Class<? extends Serializable> getDeserializedClass() {
            return String.class;
        }
    },
    TAKE_PICTURE {//hides logs on the device
        @Override
        public Class<? extends Serializable> getDeserializedClass() {
            return TakePictureModel.class;
        }
    },
    PICTURE_READY {//hides logs on the device
        @Override
        public Class<? extends Serializable> getDeserializedClass() {
            return PictureReadyModel.class;
        }
    },
    MESSAGE_TYPE_IS_NOT_SUPPORTED {
        @Override
        public Class<? extends Serializable> getDeserializedClass() {
            return MessageNotSupportedByDeviceResponseModel.class;
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
