/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.howismyphonedoing.client.service;

import com.artigile.howismyphonedoing.api.model.IDeviceLocationModel;
import com.artigile.howismyphonedoing.api.model.IResponseFromServer;
import com.artigile.howismyphonedoing.api.model.MessageType;
import com.artigile.howismyphonedoing.client.MainEventBus;
import com.artigile.howismyphonedoing.client.Messages;
import com.artigile.howismyphonedoing.client.channel.ChannelStateType;
import com.artigile.howismyphonedoing.client.widget.MessageWindow;
import com.artigile.howismyphonedoing.shared.entity.StateAndChanelEntity;
import com.google.gwt.appengine.channel.client.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * User: ioanbsu
 * Date: 6/11/13
 * Time: 2:39 PM
 */
@Singleton
@EventHandler
public class GaeChannelService extends BaseEventHandler<MainEventBus> {

    private static final int MAX_CHANNEL_OPEN_ATTEMTPS = 5;
    private static final int DELAY_BETWEEN_ATTEMPTS = 60 * 1000;
    private Socket socket;
    @Inject
    private ChannelFactory channelFactory;
    @Inject
    private HowIsMyPhoneDoingAutoBeansFactory howIsMyPhoneDoingFactory;
    @Inject
    private MessageWindow messageWindow;
    @Inject
    private Messages messages;
    private Channel channel;
    private int channelOpenAttempt;
    private Timer timer;
    private boolean userLoggedIn;

    public void onUserLogout() {
        userLoggedIn = false;
        if (socket != null) {
            socket.close();
        }
    }

    public void onUserLoggedIn(StateAndChanelEntity stateAndChanelEntity) {
        userLoggedIn = true;
    }

    public void initGaeChannel(String token) {
        channel = channelFactory.createChannel(token);
        openChannel();
    }

    private void openChannel() {
        if (channelOpenAttempt > MAX_CHANNEL_OPEN_ATTEMTPS) {
            stopTryingToReconnect();
            eventBus.channelStateChanged(ChannelStateType.CHANNEL_NOT_ABLE_TO_RECONNECT);
            messageWindow.show(messages.server_channel_can_not_be_restored(), new MessageWindow.AfterCloseButtonClickHandler() {
                @Override
                public void afterCloseButtonClicked() {
                    Window.Location.reload();
                }
            });
        } else {
            channelOpenAttempt++;
            socket = channel.open(new SocketListener() {
                @Override
                public void onOpen() {
                    eventBus.channelStateChanged(ChannelStateType.CHANNEL_OPENED);
                    stopTryingToReconnect();
                }

                @Override
                public void onMessage(String encodedData) {
                    AutoBean<IResponseFromServer> responseFromServerAutoBean = AutoBeanCodex.decode(howIsMyPhoneDoingFactory, IResponseFromServer.class, encodedData);
                    IResponseFromServer responseFromServer = responseFromServerAutoBean.as();
                    if (responseFromServer.getMessageType() == MessageType.GET_DEVICE_LOCATION) {
                        AutoBean<IDeviceLocationModel> phoneLocationModelAutoBean = AutoBeanCodex.decode(howIsMyPhoneDoingFactory, IDeviceLocationModel.class, responseFromServer.getSerializedObject());
                        eventBus.phoneLocationUpdated(phoneLocationModelAutoBean.as());
                    } else if (responseFromServer.getMessageType() == MessageType.DEVICE_INFO) {
                        gotNotification(responseFromServer);
                    } else if (responseFromServer.getMessageType() == MessageType.NOTIFY_PHONE) {
                        gotNotification(responseFromServer);
                    } else if (responseFromServer.getMessageType() == MessageType.REGISTER_DEVICE) {
                        eventBus.updateDevicesList();
                        gotNotification(responseFromServer);
                    } else if (responseFromServer.getMessageType() == MessageType.UNREGISTER_DEVICE) {
                        eventBus.updateDevicesList();
                        gotNotification(responseFromServer);
                    }

                }

                @Override
                public void onError(ChannelError error) {
                    GWT.log("Channel error: " + error.getCode() + " : " + error.getDescription());
                }

                @Override
                public void onClose() {
                    eventBus.channelStateChanged(ChannelStateType.CHANNEL_CONNECTING);
                    reOpenChannel();
                }
            });
        }
    }

    private void gotNotification(IResponseFromServer responseFromServer) {
        Window.alert(responseFromServer.getMessageType() + responseFromServer.getSerializedObject());
    }

    private void stopTryingToReconnect() {
        if (timer != null) {
            timer.cancel();
        }
        channelOpenAttempt = 0;
    }

    private void reOpenChannel() {
        if (userLoggedIn && channelOpenAttempt == 0) {
            timer = new Timer() {
                @Override
                public void run() {
                    openChannel();
                }
            };
            timer.scheduleRepeating(DELAY_BETWEEN_ATTEMPTS);
        }
    }


}
