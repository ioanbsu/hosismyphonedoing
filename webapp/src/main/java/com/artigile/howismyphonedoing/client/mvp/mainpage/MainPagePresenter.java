package com.artigile.howismyphonedoing.client.mvp.mainpage;

import com.artigile.howismyphonedoing.client.MainEventBus;
import com.artigile.howismyphonedoing.client.rpc.AsyncCallbackImpl;
import com.artigile.howismyphonedoing.client.rpc.AuthRpcServiceAsync;
import com.artigile.howismyphonedoing.client.service.ApplicationState;
import com.artigile.howismyphonedoing.client.service.GaeChannelService;
import com.artigile.howismyphonedoing.client.widget.SigninWithGooglePlusWindow;
import com.artigile.howismyphonedoing.shared.entity.GooglePlusAuthenticatedUser;
import com.artigile.howismyphonedoing.shared.entity.StateAndChanelEntity;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import javax.inject.Inject;

/**
 * @author IoaN, 5/26/13 9:07 AM
 */
@Presenter(view = MainPageView.class)
public class MainPagePresenter extends BasePresenter<MainPageView, MainEventBus> {

    @Inject
    private AuthRpcServiceAsync authRpcService;
    @Inject
    private SigninWithGooglePlusWindow signinWithGooglePlusWindow;
    @Inject
    private ApplicationState applicationState;
    @Inject
    private GaeChannelService gaeChannelService;

    public void onInitApp() {
        exportStaticMethod(this);
        signinWithGooglePlusWindow.show();
        authRpcService.getLoggedInUser(new AsyncCallbackImpl<StateAndChanelEntity>() {
            @Override
            public void success(StateAndChanelEntity token) {
                eventBus.userLoggedIn(token);
            }

            @Override
            public void failure(Throwable caught) {
                applicationState.setStateKey(caught.getMessage());
                signinWithGooglePlusWindow.loadGooglePlusLoginScript();
            }
        });
        GWT.log("MainPagePresenter initiated.");
    }

    public void onUserLogout() {
        signinWithGooglePlusWindow.show();
        signinWithGooglePlusWindow.loadGooglePlusLoginScript();
        authRpcService.logout(new AsyncCallbackImpl<Void>() {
            @Override
            public void success(Void result) {
            }

            @Override
            public void failure(Throwable caught) {
                Window.alert("Log out failed, please try refreshing the page and do logout again.");
            }
        });
    }

    public void onUserLoggedIn(StateAndChanelEntity stateAndChanelEntity) {
        gaeChannelService.initGaeChannel(stateAndChanelEntity.getChanelToken());
        signinWithGooglePlusWindow.hide();
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

            @Override
            public void failure(Throwable caught) {

            }
        });
    }


    public native void exportStaticMethod(MainPagePresenter thiz) /*-{
        var _this = this;

        $wnd.exposedMethod = function (code, accessToken, idToken, error) {
            _this.@com.artigile.howismyphonedoing.client.mvp.mainpage.MainPagePresenter::onGooglePlusCallbackEvent(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(code, accessToken, idToken, error);
        }
    }-*/;


}
