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
import com.artigile.howismyphonedoing.client.Messages;
import com.artigile.howismyphonedoing.client.rpc.AsyncCallbackImpl;
import com.artigile.howismyphonedoing.client.rpc.AuthRpcServiceAsync;
import com.artigile.howismyphonedoing.client.service.ApplicationState;
import com.artigile.howismyphonedoing.client.service.GaeChannelService;
import com.artigile.howismyphonedoing.shared.entity.GooglePlusAuthenticatedUser;
import com.artigile.howismyphonedoing.shared.entity.StateAndChanelEntity;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
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
    @UiField
    FlowPanel loadingIcon;
    @Inject
    private ApplicationState applicationState;
    @Inject
    private AuthRpcServiceAsync authRpcService;

    @Inject
    private GaeChannelService gaeChannelService;


    private boolean loginButtonLoaded;
    private AsyncCallbackImpl.AfterRpcResponceHandler afterRpcResponceHandler;

    @Inject
    public SigninWithGooglePlusWindow(Binder binder) {
        binder.createAndBindUi(this);
        exportOnGoogleResponseReadyMethod();
        loadingIcon.setVisible(false);
        afterRpcResponceHandler = initAfterResponseHandler();
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
        loadingIcon.setVisible(true);
        signInWithGoogleButtonPanel.setVisible(false);
        authRpcService.validateGooglePlusCallback(googlePlusAuthenticatedUser, new AsyncCallbackImpl<StateAndChanelEntity>(eventBus, afterRpcResponceHandler) {
            @Override
            public void success(StateAndChanelEntity token) {
                eventBus.userLoggedIn(token);
            }

            @Override
            public void failure(Throwable caught) {
                Window.alert("Failed to validate google credentials");
            }
        });
    }

    public void onUserLoggedIn(StateAndChanelEntity stateAndChanelEntity) {
        hide();
        gaeChannelService.initGaeChannel(stateAndChanelEntity.getChanelToken());
    }

    public void onShowLoginWindow(String key) {
        applicationState.setStateKey(key);
        loadGooglePlusLoginScript();
        show();
    }

    private AsyncCallbackImpl.AfterRpcResponceHandler initAfterResponseHandler() {
        return new AsyncCallbackImpl.AfterRpcResponceHandler() {
            @Override
            public void afterResponse() {
                loadingIcon.setVisible(false);
                signInWithGoogleButtonPanel.setVisible(true);
            }
        };
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
