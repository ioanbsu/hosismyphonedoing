package com.artigile.howismyphonedoing.api;

/**
 * User: ioanbsu
 * Date: 5/29/13
 * Time: 5:22 PM
 */
public enum MessageType {
    PHONE_INFO,
    REGISTER_DEVICE,
    UNREGISTER_DEVICE;

    public boolean of(String stringValue) {
        if (stringValue == null) {
            return false;
        }
        return MessageType.valueOf(stringValue) == this;
    }
}
