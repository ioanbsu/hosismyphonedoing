package com.artigile.howismyphonedoing.api;

/**
 * User: ioanbsu
 * Date: 5/31/13
 * Time: 7:31 PM
 */
public interface MessageProcessor {

    void processMessage(String messageType, String message);
}
