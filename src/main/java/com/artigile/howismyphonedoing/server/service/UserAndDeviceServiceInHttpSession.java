package com.artigile.howismyphonedoing.server.service;

import com.artigile.howismyphonedoing.server.entity.User;
import com.artigile.howismyphonedoing.server.entity.UserDevice;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * @author IoaN, 5/28/13 9:30 PM
 */
@Service
public class UserAndDeviceServiceInHttpSession implements UserAndDeviceService {

    private static Map<String, User> usersMap = new HashMap<>();

    @Override
    public void createOrUpdateUser(User user) {
        if (user.getEmail() != null) {
            usersMap.put(user.getEmail(), user);
        }
    }

    @Override
    public void createOrUpdateUserDevice(String userEmail, UserDevice userDevice) {
        User user = usersMap.get(userEmail);
        if (user == null) {
            user = new User();
        }
        if (user.getUserDevices() == null) {
            user.setUserDevices(new HashSet<UserDevice>());
        }
        user.getUserDevices().add(userDevice);
        createOrUpdateUser(user);
    }

    @Override
    public void removeUserDevice(String userEmail, UserDevice userDevice) {
        User user = usersMap.get(userEmail);
        if (user != null) {
            if (user.getUserDevices() == null) {
                return;
            }
            user.getUserDevices().remove(userDevice);
        }
        createOrUpdateUser(user);
    }

    @Override
    public User getUserByEmail(String userEmail) {
        return usersMap.get(userEmail);
    }
}
