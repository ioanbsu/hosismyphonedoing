package com.artigile.howismyphonedoing.client.widget;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.PopupPanel;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * User: ioanbsu
 * Date: 10/21/13
 * Time: 11:02 AM
 */
@Singleton
public class UserHasNoDevicesInfoPopup {

    @UiField
    PopupPanel popupPanel;

    @Inject
    public UserHasNoDevicesInfoPopup(Binder binder) {
        binder.createAndBindUi(this);
    }

    public void show() {
        popupPanel.center();
    }

    interface Binder extends UiBinder<PopupPanel, UserHasNoDevicesInfoPopup> {
    }
}
