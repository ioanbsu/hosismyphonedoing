/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.howismyphonedoing.client.mvp.mapview;

import com.artigile.howismyphonedoing.api.model.IDeviceLocationModel;
import com.artigile.howismyphonedoing.client.MainEventBus;
import com.artigile.howismyphonedoing.client.Messages;
import com.artigile.howismyphonedoing.shared.entity.StateAndChanelEntity;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.maps.gwt.client.*;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: ioanbsu
 * Date: 6/11/13
 * Time: 1:00 PM
 */
@Presenter(view = MapBodyView.class)
public class MapBodyPresenter extends BasePresenter<MapBodyView, MainEventBus> {
    private List<DeviceMarkerModel> deviceMarkerModelList;
    @Inject
    private Messages messages;

    public void onInitApp() {
        GWT.log("MapBodyPresenter initiated.");
    }

    public void onPhoneLocationUpdated(IDeviceLocationModel model) {
        deviceMarkerModelList = prepareMarkers(model);
        view.showMarkers(deviceMarkerModelList);
    }

    private List<DeviceMarkerModel> prepareMarkers(IDeviceLocationModel deviceLocationModel) {
        if (deviceMarkerModelList == null) {
            deviceMarkerModelList = new ArrayList<DeviceMarkerModel>();
        }
        DeviceMarkerModel foundModel = null;
        for (DeviceMarkerModel deviceMarkerModel : deviceMarkerModelList) {
            if (deviceMarkerModel.getDeviceLocationModel().getDeviceId().equals(deviceLocationModel.getDeviceId())) {
                foundModel = deviceMarkerModel;
                break;
            }
        }
        if (foundModel != null) {
            LatLng myLatLng = LatLng.create(deviceLocationModel.getLatitude(), deviceLocationModel.getLongitude());
            foundModel.getCircle().setCenter(myLatLng);
            foundModel.getCircle().setRadius(deviceLocationModel.getAccuracy());
            foundModel.getMarker().setPosition(myLatLng);
        } else {
            foundModel = new DeviceMarkerModel();
            foundModel.setDeviceLocationModel(deviceLocationModel);
            foundModel.setCircle(createCircle(deviceLocationModel));
            foundModel.setMarker(createMarker(deviceLocationModel));
            deviceMarkerModelList.add(foundModel);
        }
        String timeDeviceUpdated = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_MEDIUM).format(new Date());
        foundModel.getMarker().setTitle(messages.map_view_device_name_on_map(deviceLocationModel.getDeviceName(), timeDeviceUpdated));
        return deviceMarkerModelList;
    }

    public void onUserLoggedIn(StateAndChanelEntity stateAndChanelEntity) {
        view.initMap();
    }

    public void onUserLogout() {
    }

    private Marker createMarker(IDeviceLocationModel deviceLocationModel) {
        MarkerOptions newMarkerOpts = MarkerOptions.create();
        LatLng myLatLng = LatLng.create(deviceLocationModel.getLatitude(), deviceLocationModel.getLongitude());
        newMarkerOpts.setPosition(myLatLng);
        newMarkerOpts.setMap(view.getMap());
        return Marker.create(newMarkerOpts);

    }

    private Circle createCircle(IDeviceLocationModel deviceLocationModel) {
        LatLng myLatLng = LatLng.create(deviceLocationModel.getLatitude(), deviceLocationModel.getLongitude());
        CircleOptions populationOptions = CircleOptions.create();
        populationOptions.setStrokeColor("#0000ff");
        populationOptions.setStrokeOpacity(0.8);
        populationOptions.setStrokeWeight(2);
        populationOptions.setFillColor("#0000bb");
        populationOptions.setFillOpacity(0.35);
        populationOptions.setCenter(myLatLng);
        populationOptions.setRadius(deviceLocationModel.getAccuracy());
        populationOptions.setMap(view.getMap());
        return Circle.create(populationOptions);
    }


}
