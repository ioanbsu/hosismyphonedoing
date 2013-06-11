package com.artigile.howismyphonedoing.client.mvp.toppanel;

import com.artigile.howismyphonedoing.client.Messages;
import com.google.gwt.event.dom.client.ClickEvent;
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

    private TopPanelPresenter presenter;
    @UiField
    Button logoutButton;
    @UiField
    Button sendTextToPhone;
    @UiField
    Button phoneInfoButton;
    @UiField
    Button removeAllDevices;
    @UiField
    Button phoneLocation;
    @UiField
    TextBox textToSend;
    @UiField
    Label loggedInAs;

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

    @UiHandler("phoneInfoButton")
    void phoneInfoButtonButtonClickHandler(ClickEvent clickEvent) {
        presenter.getPhonesInfo();
    }

    @UiHandler("phoneLocation")
    void getPhoneLocationHandler(ClickEvent event) {
        presenter.sendRequestToUpdatePhoneLocation();
    }

    @UiHandler("removeAllDevices")
    void getRemoveAllDevicesHandler(ClickEvent event) {
        presenter.removeAllDevices();
    }

    public void setLoggedInUserData(String email) {
        loggedInAs.setText(messages.logged_in_as(email));
    }


    public static interface Binder extends UiBinder<FlowPanel, TopPanelView> {
    }
}
