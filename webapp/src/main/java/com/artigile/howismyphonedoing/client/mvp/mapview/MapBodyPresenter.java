package com.artigile.howismyphonedoing.client.mvp.mapview;

import com.artigile.howismyphonedoing.api.model.IDeviceLocationModel;
import com.artigile.howismyphonedoing.client.MainEventBus;
import com.artigile.howismyphonedoing.shared.entity.StateAndChanelEntity;
import com.google.gwt.core.shared.GWT;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

/**
 * User: ioanbsu
 * Date: 6/11/13
 * Time: 1:00 PM
 */
@Presenter(view = MapBodyView.class)
public class MapBodyPresenter extends BasePresenter<MapBodyView, MainEventBus> {

    public void onInitApp() {
        GWT.log("MapBodyPresenter initiated.");
    }

    public void onPhoneLocationUpdated(IDeviceLocationModel as) {
        view.showMarker(as);
    }

    public void onUserLoggedIn(StateAndChanelEntity stateAndChanelEntity) {
        view.initMap();
    }

    public void onUserLogout() {
    }


}