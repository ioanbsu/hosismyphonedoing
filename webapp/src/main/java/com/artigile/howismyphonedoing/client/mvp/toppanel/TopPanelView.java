package com.artigile.howismyphonedoing.client.mvp.toppanel;

import com.artigile.howismyphonedoing.client.Messages;
import com.artigile.howismyphonedoing.client.channel.ChannelStateType;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import com.mvp4g.client.view.ReverseViewInterface;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * User: ioanbsu
 * Date: 6/11/13
 * Time: 12:12 PM
 */
@Singleton
public class TopPanelView extends Composite implements ReverseViewInterface<TopPanelPresenter> {

    @UiField
    Button logoutButton;
    @UiField
    Button sendTextToPhone;
    @UiField
    Button removeAllDevices;
    @UiField
    Button phoneLocation;
    @UiField
    TextBox textToSend;
    @UiField
    Label loggedInAs;
    @UiField
    Button myDevicesCount;
    @UiField
    Label channelStateLabel;
    private TopPanelPresenter presenter;
    @Inject
    private Messages messages;


    @Inject
    public TopPanelView(Binder binder) {
        initWidget(binder.createAndBindUi(this));
    }

    @Override
    public TopPanelPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void setPresenter(TopPanelPresenter presenter) {
        this.presenter = presenter;
    }

    @UiHandler("logoutButton")
    void logOutButtonClickHandler(ClickEvent clickEvent) {
        presenter.logout();
    }

    @UiHandler("sendTextToPhone")
    void sendTextToPhoneButtonClickHandler(ClickEvent clickEvent) {
        presenter.sendTextToPhone(textToSend.getText());
    }


    @UiHandler("phoneLocation")
    void getPhoneLocationHandler(ClickEvent event) {
        presenter.sendRequestToUpdatePhoneLocation();
    }

    @UiHandler("removeAllDevices")
    void getRemoveAllDevicesHandler(ClickEvent event) {
        presenter.removeAllDevices();
    }

    @UiHandler("myDevicesCount")
    void onMyDevicesCountClick(ClickEvent event){
       presenter.showDevicesCountWindow();
    }

    public void setLoggedInUserData(String email) {
        loggedInAs.setText(messages.top_panel_logged_in_as(email));
    }

    public void setMyDevicesCount(int size) {
        myDevicesCount.setText(messages.top_panel_view_my_devices_link(size + ""));
    }

    public void updateChannelStateIcon(ChannelStateType channelState) {
        channelStateLabel.setText(channelState + "");
    }


    public static interface Binder extends UiBinder<FlowPanel, TopPanelView> {
    }
}
