package com.artigile.howismyphonedoing.server.service;

import com.artigile.howismyphonedoing.server.entity.User;
import com.artigile.howismyphonedoing.server.entity.UserDevice;

/**
 * @author IoaN, 5/28/13 9:27 PM
 */
public interface UserAndDeviceService {

    void createOrUpdateUser(User user);

    void createOrUpdateUserDevice(String userEmail, UserDevice userDevice);

    void removeUserDevice(String userEmail, UserDevice userDevice);

    User getUserByEmail(String userEmail);
}
