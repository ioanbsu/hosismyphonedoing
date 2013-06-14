package com.artigile.howismyphonedoing.client.widget;

import com.artigile.howismyphonedoing.client.Messages;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * User: ioanbsu
 * Date: 6/14/13
 * Time: 3:02 PM
 */
@Singleton
public class YesNoWindow {

    @UiField
    HTML promptMessage;
    @UiField
    Button yesButton;
    @UiField
    Button noButton;
    @UiField
    DialogBox dialogBox;
    @Inject
    private Messages messages;
    private ClickHandler yesButtonClickHandler;
    private ClickHandler noButtonClickHandler;

    @Inject
    public YesNoWindow(Binder binder) {
        binder.createAndBindUi(this);
    }

    @UiHandler("yesButton")
    public void yesButtonClickHandler(ClickEvent event) {
        dialogBox.hide();
        if (yesButtonClickHandler != null) {
            yesButtonClickHandler.onClick(event);
        }
    }

    @UiHandler("noButton")
    public void noButtonClickHandler(ClickEvent event) {
        dialogBox.hide();
        if (noButtonClickHandler != null) {
            noButtonClickHandler.onClick(event);
        }
    }

    public void show(ClickHandler yesButtonClickHandler, ClickHandler noButtonClickHandler, String promptMessage) {
        show(yesButtonClickHandler, noButtonClickHandler, messages.yes_no_window_default_yes_button(), messages.yes_no_window_default_no_button(),promptMessage);
    }

    public void show(ClickHandler yesButtonClickHandler, ClickHandler noButtonClickHandler, String yesButtonText, String noButtonText, String promptMessageStr) {
        promptMessage.setHTML(promptMessageStr);
        yesButton.setText(yesButtonText);
        noButton.setText(noButtonText);
        this.yesButtonClickHandler = yesButtonClickHandler;
        this.noButtonClickHandler = noButtonClickHandler;
        dialogBox.center();
    }


    interface Binder extends UiBinder<Widget, YesNoWindow> {
    }
}
