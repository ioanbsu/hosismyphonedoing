package com.artigile.howismyphonedoing.server.service;

import com.artigile.howismyphonedoing.api.model.UserDeviceModel;
import com.artigile.howismyphonedoing.server.entity.UserDevice;

import java.util.List;

/**
 * Date: 6/25/13
 * Time: 4:17 PM
 *
 * @author ioanbsu
 */

public interface UserService {
    List<UserDeviceModel> getUsersDevicesList(String userEmail);

    void registerUserDevice(UserDevice userDevice);

    String unregisterDeviceByUuid(String uuid);

    UserDevice findUserDeviceByUuid(String uuid);

    void updateDeviceGcmRegistration(String currentRegistrationId, String newRegistrationId);

    UserDevice getDeviceByGcmRegId(String gcmRegistrationId);

    void unregisterDevice(String userDeviceUuid);

    void removeAllDevices(String userEmail);
}
