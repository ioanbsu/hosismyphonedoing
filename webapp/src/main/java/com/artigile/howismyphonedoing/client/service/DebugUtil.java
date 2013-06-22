package com.artigile.howismyphonedoing.client.service;

import com.google.gwt.user.client.History;

/**
 * Date: 6/22/13
 * Time: 10:23 AM
 *
 * @author ioanbsu
 */

public class DebugUtil {

    private DebugUtil() {

    }

    public static boolean isDebugMode() {
        return "debugMode".equals(History.getToken());

    }
}
