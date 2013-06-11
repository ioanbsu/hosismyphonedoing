package com.artigile.howismyphonedoing.shared.entity;

import java.io.Serializable;

/**
 * @author IoaN, 6/8/13 10:17 PM
 */
public class StateAndChanelEntity implements Serializable {

    private String chanelToken;

    private String stateSecret;

    private String email;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
