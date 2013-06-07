package com.artigile.howismyphonedoing.client.mainpage;

import com.artigile.howismyphonedoing.api.model.IDeviceLocationModel;
import com.artigile.howismyphonedoing.client.rpc.AsyncCallbackImpl;
import com.artigile.howismyphonedoing.client.rpc.AuthRpcServiceAsync;
import com.artigile.howismyphonedoing.client.rpc.MessageRpcServiceAsync;
import com.artigile.howismyphonedoing.client.service.HowIsMyPhoneDoingFactory;
import com.artigile.howismyphonedoing.client.widget.SigninWithGooglePlusWindow;
import com.artigile.howismyphonedoing.shared.entity.GooglePlusAuthenticatedUser;
import com.google.gwt.appengine.channel.client.*;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.SerializationStreamFactory;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import javax.inject.Inject;

/**
 * @author IoaN, 5/26/13 9:07 AM
 */
@Presenter(view = MainPageView.class)
public class MainPagePresenter extends BasePresenter<MainPageView, MainEventBus> {

    private Socket socket;
    @Inject
    private AuthRpcServiceAsync authRpcService;
    @Inject
    private MessageRpcServiceAsync messageRpcServiceAsync;
    @Inject
    private SigninWithGooglePlusWindow signinWithGooglePlusWindow;
    @Inject
    private ChannelFactory channelFactory;
    @Inject
    private HowIsMyPhoneDoingFactory howIsMyPhoneDoingFactory;
    private SerializationStreamFactory pushServiceStreamFactory;

    public void onGooglePlusCallbackEvent(String code, String accessToken, String clientId, String error) {
        GooglePlusAuthenticatedUser googlePlusAuthenticatedUser = new GooglePlusAuthenticatedUser();
        googlePlusAuthenticatedUser.setCode(code);
        googlePlusAuthenticatedUser.setAccessToken(accessToken);
        googlePlusAuthenticatedUser.setClientId(clientId);
        authRpcService.validateGooglePlusCallback(googlePlusAuthenticatedUser, new AsyncCallbackImpl<String>() {
            @Override
            public void success(String token) {
                initGaeChannel(token);
            }

            @Override
            public void failure(Throwable caught) {
            }
        });
    }

    private void initGaeChannel(String token) {
        Channel channel = channelFactory.createChannel(token);
        socket = channel.open(new SocketListener() {
            @Override
            public void onOpen() {
                Window.alert("Channel opened!");
            }

            @Override
            public void onMessage(String encodedData) {
                view.setPhoneInfo("MESSAGE FROM CHANNEL!!!" + encodedData);
                AutoBean<IDeviceLocationModel> bean = AutoBeanCodex.decode(howIsMyPhoneDoingFactory, IDeviceLocationModel.class, encodedData);
                view.showMarker(bean.as());
            }

            @Override
            public void onError(ChannelError error) {
                Window.alert("Channel error: " + error.getCode() + " : " + error.getDescription());
            }

            @Override
            public void onClose() {
                Window.alert("Channel closed!");
            }
        });
    }

    public void onInitApp() {
        exportStaticMethod(this);
        authRpcService.userIsInSession(new AsyncCallbackImpl<String>() {
            @Override
            public void success(String token) {
                initGaeChannel(token);
            }

            @Override
            public void failure(Throwable caught) {
                signinWithGooglePlusWindow.show();
            }
        });
    }

    public void logout() {
        if (socket != null) {
            socket.close();
        }
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

    public void sendTextToPhone(String text) {
        messageRpcServiceAsync.sendSimpleTextMessage(text, new AsyncCallbackImpl<String>() {
        });
    }

    public void getPhonesInfo() {
        messageRpcServiceAsync.getPhoneInfo(new AsyncCallbackImpl<String>() {
            @Override
            public void success(String result) {
                view.setPhoneInfo(result);
            }
        });
    }

    public void getPhoneLocation() {
        messageRpcServiceAsync.getPhoneLocation(new AsyncCallbackImpl<String>() {
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
