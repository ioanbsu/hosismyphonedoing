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
import com.artigile.howismyphonedoing.api.WebAppMessageProcessor;
import com.artigile.howismyphonedoing.server.dao.UserAndDeviceDao;
import com.artigile.howismyphonedoing.server.entity.UserDevice;
import com.artigile.howismyphonedoing.server.gcmserver.*;
import com.artigile.howismyphonedoing.server.service.cloudutil.KeysResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
public class WebAppMessageSender implements WebAppMessageProcessor<Set<UserDevice>> {
    private static final Executor threadPool = Executors.newFixedThreadPool(5);
    private static final int MULTICAST_SIZE = 1000;
    protected final Logger logger = Logger.getLogger(getClass().getName());
    private Sender sender;
    @Autowired
    private KeysResolver apiKeyResolver;
    @Autowired
    private UserAndDeviceDao userAndDeviceDao;

    @PostConstruct
    private void init() {
        sender = new Sender(apiKeyResolver.getPhoneApiKey());
    }

    @Override
    public String processMessage(Set<UserDevice> devices, MessageType messageType, String messageStr) {
        String status;
        if (devices.isEmpty()) {
            status = "Message ignored as there is no device registered!";
        } else {
            Message message = new Message.Builder().addData(CommonConstants.SERIALIZED_OBJECT, messageStr)
                    .addData(CommonConstants.MESSAGE_EVENT_TYPE, messageType.toString())
                    .build();
            // must split in chunks of 1000 devices (GCM limit)
            int total = devices.size();
            List<String> partialDevices = new ArrayList<String>(total);
            int counter = 0;
            int tasks = 0;
            for (UserDevice device : devices) {
                counter++;
                partialDevices.add(device.getDeviceCloudRegistrationId());
                int partialSize = partialDevices.size();
                if (partialSize == MULTICAST_SIZE || counter == total) {
                    asyncSend(partialDevices, message);
                    partialDevices.clear();
                    tasks++;
                }
            }
            status = "Asynchronously sending " + tasks + " multicast messages to " + total + " devices";
        }
        return status;
    }

    private void asyncSend(List<String> partialDevices, final Message message) {
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
        // analyze the results
        for (int i = 0; i < devices.size(); i++) {
            String regId = devices.get(i);
            Result result = results.get(i);
            String messageId = result.getMessageId();
            if (messageId != null) {
                logger.fine("Succesfully sent message to device: " + regId +
                        "; messageId = " + messageId);
                String canonicalRegId = result.getCanonicalRegistrationId();
                if (canonicalRegId != null) {
                    // same device has more than on registration id: update it
                    logger.info("canonicalRegId " + canonicalRegId);
                    userAndDeviceDao.updateRegistration(regId, canonicalRegId);
                }
            } else {
                String error = result.getErrorCodeName();
                if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
                    // application has been removed from device - unregister it
                    logger.info("Unregistered device: " + regId);
                    userAndDeviceDao.unregister(regId);
                } else {
                    logger.severe("Error sending message to " + regId + ": " + error);
                }
            }
        }
    }


}
