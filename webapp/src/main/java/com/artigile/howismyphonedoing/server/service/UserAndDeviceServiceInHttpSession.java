package com.artigile.howismyphonedoing.server.service;

import com.artigile.howismyphonedoing.server.entity.UserDevice;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * @author IoaN, 5/28/13 9:30 PM
 */
@Service
public class UserAndDeviceServiceInHttpSession implements UserAndDeviceService {

    private static Set<UserDevice> usersMap = new HashSet<>();

    @Override
    public void register(UserDevice userDevice) {
        usersMap.remove(userDevice);
        usersMap.add(userDevice);
    }

    @Override
    public void unregister(String registeredDeviceId) {
        Set<UserDevice> newUserMap = new HashSet<>();
        for (UserDevice userDevice : usersMap) {
            if (!registeredDeviceId.equals(userDevice.getRegisteredId())) {
                newUserMap.add(userDevice);
            }
        }
        usersMap = newUserMap;
    }

    @Override
    public void updateRegistration(String oldId, String newId) {
        for (UserDevice userDevice : usersMap) {
            if (oldId.equals(userDevice.getRegisteredId())) {
                userDevice.setRegisteredId(newId);
            }
        }
    }

    @Override
    public Set<UserDevice> getDevices(String userEmail) {
        Set<UserDevice> devicesByEmailList = new HashSet<>();
        for (UserDevice userDevice : usersMap) {
            if (userEmail.equals(userDevice.getUserEmail())) {
                devicesByEmailList.add(userDevice);
            }
        }
        return devicesByEmailList;
    }
}
