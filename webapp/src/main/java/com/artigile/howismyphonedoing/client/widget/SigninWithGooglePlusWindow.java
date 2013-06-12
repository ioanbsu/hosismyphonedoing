/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.howismyphonedoing.client.widget;

import com.artigile.howismyphonedoing.client.MainEventBus;
import com.artigile.howismyphonedoing.client.rpc.AsyncCallbackImpl;
import com.artigile.howismyphonedoing.client.rpc.AuthRpcServiceAsync;
import com.artigile.howismyphonedoing.client.service.ApplicationState;
import com.artigile.howismyphonedoing.shared.entity.GooglePlusAuthenticatedUser;
import com.artigile.howismyphonedoing.shared.entity.StateAndChanelEntity;
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
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author IoaN, 5/27/13 8:37 PM
 */
@EventHandler
@Singleton
public class SigninWithGooglePlusWindow extends BaseEventHandler<MainEventBus> {

    @UiField
    DialogBox showSignInWithGoogle;
    @UiField
    Button hideWindow;
    @UiField
    HTMLPanel signInWithGoogleButtonPanel;
    @Inject
    private ApplicationState applicationState;
    @Inject
    private AuthRpcServiceAsync authRpcService;
    private boolean loginButtonLoaded;

    @Inject
    public SigninWithGooglePlusWindow(Binder binder) {
        binder.createAndBindUi(this);
        exportOnGoogleResponseReadyMethod();
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

    public void onGooglePlusCallbackEvent(String code, String accessToken, String clientId, String error) {
        GooglePlusAuthenticatedUser googlePlusAuthenticatedUser = new GooglePlusAuthenticatedUser();
        googlePlusAuthenticatedUser.setCode(code);
        googlePlusAuthenticatedUser.setAccessToken(accessToken);
        googlePlusAuthenticatedUser.setClientId(clientId);
        googlePlusAuthenticatedUser.setState(applicationState.getStateKey());
        authRpcService.validateGooglePlusCallback(googlePlusAuthenticatedUser, new AsyncCallbackImpl<StateAndChanelEntity>() {
            @Override
            public void success(StateAndChanelEntity token) {
                eventBus.userLoggedIn(token);
            }
        });
    }

    public void onUserLoggedIn(StateAndChanelEntity stateAndChanelEntity) {
        hide();
    }

    public native void exportOnGoogleResponseReadyMethod() /*-{
        var _this = this;
        $wnd.exposedMethod = function (code, accessToken, idToken, error) {
            _this.@com.artigile.howismyphonedoing.client.widget.SigninWithGooglePlusWindow::onGooglePlusCallbackEvent(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(code, accessToken, idToken, error);
        }
    }-*/;

    interface Binder extends UiBinder<DialogBox, SigninWithGooglePlusWindow> {
    }
}
