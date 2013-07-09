package com.artigile.howismyphonedoing.client.service;

import com.google.gwt.core.client.GWT;
import com.mvp4g.client.event.Mvp4gLogger;

import javax.inject.Singleton;

/**
 * User: ioanbsu
 * Date: 7/2/13
 * Time: 9:34 AM
 */
@Singleton
public class CustomLogger implements Mvp4gLogger {

    /**
     * Displays logs into chrome console for debugging.
     *
     * @param logMessage the message to display
     */
    public static native void logToBrowserConsole(String logMessage) /*-{
        console.log(logMessage);
    }-*/;

    public void log(String message, int depth) {
        String indent = "";
        for (int i = 0; i < depth; ++i)
            indent += "    ";
        GWT.log(indent + "CustomLogger: " + message);
        if (DebugUtil.isDebugMode()) {
            logToBrowserConsole(message);
        }
    }
}
