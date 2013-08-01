package com.artigile.howismyphonedoing.client.mvp.settings;

import com.artigile.howismyphonedoing.api.model.IUserDeviceModel;
import com.artigile.howismyphonedoing.api.model.UserDeviceModel;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.Start;
import com.mvp4g.client.event.EventBus;

import java.util.List;

/**
 * Date: 7/23/13
 * Time: 7:16 PM
 *
 * @author ioanbsu
 */

@Events(module = SettingsModule.class, startPresenter = SettingsPresenter.class)
public interface SettingsEventBus extends EventBus {

    @Start
    @Event(handlers =  {SettingsPresenter.class})
    void initApp();

    @Event(forwardToParent = true)
    void centerDeviceLocationOnScreen(IUserDeviceModel iUserDeviceModel);

    @Event(handlers = {SettingsPresenter.class})
    void usersDevicesListReceived(List<UserDeviceModel> result);

    @Event(handlers = {SettingsPresenter.class})
    void deviceDetailsReceived(IUserDeviceModel deviceDetails);

    @Event(handlers = {SettingsPresenter.class})
    void deviceHadBeenLocked(IUserDeviceModel userDevice);

    @Event(handlers = {SettingsPresenter.class})
    void deviceAdminIsNotEnabled(IUserDeviceModel userDevice);

    @Event(handlers = {SettingsPresenter.class})
    void showSettingsWindow();

}
