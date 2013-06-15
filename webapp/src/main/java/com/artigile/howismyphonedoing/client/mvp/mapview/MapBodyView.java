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
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.maps.gwt.client.*;
import com.mvp4g.client.view.ReverseViewInterface;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * User: ioanbsu
 * Date: 6/11/13
 * Time: 12:57 PM
 */
@Singleton
public class MapBodyView extends Composite implements ReverseViewInterface<MapBodyPresenter> {

    @UiField
    SimplePanel mapContainer;
    private GoogleMap map;
    private MapBodyPresenter presenter;
    private Marker marker;
    private Circle circle;

    @Inject
    public MapBodyView(Binder binder) {
        initWidget(binder.createAndBindUi(this));
    }

    public void initMap() {
        MapOptions mapOptions = MapOptions.create();
        mapOptions.setCenter(LatLng.create(54.4898398, 26.8960599));
        mapOptions.setZoom(18.0);
        mapOptions.setMapTypeId(MapTypeId.SATELLITE);
        map = GoogleMap.create(mapContainer.getElement(), mapOptions);
        map.setTilt(45);
    }

    public void showMarker(IDeviceLocationModel deviceLocationModel) {
        if (marker != null) {
            marker.setMap((GoogleMap) null);
        }
        MarkerOptions newMarkerOpts = MarkerOptions.create();
        LatLng myLatLng = LatLng.create(deviceLocationModel.getLatitude(), deviceLocationModel.getLongitude());
        newMarkerOpts.setPosition(myLatLng);
        newMarkerOpts.setMap(map);
        newMarkerOpts.setTitle("Hello World!");
        marker = Marker.create(newMarkerOpts);
        map.setCenter(myLatLng);

        if (circle != null) {
            circle.setMap(null);
        }
        CircleOptions populationOptions = CircleOptions.create();
        populationOptions.setStrokeColor("#0000ff");
        populationOptions.setStrokeOpacity(0.8);
        populationOptions.setStrokeWeight(2);
        populationOptions.setFillColor("#0000bb");
        populationOptions.setFillOpacity(0.35);
        populationOptions.setMap(map);
        populationOptions.setCenter(myLatLng);
        populationOptions.setRadius(deviceLocationModel.getAccuracy());
        circle = Circle.create(populationOptions);
    }

    @Override
    public MapBodyPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void setPresenter(MapBodyPresenter presenter) {
        this.presenter = presenter;
    }

    public GoogleMap getMap(){
        return map;
    }

    public void showMarker1(List<DeviceMarkerModel> deviceMarkerModelList) {
        if (!deviceMarkerModelList.isEmpty()) {
            double averageLat = 0.;
            double averageLon = 0.;
            for (DeviceMarkerModel deviceMarkerModel : deviceMarkerModelList) {
                averageLat += deviceMarkerModel.getDeviceLocationModel().getLatitude();
                averageLon += deviceMarkerModel.getDeviceLocationModel().getLongitude();
            }
            LatLng myLatLng = LatLng.create(averageLat / deviceMarkerModelList.size(), averageLon / deviceMarkerModelList.size());
            map.setCenter(myLatLng);
        }
    }


    public static interface Binder extends UiBinder<SimplePanel, MapBodyView> {
    }
}
