package com.artigile.howismyphonedoing.api;

import java.io.IOException;

/**
 * @author IoaN, 5/29/13 9:51 PM
 */
public interface AndroidMessageProcessor<T> {

    public String processMessage( MessageType messageType, String message) throws IOException;
}
