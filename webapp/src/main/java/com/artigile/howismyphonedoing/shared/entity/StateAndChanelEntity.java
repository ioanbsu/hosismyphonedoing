package com.artigile.howismyphonedoing.shared.entity;

import java.io.Serializable;

/**
 * @author IoaN, 6/8/13 10:17 PM
 */
public class StateAndChanelEntity implements Serializable {

    private String chanelToken;

    private String stateSecret;

    private boolean isUserInSession;

    public String getChanelToken() {
        return chanelToken;
    }

    public void setChanelToken(String chanelToken) {
        this.chanelToken = chanelToken;
    }

    public String getStateSecret() {
        return stateSecret;
    }

    public void setStateSecret(String stateSecret) {
        this.stateSecret = stateSecret;
    }

    public boolean isUserInSession() {
        return isUserInSession;
    }

    public void setUserInSession(boolean userInSession) {
        isUserInSession = userInSession;
    }
}
