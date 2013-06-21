/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.howismyphonedoing.client;

import com.artigile.howismyphonedoing.api.model.*;
import com.artigile.howismyphonedoing.client.channel.ChannelStateType;
import com.artigile.howismyphonedoing.client.mvp.mainpage.MainPagePresenter;
import com.artigile.howismyphonedoing.client.mvp.mapview.MapBodyPresenter;
import com.artigile.howismyphonedoing.client.mvp.toppanel.TopPanelPresenter;
import com.artigile.howismyphonedoing.client.service.GaeChannelService;
import com.artigile.howismyphonedoing.client.service.MessageNotSupportedProcessor;
import com.artigile.howismyphonedoing.client.widget.DevicesListWindow;
import com.artigile.howismyphonedoing.client.widget.SendMessageWindow;
import com.artigile.howismyphonedoing.client.widget.SigninWithGooglePlusWindow;
import com.artigile.howismyphonedoing.shared.entity.StateAndChanelEntity;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.Start;
import com.mvp4g.client.event.EventBus;

import java.util.List;

/**
 * @author IoaN, 5/26/13 9:07 AM
 */
@Events(startPresenter = MainPagePresenter.class)
public interface MainEventBus extends EventBus {


    @Start
    @Event(handlers = {MainPagePresenter.class, TopPanelPresenter.class, MapBodyPresenter.class})
    void initApp();

    @Event(handlers = {MainPagePresenter.class, TopPanelPresenter.class, MapBodyPresenter.class, GaeChannelService.class})
    void userLogout();

    @Event(handlers = {MainPagePresenter.class, TopPanelPresenter.class, MapBodyPresenter.class,
            SigninWithGooglePlusWindow.class,GaeChannelService.class})
    void userLoggedIn(StateAndChanelEntity stateAndChanelEntity);

    @Event(handlers = {MapBodyPresenter.class,TopPanelPresenter.class})
    void deviceLocationUpdated(IDeviceLocationModel as);

    @Event(handlers = {TopPanelPresenter.class, DevicesListWindow.class, SendMessageWindow.class})
    void usersDevicesListReceived(List<UserDeviceModel> result);

    @Event(handlers = {TopPanelPresenter.class})
    void updateDevicesList();

    @Event(handlers = TopPanelPresenter.class)
    void channelStateChanged(ChannelStateType channelState);

    @Event(handlers = SigninWithGooglePlusWindow.class)
    void showLoginWindow(String cause);

    @Event(handlers = MessageNotSupportedProcessor.class)
    void messageNotSupported(IUserDeviceModel userDeviceModel, MessageType messageType);

    @Event(handlers = SendMessageWindow.class)
    void messageDelivered(IMessageToDeviceModel messageDeliveredModel);
}
