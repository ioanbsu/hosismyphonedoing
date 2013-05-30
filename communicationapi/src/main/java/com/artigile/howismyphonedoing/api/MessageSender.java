package com.artigile.howismyphonedoing.api;

/**
 * @author IoaN, 5/29/13 9:51 PM
 */
public interface MessageSender<T> {

    public void sendMessage(T key, String messageType,String message);
}
