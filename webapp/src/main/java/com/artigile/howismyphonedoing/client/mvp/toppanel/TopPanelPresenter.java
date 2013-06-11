package com.artigile.howismyphonedoing.client.mvp.toppanel;

import com.artigile.howismyphonedoing.client.MainEventBus;
import com.artigile.howismyphonedoing.client.Messages;
import com.artigile.howismyphonedoing.client.rpc.AsyncCallbackImpl;
import com.artigile.howismyphonedoing.client.rpc.MessageRpcServiceAsync;
import com.artigile.howismyphonedoing.shared.entity.StateAndChanelEntity;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import javax.inject.Inject;

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

    public void onInitApp() {
        GWT.log("TopPanelPresenter initiated.");
    }

    public void logout() {
        eventBus.userLogout();
    }

    public void onUserLogout() {
        view.setLoggedInUserData(messages.not_logged_in());
    }

    public void onUserLoggedIn(StateAndChanelEntity stateAndChanelEntity) {
        view.setLoggedInUserData(stateAndChanelEntity.getEmail());
    }

    public void sendTextToPhone(String text) {
        messageRpcServiceAsync.sendSimpleTextMessage(text, new AsyncCallbackImpl<String>() {
        });
    }

    public void getPhonesInfo() {
        messageRpcServiceAsync.getPhoneInfo(new AsyncCallbackImpl<String>() {
            @Override
            public void success(String result) {
                //todo: fix it.
            }
        });
    }

    public void removeAllDevices() {
        messageRpcServiceAsync.removeAllEntities(new AsyncCallbackImpl<String>() {
            @Override
            public void success(String result) {
                Window.alert("Devices removed");
            }

            @Override
            public void failure(Throwable caught) {
                Window.alert("failed to remove entities");
            }
        });
    }

    public void sendRequestToUpdatePhoneLocation() {
        messageRpcServiceAsync.getPhoneLocation(new AsyncCallbackImpl<String>() {
            @Override
            public void success(String result) {

            }
        });
    }

}
