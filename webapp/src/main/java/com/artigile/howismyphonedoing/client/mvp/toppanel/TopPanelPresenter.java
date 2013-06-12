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

import com.artigile.howismyphonedoing.api.model.UserDeviceModel;
import com.artigile.howismyphonedoing.client.MainEventBus;
import com.artigile.howismyphonedoing.client.Messages;
import com.artigile.howismyphonedoing.client.channel.ChannelStateType;
import com.artigile.howismyphonedoing.client.rpc.AsyncCallbackImpl;
import com.artigile.howismyphonedoing.client.rpc.MessageRpcServiceAsync;
import com.artigile.howismyphonedoing.client.rpc.UserInfoRpcServiceAsync;
import com.artigile.howismyphonedoing.client.service.ApplicationState;
import com.artigile.howismyphonedoing.client.widget.DevicesListWindow;
import com.artigile.howismyphonedoing.shared.entity.StateAndChanelEntity;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * User: ioanbsu
 * Date: 6/11/13
 * Time: 12:11 PM
 */
@Presenter(view = TopPanelView.class)
public class TopPanelPresenter extends BasePresenter<TopPanelView, MainEventBus> {

    @Inject
    private MessageRpcServiceAsync messageRpcServiceAsync;
    @Inject
    private Messages messages;
    @Inject
    private UserInfoRpcServiceAsync userInfoRpcService;
    @Inject
    private ApplicationState applicationState;
    @Inject
    private DevicesListWindow devicesListWindow;
    private List<UserDeviceModel> devicesList;

    public void onInitApp() {
        GWT.log("TopPanelPresenter initiated.");
    }

    public void logout() {
        eventBus.userLogout();
    }

    public void onUserLogout() {
        view.setLoggedInUserData(messages.top_panel_not_logged_in());
    }

    public void onUserLoggedIn(StateAndChanelEntity stateAndChanelEntity) {
        view.setLoggedInUserData(stateAndChanelEntity.getEmail());
        userInfoRpcService.getUsersDevicesList(new AsyncCallbackImpl<List<UserDeviceModel>>() {
            @Override
            public void success(List<UserDeviceModel> result) {
                eventBus.usersDevicesListUdated(result);
            }
        });
    }

    public void onUsersDevicesListUdated(List<UserDeviceModel> result) {
        devicesList = result;
        view.setMyDevicesCount(result.size());
    }

    public void onChannelStateChanged(ChannelStateType channelState) {
        view.updateChannelStateIcon(channelState);
    }

    public void sendTextToPhone(String text) {
        if (devicesList == null || devicesList.isEmpty()) {
            showNoDevicesFoundMessage();
        } else {
            messageRpcServiceAsync.sendSimpleTextMessage(text, new AsyncCallbackImpl<String>() {
            });
        }
    }

    public void removeAllDevices() {
        messageRpcServiceAsync.removeAllEntities(new AsyncCallbackImpl<String>() {
            @Override
            public void success(String result) {
                eventBus.usersDevicesListUdated(new ArrayList<UserDeviceModel>());
                Window.alert("Devices removed");
            }

            @Override
            public void failure(Throwable caught) {
                Window.alert("failed to remove entities");
            }
        });
    }

    public void sendRequestToUpdatePhoneLocation() {
        if (devicesList == null || devicesList.isEmpty()) {
            showNoDevicesFoundMessage();
        } else {
            messageRpcServiceAsync.getPhoneLocation(new AsyncCallbackImpl<String>() {
                @Override
                public void success(String result) {

                }
            });
        }
    }

    private void showNoDevicesFoundMessage() {
        Window.alert("you don't have any devices linked to your account. " +
                "Please register the device first, then try to update devices list.");
    }

    public void showDevicesCountWindow() {
        devicesListWindow.show();
    }
}
