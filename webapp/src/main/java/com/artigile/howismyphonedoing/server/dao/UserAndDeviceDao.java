package com.artigile.howismyphonedoing.server.dao;

import com.artigile.howismyphonedoing.server.entity.UserDevice;

import java.util.Set;

/**
 * @author IoaN, 5/28/13 9:27 PM
 */
public interface UserAndDeviceDao {

    void register(UserDevice userDevice);

    void unregister(String registeredDeviceId);

    void updateRegistration(String oldId, String newId);

    Set<UserDevice> getDevices(String userEmail);

    UserDevice getById(String uuid);
}
