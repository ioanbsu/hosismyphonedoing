/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.howismyphonedoing.client.widget;

import com.artigile.howismyphonedoing.api.model.UserDeviceModel;
import com.artigile.howismyphonedoing.client.MainEventBus;
import com.artigile.howismyphonedoing.client.Messages;
import com.artigile.howismyphonedoing.client.rpc.AsyncCallbackImpl;
import com.artigile.howismyphonedoing.client.rpc.UserInfoRpcServiceAsync;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * User: ioanbsu
 * Date: 6/11/13
 * Time: 5:33 PM
 */
@EventHandler
@Singleton
public class DevicesListWindow extends BaseEventHandler<MainEventBus> {
    @UiField
    DialogBox devicesListWindow;
    @UiField
    Button closeWindow;
    @UiField
    FlowPanel devicesList;
    @UiField
    Button refresh;
    @Inject
    private UserInfoRpcServiceAsync userInfoRpcServiceAsync;
    @Inject
    private Messages messages;

    @Inject
    public DevicesListWindow(Binder binder) {
        binder.createAndBindUi(this);
    }

    public void show() {
        devicesListWindow.center();
        refreshDevicesList();
    }

    public void onUsersDevicesListUdated(List<UserDeviceModel> result) {
        devicesList.clear();
        if (result == null || result.isEmpty()) {
            devicesList.add(new Label(messages.view_my_devices_window_no_devices_found_message()));
        } else {
            for (UserDeviceModel userDeviceModel : result) {
                devicesList.add(new Label(userDeviceModel.getDeviceId()));
            }
        }
    }

    public void hide() {
        devicesListWindow.hide();
    }

    @UiHandler("closeWindow")
    void onDeviceCloseButtonCliecked(ClickEvent clickEvent) {
        hide();
    }

    @UiHandler("refresh")
    void onRefreshClicked(ClickEvent clickEvent) {
        refreshDevicesList();
    }

    private void refreshDevicesList() {
        devicesList.add(new Label("Loading devices list... Please Wait"));
        userInfoRpcServiceAsync.getUsersDevicesList(new AsyncCallbackImpl<List<UserDeviceModel>>(eventBus) {
            @Override
            public void success(List<UserDeviceModel> result) {
                eventBus.usersDevicesListUdated(result);
            }
        });
    }

    interface Binder extends UiBinder<DialogBox, DevicesListWindow> {
    }
}
