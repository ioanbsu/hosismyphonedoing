package com.artigile.howismyphonedoing.client.mainpage;

import com.artigile.howismyphonedoing.client.rpc.AsyncCallbackImpl;
import com.artigile.howismyphonedoing.client.rpc.AuthRpcServiceAsync;
import com.artigile.howismyphonedoing.client.rpc.GreetingRpcServiceAsync;
import com.artigile.howismyphonedoing.client.widget.SigninWithGooglePlusWindow;
import com.artigile.howismyphonedoing.shared.entity.GooglePlusAuthenticatedUser;
import com.google.gwt.user.client.Window;
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
    private GreetingRpcServiceAsync greetingRpcServiceAsync;
    @Inject
    private SigninWithGooglePlusWindow signinWithGooglePlusWindow;

    public void onGooglePlusCallbackEvent(String code, String accessToken, String clientId, String error) {
        GooglePlusAuthenticatedUser googlePlusAuthenticatedUser = new GooglePlusAuthenticatedUser();
        googlePlusAuthenticatedUser.setCode(code);
        googlePlusAuthenticatedUser.setAccessToken(accessToken);
        googlePlusAuthenticatedUser.setClientId(clientId);
        authRpcService.validateGooglePlusCallback(googlePlusAuthenticatedUser, new AsyncCallbackImpl<String>() {
            @Override
            public void success(String result) {
            }

            @Override
            public void failure(Throwable caught) {
            }
        });
    }

    public void onInitApp() {
        exportStaticMethod(this);
        authRpcService.userIsInSession(new AsyncCallbackImpl<String>() {
            @Override
            public void success(String result) {
                Window.alert("User already logged in!");
            }

            @Override
            public void failure(Throwable caught) {
                signinWithGooglePlusWindow.show();
            }
        });
    }

    public void logout() {
        authRpcService.logout(new AsyncCallbackImpl<Void>() {
            @Override
            public void failure(Throwable caught) {
                Window.alert("Log out failed, please try refreshing the page and do logout again.");
            }
        });
    }

    public void sendTextToPhone(String text) {
        greetingRpcServiceAsync.greetServer(text, new AsyncCallbackImpl<String>() {
        });
    }

    public void getPhonesInfo() {
        greetingRpcServiceAsync.getPhoneInfo(new AsyncCallbackImpl<String>() {
            @Override
            public void success(String result) {
                view.setPhoneInfo(result);
            }
        });
    }

    public native void exportStaticMethod(MainPagePresenter thiz) /*-{
        var _this = this;

        $wnd.exposedMethod = function (code, accessToken, idToken, error) {
            _this.@com.artigile.howismyphonedoing.client.mainpage.MainPagePresenter::onGooglePlusCallbackEvent(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(code, accessToken, idToken, error);
        }
    }-*/;
}
