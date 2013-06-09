package com.artigile.howismyphonedoing.shared.entity;


import java.io.Serializable;

/**
 * @author IoaN, 5/21/13 10:35 PM
 */
public class GooglePlusAuthenticatedUser implements Serializable {

    private String code;
    private String accessToken;
    private String clientId;
    private String state;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
