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

import com.artigile.howismyphonedoing.api.model.MessageType;
import com.artigile.howismyphonedoing.api.shared.WebAppMessageProcessor;
import com.artigile.howismyphonedoing.client.exception.DeviceWasRemovedException;
import com.artigile.howismyphonedoing.client.rpc.MessageRpcService;
import com.artigile.howismyphonedoing.server.service.UserService;
import com.artigile.howismyphonedoing.server.service.WebAppMessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
@Service
public class MessageRpcServiceImpl extends AbstractRpcService implements MessageRpcService {

    @Autowired
    private UserService userService;
    @Autowired
    private WebAppMessageSender messageSender;
    protected final Logger logger = Logger.getLogger(getClass().getName());

    @Override
    public String removeAllUserDevices() {
        userService.removeAllDevices(getUserEmailFromSession());
        return "successfully removed";
    }

    @Override
    public String sendMessageToDevice(MessageType messageType, String deviceId, String serializedObject) throws DeviceWasRemovedException {
        try {
            messageSender.processMessage(deviceId, messageType, serializedObject);
        } catch (Exception e) {
            logger.warning("message failed to be send to device with id = " + deviceId);
        }
        return "success";
    }


}
