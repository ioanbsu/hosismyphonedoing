package com.artigile.howismyphonedoing.client;

import com.google.gwt.user.client.Window;
import net.zschech.gwt.comet.client.CometListener;

import javax.inject.Singleton;
import java.io.Serializable;
import java.util.List;

/**
 * @author IoaN, 5/23/13 9:58 PM
 */
@Singleton
public class CometListenerImpl implements CometListener {


    @Override
    public void onConnected(int i) {

    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onError(Throwable throwable, boolean b) {

    }

    @Override
    public void onHeartbeat() {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onMessage(List<? extends Serializable> messages) {
        for (Serializable message : messages) {
            Window.alert("TEST!!!");
        }
    }
}
