package com.artigile.howismyphonedoing.client.service;

import com.artigile.howismyphonedoing.api.model.IUserDeviceModel;
import com.artigile.howismyphonedoing.api.model.MessageType;
import com.artigile.howismyphonedoing.api.model.UserDeviceModel;
import com.artigile.howismyphonedoing.client.MainEventBus;
import com.artigile.howismyphonedoing.client.Messages;
import com.artigile.howismyphonedoing.client.widget.MessageWindow;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * User: ioanbsu
 * Date: 6/13/13
 * Time: 1:15 PM
 */
@EventHandler
@Singleton
public class MessageNotSupportedProcessor extends BaseEventHandler<MainEventBus> {

    @Inject
    private MessageWindow messageWindow;
    @Inject
    private Messages messages;

    public void onMessageNotSupported(IUserDeviceModel userDeviceModel, MessageType messageType) {
        messageWindow.show(messages.global_message_type_does_not_supported(userDeviceModel.getDeviceId(), convertMessageTypeToString(messageType)));
    }

    private String convertMessageTypeToString(MessageType messageType) {
        return messageType.toString();
    }
}
