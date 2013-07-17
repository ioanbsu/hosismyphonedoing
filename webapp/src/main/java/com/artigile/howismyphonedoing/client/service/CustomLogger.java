package com.artigile.howismyphonedoing.client.service;

import com.mvp4g.client.event.Mvp4gLogger;

import javax.inject.Singleton;
import java.util.logging.Logger;

/**
 * User: ioanbsu
 * Date: 7/2/13
 * Time: 9:34 AM
 */
@Singleton
public class CustomLogger implements Mvp4gLogger {

    public static final Logger logger = Logger.getLogger(CustomLogger.class.getName());

    public void log(String message, int depth) {
        logger.info(message);
    }
}
