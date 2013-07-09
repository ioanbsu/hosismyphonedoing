package com.artigile.howismyphonedoing.api.model;

import java.io.Serializable;

/**
 * User: ioanbsu
 * Date: 7/8/13
 * Time: 7:00 PM
 */
public class LockDeviceScreenModel implements ILockDeviceScreenModel {

    private String newPinCode;

    @Override
    public String getNewPinCode() {
        return newPinCode;
    }

    @Override
    public void setNewPinCode(String newPinCode) {
        this.newPinCode = newPinCode;
    }
}
