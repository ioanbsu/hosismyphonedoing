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

import com.artigile.howismyphonedoing.client.Messages;
import com.artigile.howismyphonedoing.client.channel.ChannelStateType;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.mvp4g.client.view.ReverseViewInterface;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * User: ioanbsu
 * Date: 6/11/13
 * Time: 12:12 PM
 */
@Singleton
public class TopPanelView extends Composite implements ReverseViewInterface<TopPanelPresenter> {

    @UiField
    TopPanelButton logoutButton;
    @UiField
    TopPanelButton sendTextToPhone;
    @UiField
    TopPanelButton removeAllDevices;
    @UiField
    TopPanelButton devicesLocationButton;
    @UiField
    Label loggedInAs;
    @UiField
    TopPanelButton myDevicesCount;
    @UiField
    FlowPanel blockWhileConnecting;
    @UiField
    Image reloadPage;
    @UiField
    TopPanelButton devicesSettings;
    private TopPanelPresenter presenter;
    @Inject
    private Messages messages;

    @Inject
    public TopPanelView(Binder binder) {
        initWidget(binder.createAndBindUi(this));
        blockWhileConnecting.setVisible(false);
    }

    @Override
    public TopPanelPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void setPresenter(TopPanelPresenter presenter) {
        this.presenter = presenter;
    }

    @UiHandler("logoutButton")
    void logOutButtonClickHandler(ClickEvent clickEvent) {
        presenter.logout();
    }

    @UiHandler("sendTextToPhone")
    void sendTextToPhoneButtonClickHandler(ClickEvent clickEvent) {
        presenter.sendTextToPhone();
    }

    @UiHandler("devicesLocationButton")
    void getPhoneLocationHandler(ClickEvent event) {
        if (!devicesLocationButton.isLoadingShowing()) {
            presenter.sendRequestToUpdatePhoneLocation();
        }
    }

    @UiHandler("removeAllDevices")
    void getRemoveAllDevicesHandler(ClickEvent event) {
        presenter.removeAllDevices();
    }

    @UiHandler("myDevicesCount")
    void onMyDevicesCountClick(ClickEvent event) {
        presenter.showDevicesCountWindow();
    }

    @UiHandler("reloadPage")
    void onReloadPageClick(ClickEvent event) {
        Window.Location.reload();
    }

    @UiHandler("devicesSettings")
    void onDeviceSettingsClick(ClickEvent event){
        presenter.showDeviceSettings();
    }

    public void setLoggedInUserData(String email) {
        loggedInAs.setText(messages.top_panel_logged_in_as(email));
    }

    public void setMyDevicesCount(int size) {
        myDevicesCount.setText(messages.top_panel_view_my_devices_link(size + ""));
    }

    public void updateChannelStateIcon(ChannelStateType channelState) {
        blockWhileConnecting.setVisible(channelState == ChannelStateType.CHANNEL_CONNECTING);
    }

    public void hideDevicesLoading() {
        devicesLocationButton.hideLoading();
    }

    public void showDevicesLoading() {
        devicesLocationButton.showLoading(messages.top_panel_searching_for_your_devices_loading_text());
    }

    public void displayDevicesLocationTooltip(String message) {
        devicesLocationButton.showTooltip(message);
    }


    public static interface Binder extends UiBinder<FlowPanel, TopPanelView> {
    }
}
