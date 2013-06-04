package com.artigile.howismyphonedoing.api;

import java.io.IOException;

/**
 * @author IoaN, 5/29/13 9:51 PM
 */
public interface MessageSender<T> {

    public void sendMessage(T key, MessageType messageType, String message) throws IOException;
}
