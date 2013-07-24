/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.howismyphonedoing.client.mvp.toppanel;

import com.artigile.howismyphonedoing.api.model.IDeviceLocationModel;
import com.artigile.howismyphonedoing.api.model.IDeviceModel;
import com.artigile.howismyphonedoing.api.model.MessageType;
import com.artigile.howismyphonedoing.api.model.UserDeviceModel;
import com.artigile.howismyphonedoing.client.MainEventBus;
import com.artigile.howismyphonedoing.client.Messages;
import com.artigile.howismyphonedoing.client.channel.ChannelStateType;
import com.artigile.howismyphonedoing.client.exception.UserHasNoDevicesException;
import com.artigile.howismyphonedoing.client.mvp.settings.SettingsPresenter;
import com.artigile.howismyphonedoing.client.rpc.AsyncCallbackImpl;
import com.artigile.howismyphonedoing.client.rpc.MessageRpcServiceAsync;
import com.artigile.howismyphonedoing.client.rpc.UserInfoRpcServiceAsync;
import com.artigile.howismyphonedoing.client.service.ApplicationState;
import com.artigile.howismyphonedoing.client.service.HowIsMyPhoneDoingAutoBeansFactory;
import com.artigile.howismyphonedoing.client.widget.MessageWindow;
import com.artigile.howismyphonedoing.client.widget.SendMessageWindow;
import com.artigile.howismyphonedoing.client.widget.YesNoWindow;
import com.artigile.howismyphonedoing.shared.entity.StateAndChanelEntity;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * User: ioanbsu
 * Date: 6/11/13
 * Time: 12:11 PM
 */
@Singleton
@Presenter(view = TopPanelView.class)
public class TopPanelPresenter extends BasePresenter<TopPanelView, MainEventBus> {

    public static final int MAX_LOCATION_RESPONSE_WAIT = 60 * 1000;
    @Inject
    private MessageRpcServiceAsync messageRpcServiceAsync;
    @Inject
    private Messages messages;
    @Inject
    private UserInfoRpcServiceAsync userInfoRpcService;
    @Inject
    private ApplicationState applicationState;
    @Inject
    private SendMessageWindow sendMessageWindow;
    @Inject
    private YesNoWindow yesNoWindow;
    @Inject
    private MessageWindow messageWindow;
    @Inject
    private HowIsMyPhoneDoingAutoBeansFactory howIsMyPhoneDoingAutoBeansFactory;
    private Timer locationDetectTimer;

    private Logger logger = Logger.getLogger(TopPanelPresenter.class.getName());

    public void onInitApp() {
        GWT.log("TopPanelPresenter initiated.");
        view.setMyDevicesCount(0);
    }

    public void logout() {
        eventBus.userLogout();
    }

    public void onUserLogout() {
        view.setLoggedInUserData(messages.top_panel_not_logged_in());
    }

    public void onUserLoggedIn(StateAndChanelEntity stateAndChanelEntity) {
        view.setLoggedInUserData(stateAndChanelEntity.getEmail());
        onUpdateDevicesList();
        sendRequestToUpdatePhoneLocation();
    }

    public void onUpdateDevicesList() {
        userInfoRpcService.getUsersDevicesList(new AsyncCallbackImpl<List<UserDeviceModel>>() {
            @Override
            public void success(List<UserDeviceModel> result) {
                eventBus.usersDevicesListReceived(result);
            }

            @Override
            public void failure(Throwable caught) {
                if (caught instanceof UserHasNoDevicesException) {
                    eventBus.usersDevicesListReceived(new ArrayList<UserDeviceModel>());
                }
            }
        });
    }

    public void onUsersDevicesListReceived(List<UserDeviceModel> result) {
        view.setMyDevicesCount(result.size());
    }

    public void onChannelStateChanged(ChannelStateType channelState) {
        view.updateChannelStateIcon(channelState);
    }

    public void onDeviceLocationUpdated(IDeviceLocationModel model) {
        cancelTimer();
        view.hideDevicesLoading();
    }

    public void sendTextToPhone() {
        sendMessageWindow.show();
    }

    public void removeAllDevices() {
        yesNoWindow.show(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                messageRpcServiceAsync.removeAllUserDevices(new AsyncCallbackImpl<String>() {
                    @Override
                    public void success(String result) {
                        eventBus.usersDevicesListReceived(new ArrayList<UserDeviceModel>());
                        Window.alert("Devices removed");
                    }

                    @Override
                    public void failure(Throwable caught) {
                        Window.alert("failed to remove entities");
                    }
                });
            }
        }, null, messages.top_panel_remove_all_devices_prompt());

    }

    public void sendRequestToUpdatePhoneLocation() {
        cancelTimer();
        locationDetectTimer = new Timer() {
            @Override
            public void run() {
                view.hideDevicesLoading();
                view.displayDevicesLocationTooltip(messages.top_panel_devices_location_detection_delay());
            }
        };
        locationDetectTimer.schedule(MAX_LOCATION_RESPONSE_WAIT);
        userInfoRpcService.getUsersDevicesList(new AsyncCallbackImpl<List<UserDeviceModel>>() {
            @Override
            public void success(List<UserDeviceModel> result) {
                eventBus.devicesLocationUpdateRequestSent();
                sendRequestForDeviceLocations(result);
                view.showDevicesLoading();
            }

            @Override
            public void failure(Throwable caught) {
                if (caught instanceof UserHasNoDevicesException) {
                    cancelTimer();
                }
            }
        });
    }

    private void sendRequestForDeviceLocations(List<UserDeviceModel> result) {
        AutoBean<IDeviceModel> iDeviceModelAutoBean = howIsMyPhoneDoingAutoBeansFactory.create(IDeviceModel.class);
        String serializedMessage = AutoBeanCodex.encode(iDeviceModelAutoBean).getPayload();
        for (UserDeviceModel userDeviceModel : result) {
            messageRpcServiceAsync.sendMessageToDevice(MessageType.GET_DEVICE_LOCATION, userDeviceModel.getDeviceId(), serializedMessage, new AsyncCallbackImpl<String>() {
            });
        }
    }

    private void cancelTimer() {
        if (locationDetectTimer != null) {
            locationDetectTimer.cancel();
        }
    }

    public void showDeviceSettings() {
        eventBus.showSettingsWindow();
    }
}
