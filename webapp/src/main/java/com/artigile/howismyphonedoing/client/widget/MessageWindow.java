package com.artigile.howismyphonedoing.client.widget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * User: ioanbsu
 * Date: 6/12/13
 * Time: 6:16 PM
 */
@Singleton
public class MessageWindow {

    @UiField
    Label messageWindowText;
    @UiField
    Button closeWindow;
    @UiField
    DialogBox messageWindow;
    private AfterCloseButtonClickHandler afterCloseButtonClickHandler;

    @Inject
    public MessageWindow(Binder binder) {
        binder.createAndBindUi(this);
    }

    public void show(String textToDisplay) {
        show(textToDisplay, null);
    }

    public void show(String textToDisplay, AfterCloseButtonClickHandler afterCloseButtonClickHandler) {
        this.afterCloseButtonClickHandler = afterCloseButtonClickHandler;
        messageWindowText.setText(textToDisplay);
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

    interface Binder extends UiBinder<DialogBox, MessageWindow> {
    }
}
