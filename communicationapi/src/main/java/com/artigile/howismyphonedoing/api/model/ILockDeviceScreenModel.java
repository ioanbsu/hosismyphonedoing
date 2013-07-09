package com.artigile.howismyphonedoing.api.model;

import java.io.Serializable;

/**
 * User: ioanbsu
 * Date: 7/8/13
 * Time: 7:09 PM
 */
public interface ILockDeviceScreenModel extends Serializable {
    String getNewPinCode();

    void setNewPinCode(String newPinCode);
}
