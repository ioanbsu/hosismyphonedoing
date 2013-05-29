package com.artigile.checkmyphone;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;

/**
 * User: ioanbsu
 * Date: 5/20/13
 * Time: 6:00 PM
 */
public class AppConfig extends AbstractModule {
    @Override
    protected void configure() {
        bind(EventBus.class).asEagerSingleton();
    }
}