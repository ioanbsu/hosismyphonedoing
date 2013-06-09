package com.artigile.howismyphonedoing.client.mainpage;

import com.artigile.howismyphonedoing.api.model.IDeviceLocationModel;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import com.google.maps.gwt.client.*;
import com.mvp4g.client.view.ReverseViewInterface;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author IoaN, 5/26/13 9:10 AM
 */
@Singleton
public class MainPageView extends Composite implements ReverseViewInterface<MainPagePresenter> {


    @UiField
    Button logoutButton;
    @UiField
    Button sendTextToPhone;
    @UiField
    TextBox textToSend;
    @UiField
    Button phoneInfoButton;
    @UiField
    Label phoneInfo;
    @UiField
    Button phoneLocation;
    @UiField
    SimplePanel mapContainer;
    @UiField
    Button removeAllDevices;
    GoogleMap map;
    private MainPagePresenter mainPagePresenter;
    private Marker marker;
    private Circle circle;

    @Inject
    public MainPageView(Binder binder) {
        initWidget(binder.createAndBindUi(this));
        MapOptions mapOptions = MapOptions.create();
        mapOptions.setCenter(LatLng.create(-25.363882, 131.044922));
        mapOptions.setZoom(18.0);
        mapOptions.setMapTypeId(MapTypeId.SATELLITE);

        map = GoogleMap.create(mapContainer.getElement(), mapOptions);
        map.setTilt(45);
    }

    @Override
    public MainPagePresenter getPresenter() {
        return mainPagePresenter;
    }

    @Override
    public void setPresenter(MainPagePresenter mainPagePresenter) {
        this.mainPagePresenter = mainPagePresenter;
    }

    @UiHandler("logoutButton")
    void logOutButtonClickHandler(ClickEvent clickEvent) {
        mainPagePresenter.logout();
    }

    @UiHandler("sendTextToPhone")
    void sendTextToPhoneButtonClickHandler(ClickEvent clickEvent) {
        mainPagePresenter.sendTextToPhone(textToSend.getText());
    }

    @UiHandler("phoneInfoButton")
    void phoneInfoButtonButtonClickHandler(ClickEvent clickEvent) {
        mainPagePresenter.getPhonesInfo();
    }

    @UiHandler("phoneLocation")
    void getPhoneLocationHandler(ClickEvent event) {
        mainPagePresenter.getPhoneLocation();
    }

    @UiHandler("removeAllDevices")
    void getRemoveAllDevicesHandler(ClickEvent event) {
        mainPagePresenter.removeAllDevices();
    }

    public void setPhoneInfo(String result) {
        phoneInfo.setText(result);
    }

    public void showMarker(IDeviceLocationModel deviceLocationModel) {
        if (marker != null) {
            marker.setMap((GoogleMap)null);
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


    public static interface Binder extends UiBinder<DockLayoutPanel, MainPageView> {
    }
}
