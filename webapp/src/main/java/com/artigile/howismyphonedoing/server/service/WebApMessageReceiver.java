package com.artigile.howismyphonedoing.server.service;

import com.artigile.howismyphonedoing.api.MessageParser;
import com.artigile.howismyphonedoing.api.WebAppMessageProcessor;
import com.artigile.howismyphonedoing.api.model.DeviceRegistrationModel;
import com.artigile.howismyphonedoing.api.model.MessageType;
import com.artigile.howismyphonedoing.server.dao.UserAndDeviceDao;
import com.artigile.howismyphonedoing.server.entity.UserDevice;
import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

/**
 * User: ioanbsu
 * Date: 6/6/13
 * Time: 5:55 PM
 */
@Service
public class WebApMessageReceiver implements WebAppMessageProcessor<String> {
    protected final Logger logger = Logger.getLogger(getClass().getName());
    @Autowired
    private UserAndDeviceDao userAndDeviceDao;
    @Autowired
    private MessageParser messageParser;

    @Override
    public String processMessage(String uuid, MessageType messageType, String serializedObject) {
        try {
            if (messageType == MessageType.REGISTER_DEVICE) {
                logger.info("Registering new device: " + uuid);
                DeviceRegistrationModel registrationModel = (DeviceRegistrationModel) messageParser.parse(messageType, serializedObject);
                UserDevice userDevice = new UserDevice();
                userDevice.setUserEmail(registrationModel.getUserEmail());
                userDevice.setUuid(uuid);
                userDevice.setDeviceCloudRegistrationId(registrationModel.getDeviceCloudRegistrationId());
                userAndDeviceDao.register(userDevice);
            }
            if (messageType == MessageType.UNREGISTER_DEVICE) {
                logger.info("Unregistering device: " + uuid);
                userAndDeviceDao.unregister(uuid);
            }
            if (messageType == MessageType.DEVICE_INFO) {
                logger.info("Parsing info about phone.");
                UserDevice userDevice = userAndDeviceDao.getById(uuid);
                ChannelService channelService = ChannelServiceFactory.getChannelService();
                channelService.sendMessage(new ChannelMessage(userDevice.getUserEmail(), serializedObject));
            }
            if (messageType == MessageType.GET_DEVICE_LOCATION) {
                logger.info("Parsing device location info.");
                UserDevice userDevice = userAndDeviceDao.getById(uuid);
                ChannelService channelService = ChannelServiceFactory.getChannelService();
                channelService.sendMessage(new ChannelMessage(userDevice.getUserEmail(), serializedObject));
            }

        } catch (Exception e) {
            logger.warning("unexpected error happened, please investigate!!!!!!!!!!!!!!!!");
            e.printStackTrace();
            return "message was not parsed:(";
        }
        return "message pasring success";
    }


}
