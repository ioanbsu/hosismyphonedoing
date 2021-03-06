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

import com.artigile.howismyphonedoing.api.CommonConstants;
import com.artigile.howismyphonedoing.api.model.MessageType;
import com.artigile.howismyphonedoing.shared.WebAppMessageProcessor;
import com.artigile.howismyphonedoing.client.exception.DeviceWasRemovedException;
import com.artigile.howismyphonedoing.server.entity.UserDevice;
import com.artigile.howismyphonedoing.server.gcmserver.*;
import com.artigile.howismyphonedoing.server.service.cloudutil.KeysResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: ioanbsu
 * Date: 5/24/13
 * Time: 9:22 AM
 */
@Service
public class WebAppMessageSender implements WebAppMessageProcessor<String> {
    private static final Executor threadPool = Executors.newFixedThreadPool(5);
    private static final int MULTICAST_SIZE = 1000;
    protected final Logger logger = Logger.getLogger(getClass().getName());
    private Sender sender;
    @Autowired
    private KeysResolver apiKeyResolver;
    @Autowired
    private UserService userService;

    @PostConstruct
    private void init() {
        sender = new Sender(apiKeyResolver.getPhoneApiKey());
    }

    @Override
    public String processMessage(String deviceId, MessageType messageType, String messageStr) throws DeviceWasRemovedException {
        logger.info("Sending message to device with id = " + deviceId + ", message type = " + messageType);
        UserDevice device = userService.findUserDeviceByUuid(deviceId);
        if (device == null) {
            throw new DeviceWasRemovedException();
        }
        String status;
        Message message = new Message.Builder().addData(CommonConstants.SERIALIZED_OBJECT, messageStr)
                .addData(CommonConstants.MESSAGE_EVENT_TYPE, messageType.toString())
                .build();
        // must split in chunks of 1000 devices (GCM limit)
        int total = 1;
        List<String> partialDevices = new ArrayList<String>(total);
        int counter = 0;
        int tasks = 0;
        counter++;
        partialDevices.add(device.getDeviceCloudRegistrationId());
        int partialSize = partialDevices.size();
        if (partialSize == MULTICAST_SIZE || counter == total) {
            sendMessage(partialDevices, message);
            partialDevices.clear();
            tasks++;
        }
        status = "Asynchronously sending " + tasks + " multicast messages to " + total + " devices";
        return status;
    }

    private void sendMessage(List<String> partialDevices, final Message message) throws DeviceWasRemovedException {
        // make a copy
        final List<String> devices = new ArrayList<String>(partialDevices);
        MulticastResult multicastResult;
        try {
            multicastResult = sender.send(message, devices, 5);
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error posting messages", e);
            return;
        }
        List<Result> results = multicastResult.getResults();
        for (Result result : results) {
            logger.info(result.toString());
        }
        // analyze the results
        for (int i = 0; i < devices.size(); i++) {
            String currentRegistrationId = devices.get(i);
            Result result = results.get(i);
            String messageId = result.getMessageId();
            if (messageId != null) {
                logger.fine("Succesfully sent message to device: " + currentRegistrationId +
                        "; messageId = " + messageId);
                String newRegistrationId = result.getCanonicalRegistrationId();
                if (newRegistrationId != null) {
                    // same device has more than one registration id: update it
                    logger.info("canonicalRegId " + newRegistrationId);
                    userService.updateDeviceGcmRegistration(currentRegistrationId, newRegistrationId);
                }
            } else {
                String error = result.getErrorCodeName();
                if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
                    // application has been removed from device - unregister it
                    logger.info("Unregistered device: " + currentRegistrationId);
                    UserDevice userDevice = userService.getDeviceByGcmRegId(currentRegistrationId);
                    if (userDevice != null) {
                        userService.unregisterDevice(userDevice.getUuid());
                        throw new DeviceWasRemovedException();
                    }
                } else {
                    logger.severe("Error sending message to " + currentRegistrationId + ": " + error);
                }
            }
        }
    }


}
