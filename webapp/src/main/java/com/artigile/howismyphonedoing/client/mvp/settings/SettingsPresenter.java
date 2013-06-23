package com.artigile.howismyphonedoing.client.mvp.settings;

import com.artigile.howismyphonedoing.api.model.UserDeviceModel;
import com.artigile.howismyphonedoing.client.MainEventBus;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

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


    public void onInitApp() {
        GWT.log("Settings window initiated.");
    }


    public void show() {
        view.show();
    }

    public void onDeviceSelected(UserDeviceModel userDeviceModel) {

    }
}
