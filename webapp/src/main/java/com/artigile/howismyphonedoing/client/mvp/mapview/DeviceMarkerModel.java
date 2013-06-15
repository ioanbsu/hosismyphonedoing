package com.artigile.howismyphonedoing.client.mvp.mapview;

import com.artigile.howismyphonedoing.api.model.IDeviceLocationModel;
import com.google.maps.gwt.client.Circle;
import com.google.maps.gwt.client.Marker;

/**
 * User: ioanbsu
 * Date: 6/14/13
 * Time: 4:51 PM
 */
public class DeviceMarkerModel {

    private Marker marker;
    private Circle circle;
    private IDeviceLocationModel deviceLocationModel;

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public Circle getCircle() {
        return circle;
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
    }

    public IDeviceLocationModel getDeviceLocationModel() {
        return deviceLocationModel;
    }

    public void setDeviceLocationModel(IDeviceLocationModel deviceLocationModel) {
        this.deviceLocationModel = deviceLocationModel;
    }
}
