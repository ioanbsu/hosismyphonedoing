package com.artigile.howismyphonedoing.client.service;

import javax.inject.Singleton;

/**
 * @author IoaN, 6/8/13 10:21 PM
 */
@Singleton
public class ApplicationState {

    private String stateKey;

    public String getStateKey() {
        return stateKey;
    }

    public void setStateKey(String stateKey) {
        this.stateKey = stateKey;
    }

}
