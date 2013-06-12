package com.artigile.howismyphonedoing.client;

import com.artigile.howismyphonedoing.api.model.IDeviceLocationModel;
import com.artigile.howismyphonedoing.api.model.UserDeviceModel;
import com.artigile.howismyphonedoing.client.channel.ChannelStateType;
import com.artigile.howismyphonedoing.client.mvp.mainpage.MainPagePresenter;
import com.artigile.howismyphonedoing.client.mvp.mapview.MapBodyPresenter;
import com.artigile.howismyphonedoing.client.mvp.toppanel.TopPanelPresenter;
import com.artigile.howismyphonedoing.client.service.GaeChannelService;
import com.artigile.howismyphonedoing.client.widget.DevicesListWindow;
import com.artigile.howismyphonedoing.shared.entity.StateAndChanelEntity;
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

    @Event(handlers = {MainPagePresenter.class, TopPanelPresenter.class, MapBodyPresenter.class})
    void userLoggedIn(StateAndChanelEntity stateAndChanelEntity);

    @Event(handlers = MapBodyPresenter.class)
    void phoneLocationUpdated(IDeviceLocationModel as);

    @Event(handlers = {TopPanelPresenter.class, DevicesListWindow.class})
    void usersDevicesListUdated(List<UserDeviceModel> result);

    @Event(handlers = TopPanelPresenter.class)
    void channelStateChanged(ChannelStateType channelState);

}
