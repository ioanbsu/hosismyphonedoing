package com.artigile.howismyphonedoing.api;

/**
 * User: ioanbsu
 * Date: 5/31/13
 * Time: 7:31 PM
 */
public interface WebAppMessageProcessor<T> {

    String processMessage(T key,MessageType messageType, String message);
}
