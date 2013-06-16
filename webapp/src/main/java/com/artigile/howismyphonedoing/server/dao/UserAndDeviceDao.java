/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.howismyphonedoing.server.dao;

import com.artigile.howismyphonedoing.server.entity.UserDevice;

import java.util.Set;

/**
 * @author IoaN, 5/28/13 9:27 PM
 */
public interface UserAndDeviceDao {

    void register(UserDevice userDevice);

    String unregister(String registeredDeviceId);

    void updateRegistration(String oldId, String newId);

    Set<UserDevice> getDevices(String userEmail);

    UserDevice getById(String uuid);

    void removeAllUserDevices(String userEmailFromSession);

    UserDevice getDeviceByGcmId(String regId);
}
