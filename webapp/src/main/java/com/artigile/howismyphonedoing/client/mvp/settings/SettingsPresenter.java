package com.artigile.howismyphonedoing.client.mvp.settings;

import com.artigile.howismyphonedoing.api.model.MessageType;
import com.artigile.howismyphonedoing.api.model.UserDeviceModel;
import com.artigile.howismyphonedoing.client.MainEventBus;
import com.artigile.howismyphonedoing.client.rpc.AsyncCallbackImpl;
import com.artigile.howismyphonedoing.client.rpc.MessageRpcServiceAsync;
import com.google.gwt.core.shared.GWT;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Date: 6/22/13
 * Time: 7:53 PM
 *
 * @author ioanbsu
 */
@Singleton
@Presenter(view = SettingsView.class)
public class SettingsPresenter extends BasePresenter<SettingsView, MainEventBus> {

    @Inject
    private MessageRpcServiceAsync messageRpcServiceAsync;

    public void onInitApp() {
        GWT.log("Settings window initiated.");
    }


    public void show() {
        view.show();
    }

    public void onDeviceSelected(UserDeviceModel userDeviceModel) {

    }

    public void requestRefreshDeficeInfo(UserDeviceModel selectedObject) {
        messageRpcServiceAsync.sendMessageToDevice(MessageType.DEVICE_DETAILS_INFO, selectedObject.getDeviceId(), "", new AsyncCallbackImpl<String>(eventBus) {

        });
    }
}
