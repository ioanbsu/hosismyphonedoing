package com.artigile.howismyphonedoing.client.widget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.plaf.RootPaneUI;

/**
 * @author IoaN, 5/27/13 8:37 PM
 */
@Singleton
public class SigninWithGooglePlusWindow {

    @UiField
    DialogBox showSignInWithGoogle;
    @UiField
    Button hideWindow;
    @UiField
    HTMLPanel signInWithGoogleButtonPanel;

    @Inject
    public SigninWithGooglePlusWindow(Binder binder) {
        binder.createAndBindUi(this);
        signInWithGoogleButtonPanel.add(RootPanel.get("signInButtonPanel"));
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

    public native void addScript(String uniqueId, String url) /*-{
      var elem = document.createElement("script");
      elem.setAttribute("language", "JavaScript");
      elem.setAttribute("src", url);
      document.getElementsByTagName("body")[0].appendChild(elem);
    }-*/;
}
