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

    @Override
    public MapBodyPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void setPresenter(MapBodyPresenter presenter) {
        this.presenter = presenter;
    }

    public GoogleMap getMap() {
        return map;
    }

    public void showMarkers(List<DeviceMarkerModel> deviceMarkerModelList) {
        if (!deviceMarkerModelList.isEmpty()) {
            LatLngBounds latLngBounds = LatLngBounds.create();
            for (DeviceMarkerModel deviceMarkerModel : deviceMarkerModelList) {
                LatLng myLatLng = LatLng.create(deviceMarkerModel.getDeviceLocationModel().getLatitude(),
                        deviceMarkerModel.getDeviceLocationModel().getLongitude());
                latLngBounds.extend(myLatLng);
            }
            double savedZoom = map.getZoom();
            map.fitBounds(latLngBounds);
            if (Math.abs(savedZoom - map.getZoom()) > 3) {
                map.setZoom(savedZoom);
            }
        }

    }


    public static interface Binder extends UiBinder<SimplePanel, MapBodyView> {
    }
}
