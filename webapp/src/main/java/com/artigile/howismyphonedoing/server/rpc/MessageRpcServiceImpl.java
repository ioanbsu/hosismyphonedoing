package com.artigile.howismyphonedoing.server.rpc;

import com.artigile.howismyphonedoing.api.MessageType;
import com.artigile.howismyphonedoing.api.model.PhoneModel;
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
        messageSender.processMessage(userDevice, MessageType.PHONE_INFO, new Gson().toJson(new PhoneModel()));
        return "message sent";
    }
}