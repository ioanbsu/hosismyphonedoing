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
    LOCK_DEVICE {//locks the device and changes pin code(if new pin code is specified)

        @Override
        public Class<? extends Serializable> getDeserializedClass() {
            return String.class;
        }
    },
    WIPE_DEVICE {//wipes the device. After that the device won't be available anymore.

        @Override
        public Class<? extends Serializable> getDeserializedClass() {
            return String.class;
        }
    },
    DEVICE_ADMIN_IS_NOT_ENABLED {//message that notifies that logs are disabled.

        @Override
        public Class<? extends Serializable> getDeserializedClass() {
            return String.class;
        }
    },
    TAKE_PICTURE {//takes picture on the device

        @Override
        public Class<? extends Serializable> getDeserializedClass() {
            return TakePictureModel.class;
        }
    },
    PICTURE_READY {//the message that contains picture byte data with it. Encoded in Json. Should be updated later to send pure bytes.

        @Override
        public Class<? extends Serializable> getDeserializedClass() {
            return PictureReadyModel.class;
        }
    },
    MESSAGE_TYPE_IS_NOT_SUPPORTED {//message sent from the device when the device reports that message type that it got is not supported.

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
