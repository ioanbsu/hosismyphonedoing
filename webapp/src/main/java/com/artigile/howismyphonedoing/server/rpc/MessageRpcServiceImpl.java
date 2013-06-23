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

import com.artigile.howismyphonedoing.api.MessageParser;
import com.artigile.howismyphonedoing.api.model.DeviceModel;
import com.artigile.howismyphonedoing.api.model.MessageToDeviceModel;
import com.artigile.howismyphonedoing.api.model.MessageType;
import com.artigile.howismyphonedoing.client.exception.DeviceWasRemovedException;
import com.artigile.howismyphonedoing.client.exception.UserHasNoDevicesException;
import com.artigile.howismyphonedoing.client.rpc.MessageRpcService;
import com.artigile.howismyphonedoing.server.dao.UserAndDeviceDao;
import com.artigile.howismyphonedoing.server.entity.UserDevice;
import com.artigile.howismyphonedoing.server.service.WebAppMessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

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
    @Autowired
    private MessageParser messageParser;
    protected final Logger logger = Logger.getLogger(getClass().getName());

    @Override
    public String getDevicesLodations() throws DeviceWasRemovedException, UserHasNoDevicesException {
        Set<UserDevice> userDevice = userAndDeviceDao.getDevices(getUserEmailFromSession());
        if (userDevice != null && !userDevice.isEmpty()) {
            messageSender.processMessage(userDevice, MessageType.GET_DEVICE_LOCATION, messageParser.serialize(new DeviceModel()));
        } else {
            throw new UserHasNoDevicesException();
        }
        return "message sent to get phone location";
    }

    @Override
    public String removeAllUserDevices() {
        userAndDeviceDao.removeAllUserDevices(getUserEmailFromSession());
        return "successfully removed";
    }

    @Override
    public String sendMessageToDevice(MessageType messageType, String deviceId, String serializedObject) throws DeviceWasRemovedException {
        logger.info("Sending messages to device with id = " + deviceId + ", message type = " + messageType);
        UserDevice userDevice = userAndDeviceDao.getById(deviceId);
        if (userDevice == null) {
            throw new DeviceWasRemovedException();
        }
        HashSet<UserDevice> userDevices = new HashSet<>();
        userDevices.add(userDevice);
        messageSender.processMessage(userDevices, messageType, serializedObject);
        return "success";
    }


}
