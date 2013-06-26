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

import com.artigile.howismyphonedoing.api.MessageParser;
import com.artigile.howismyphonedoing.api.model.DeviceRegistrationModel;
import com.artigile.howismyphonedoing.api.model.MessageType;
import com.artigile.howismyphonedoing.api.model.ResponseFromServer;
import com.artigile.howismyphonedoing.api.model.UserDeviceModel;
import com.artigile.howismyphonedoing.api.shared.WebAppMessageProcessor;
import com.artigile.howismyphonedoing.server.entity.UserDevice;
import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

/**
 * User: ioanbsu
 * Date: 6/6/13
 * Time: 5:55 PM
 */
@Service
public class WebAppMessageReceiver implements WebAppMessageProcessor<String> {
    protected final Logger logger = Logger.getLogger(getClass().getName());
    @Autowired
    private UserService userInfoService;
    @Autowired
    private MessageParser messageParser;

    @Override
    public String processMessage(String uuid, MessageType messageType, String serializedObject) throws Exception {
        try {
            String userEmail = null;
            UserDevice userDevice = null;
            if (messageType == MessageType.REGISTER_DEVICE) {
                logger.info("Registering new device: " + uuid);
                logger.info("Serialized object: " + serializedObject);
                DeviceRegistrationModel registrationModel = messageParser.parse(messageType, serializedObject);
                userDevice = new UserDevice();
                userEmail = registrationModel.getUserEmail();
                userDevice.setUserEmail(userEmail);
                userDevice.setUuid(uuid);
                userDevice.setDeviceCloudRegistrationId(registrationModel.getDeviceCloudRegistrationId());
                userDevice.setHumanReadableName(registrationModel.getDeviceModel().getModel());
                userInfoService.registerUserDevice(userDevice);
            } else if (messageType == MessageType.UNREGISTER_DEVICE) {
                logger.info("Unregistering device: " + uuid);
                userEmail = userInfoService.unregisterDeviceByUuid(uuid);
            } else {
                logger.info("Message Type: " + messageType);
                userDevice = userInfoService.findUserDeviceByUuid(uuid);
                userEmail = userDevice.getUserEmail();
            }
            if (userEmail != null) {
                ChannelService channelService = ChannelServiceFactory.getChannelService();
                ResponseFromServer responseFromServer = new ResponseFromServer();
                responseFromServer.setMessageType(messageType);
                responseFromServer.setSerializedObject(serializedObject);
                if (userDevice != null) {
                    UserDeviceModel userDeviceModel = new UserDeviceModel();
                    userDeviceModel.setDeviceId(userDevice.getUuid());
                    userDeviceModel.setHumanReadableName(userDevice.getHumanReadableName());
                    responseFromServer.setUserDeviceModel(userDeviceModel);
                }
                channelService.sendMessage(new ChannelMessage(userEmail, new Gson().toJson(responseFromServer)));
            }
        } catch (Exception e) {
            logger.warning("unexpected error happened, please investigate!!!!!!!!!!!!!!!!");
            throw e;
        }
        return "message pasring success";
    }

}
