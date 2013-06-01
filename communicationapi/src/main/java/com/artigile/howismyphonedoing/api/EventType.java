package com.artigile.howismyphonedoing.api;

/**
 * User: ioanbsu
 * Date: 5/29/13
 * Time: 5:22 PM
 */
public enum EventType {
    PHONE_INFO,;

    public boolean of(String stringValue) {
        if(stringValue==null){
            return false;
        }
        return EventType.valueOf(stringValue) == this;
    }
}
