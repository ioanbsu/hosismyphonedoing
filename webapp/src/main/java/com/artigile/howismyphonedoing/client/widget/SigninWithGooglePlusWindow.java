package com.artigile.howismyphonedoing.client.widget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author IoaN, 5/27/13 8:37 PM
 */
@Singleton
public class SigninWithGooglePlusWindow {

    @UiField
    DialogBox showSignInWithGoogle;
    @UiField
    Button hideWindow;

    @Inject
    public SigninWithGooglePlusWindow(Binder binder) {
        binder.createAndBindUi(this);
    }

    public void show() {
        showSignInWithGoogle.center();
    }

    public void hide() {
        showSignInWithGoogle.hide();
    }

    @UiHandler("hideWindow")
    void hideWindowClickHandler(ClickEvent event) {
        hide();
    }

    interface Binder extends UiBinder<DialogBox, SigninWithGooglePlusWindow> {
    }
}
