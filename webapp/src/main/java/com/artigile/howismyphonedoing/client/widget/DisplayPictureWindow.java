package com.artigile.howismyphonedoing.client.widget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Image;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * User: ioanbsu
 * Date: 7/16/13
 * Time: 5:44 PM
 */
@Singleton
public class DisplayPictureWindow {
    @UiField
    Button closeWindow;
    @UiField
    DialogBox messageWindow;
    @UiField
    Image imageToDisplay;
    private AfterCloseButtonClickHandler afterCloseButtonClickHandler;

    @Inject
    public DisplayPictureWindow(Binder binder) {
        binder.createAndBindUi(this);
    }

    public void show(String pictureUrl) {
        show(pictureUrl, null);
    }

    public void show(String linkToImage, AfterCloseButtonClickHandler afterCloseButtonClickHandler) {
        this.afterCloseButtonClickHandler = afterCloseButtonClickHandler;
        imageToDisplay.setUrl(linkToImage);
        messageWindow.center();
    }

    @UiHandler("closeWindow")
    void onCloseWindowClicked(ClickEvent clickEvent) {
        messageWindow.hide();
        if (afterCloseButtonClickHandler != null) {
            afterCloseButtonClickHandler.afterCloseButtonClicked();
        }
    }

    public static interface AfterCloseButtonClickHandler {
        void afterCloseButtonClicked();
    }

    interface Binder extends UiBinder<DialogBox, DisplayPictureWindow> {
    }
}
