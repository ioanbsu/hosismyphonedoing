package com.artigile.howismyphonedoing.client.mvp.settings;

import com.artigile.howismyphonedoing.api.model.IUserDeviceModel;
import com.artigile.howismyphonedoing.api.model.MessageType;
import com.artigile.howismyphonedoing.api.model.UserDeviceModel;
import com.artigile.howismyphonedoing.client.MainEventBus;
import com.artigile.howismyphonedoing.client.rpc.AsyncCallbackImpl;
import com.artigile.howismyphonedoing.client.rpc.MessageRpcServiceAsync;
import com.google.gwt.core.shared.GWT;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

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

    public void onUsersDevicesListReceived(List<UserDeviceModel> result){
         view.setDevicesList(result);
    }


    public void onDeviceDetailsReceived(IUserDeviceModel deviceDetails) {
        view.updateDeviceDetails(deviceDetails);
    }

    public void requestRefreshDeviceInfo(IUserDeviceModel selectedObject) {
        messageRpcServiceAsync.sendMessageToDevice(MessageType.DEVICE_DETAILS_INFO, selectedObject.getDeviceId(), "", new AsyncCallbackImpl<String>(eventBus) {

        });
    }
}
