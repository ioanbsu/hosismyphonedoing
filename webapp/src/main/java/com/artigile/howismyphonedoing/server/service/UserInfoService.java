/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.howismyphonedoing.server.service;

import com.artigile.howismyphonedoing.api.model.UserDeviceModel;
import com.artigile.howismyphonedoing.api.model.battery.BatteryHealthType;
import com.artigile.howismyphonedoing.api.model.battery.BatteryPluggedType;
import com.artigile.howismyphonedoing.api.model.battery.BatteryStatusType;
import com.artigile.howismyphonedoing.server.dao.UserAndDeviceDao;
import com.artigile.howismyphonedoing.server.entity.UserDevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * User: ioanbsu
 * Date: 6/11/13
 * Time: 4:38 PM
 */
@Service
public class UserInfoService {

    @Autowired
    private UserAndDeviceDao userAndDeviceDao;

    public List<UserDeviceModel> getUsersDevicesList(String userEmail) {
        List<UserDeviceModel> userDeviceModelList = new ArrayList<UserDeviceModel>();
        for (UserDevice userDevice : userAndDeviceDao.getDevices(userEmail)) {
            UserDeviceModel userDeviceModel = new UserDeviceModel();
            userDeviceModel.setDeviceId(userDevice.getUuid());
            userDeviceModel.setHumanReadableName(userDevice.getHumanReadableName());
            userDeviceModelList.add(userDeviceModel);
        }

//        fakeDevices(userDeviceModelList);
        return userDeviceModelList;
    }

    private void fakeDevices(List<UserDeviceModel> userDeviceModelList) {
        UserDeviceModel userDeviceModel1=new UserDeviceModel();
        userDeviceModel1.setBatteryHealthType(BatteryHealthType.BATTERY_HEALTH_UNKNOWN);
        userDeviceModel1.setBatteryLevel((float)Math.random()*100);
        userDeviceModel1.setDeviceId("1");
        userDeviceModel1.setBatteryStatusType(BatteryStatusType.BATTERY_STATUS_NOT_CHARGING);
        userDeviceModel1.setBatteryPluggedType(BatteryPluggedType.BATTERY_PLUGGED_WIRELESS);
        userDeviceModel1.setHumanReadableName("MyDevice 1");

        UserDeviceModel userDeviceModel2=new UserDeviceModel();
        userDeviceModel2.setBatteryHealthType(BatteryHealthType.BATTERY_HEALTH_UNSPECIFIED_FAILURE);
        userDeviceModel2.setBatteryLevel((float)Math.random()*100);
        userDeviceModel2.setBatteryStatusType(BatteryStatusType.BATTERY_STATUS_FULL);
        userDeviceModel2.setDeviceId("2");
        userDeviceModel2.setBatteryPluggedType(BatteryPluggedType.BATTERY_PLUGGED_USB);
        userDeviceModel2.setHumanReadableName("MyDevice 2");
        userDeviceModelList.add(userDeviceModel1);
        userDeviceModelList.add(userDeviceModel2);
    }
}
