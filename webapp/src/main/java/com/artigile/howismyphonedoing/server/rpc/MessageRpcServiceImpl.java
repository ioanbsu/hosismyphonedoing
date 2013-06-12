/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.howismyphonedoing.server.rpc;

import com.artigile.howismyphonedoing.api.model.DeviceModel;
import com.artigile.howismyphonedoing.api.model.MessageType;
import com.artigile.howismyphonedoing.client.rpc.MessageRpcService;
import com.artigile.howismyphonedoing.server.dao.UserAndDeviceDao;
import com.artigile.howismyphonedoing.server.entity.UserDevice;
import com.artigile.howismyphonedoing.server.service.WebAppMessageSender;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
@Service
public class MessageRpcServiceImpl extends AbstractRpcService implements MessageRpcService {

    @Autowired
    private UserAndDeviceDao userAndDeviceDao;
    @Autowired
    private WebAppMessageSender messageSender;

    public String sendSimpleTextMessage(String messageContent) throws IllegalArgumentException {

        Set<UserDevice> userDevice = userAndDeviceDao.getDevices(getUserEmailFromSession());
        if (userDevice.isEmpty()) {
            return "message was not sent because there is no devices belong to user in session.";
        }
        messageSender.processMessage(userDevice, MessageType.NOTIFY_PHONE, messageContent);
        return "Message has been successfully sent";
    }

    @Override
    public String getPhoneInfo() {
        Set<UserDevice> userDevice = userAndDeviceDao.getDevices(getUserEmailFromSession());
        messageSender.processMessage(userDevice, MessageType.DEVICE_INFO, new Gson().toJson(new DeviceModel()));
        return "message sent for getting phone info";
    }

    @Override
    public String getPhoneLocation() {
        Set<UserDevice> userDevice = userAndDeviceDao.getDevices(getUserEmailFromSession());
        messageSender.processMessage(userDevice, MessageType.GET_DEVICE_LOCATION, new Gson().toJson(new DeviceModel()));
        return "message sent to get phone location";
    }

    @Override
    public String removeAllEntities() {
        userAndDeviceDao.removeAllEntities();
        return "sucessfully removed";
    }

}
