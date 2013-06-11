package com.artigile.howismyphonedoing.client.widget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;

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
    @UiField
    HTMLPanel signInWithGoogleButtonPanel;
    private boolean loginButtonLoaded;

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

    public void loadGooglePlusLoginScript() {
        if (!loginButtonLoaded) {
            String url = "https://plus.google.com/js/client:plusone.js?onload=start";
            Element e = DOM.createElement("script");
            DOM.setElementAttribute(e, "language", "JavaScript");
            DOM.setElementAttribute(e, "src", url);
            DOM.appendChild(RootPanel.get().getElement(), e);
            loginButtonLoaded = true;
        }
    }

    public native void addScript(String uniqueId, String url) /*-{
        var elem = document.createElement("script");
        elem.setAttribute("language", "JavaScript");
        elem.setAttribute("src", url);
        document.getElementsByTagName("body")[0].appendChild(elem);
    }-*/;

    interface Binder extends UiBinder<DialogBox, SigninWithGooglePlusWindow> {
    }
}
