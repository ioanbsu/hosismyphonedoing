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
import com.artigile.howismyphonedoing.client.MainEventBus;
import com.artigile.howismyphonedoing.client.channel.ChannelStateType;
import com.google.gwt.appengine.channel.client.*;
import com.google.gwt.core.client.GWT;
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

    private Socket socket;
    @Inject
    private ChannelFactory channelFactory;
    @Inject
    private HowIsMyPhoneDoingFactory howIsMyPhoneDoingFactory;

    public void onUserLogout() {
        if (socket != null) {
            socket.close();
        }
    }

    public void initGaeChannel(String token) {
        Channel channel = channelFactory.createChannel(token);
        socket = channel.open(new SocketListener() {
            @Override
            public void onOpen() {
                eventBus.channelStateChanged(ChannelStateType.CHANNEL_OPENED);
            }

            @Override
            public void onMessage(String encodedData) {
                //view.setPhoneInfo("MESSAGE FROM CHANNEL!!!" + encodedData);
                AutoBean<IDeviceLocationModel> bean = AutoBeanCodex.decode(howIsMyPhoneDoingFactory, IDeviceLocationModel.class, encodedData);
                eventBus.phoneLocationUpdated(bean.as());
            }

            @Override
            public void onError(ChannelError error) {
                GWT.log("Channel error: " + error.getCode() + " : " + error.getDescription());
            }

            @Override
            public void onClose() {
                eventBus.channelStateChanged(ChannelStateType.CHANNEL_CLOSED);
            }
        });
    }
}
