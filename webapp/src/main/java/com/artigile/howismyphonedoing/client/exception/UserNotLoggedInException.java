package com.artigile.howismyphonedoing.client.exception;

/**
 * @author IoaN, 5/26/13 10:44 AM
 */
public class UserNotLoggedInException extends Exception {
    public UserNotLoggedInException() {
    }

    public UserNotLoggedInException(String message) {
        super(message);
    }
}
