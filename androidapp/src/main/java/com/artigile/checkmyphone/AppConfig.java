package com.artigile.checkmyphone;

import com.artigile.howismyphonedoing.api.MessageSender;
import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

/**
 * User: ioanbsu
 * Date: 5/20/13
 * Time: 6:00 PM
 */
public class AppConfig extends AbstractModule {
    @Override
    protected void configure() {
        bind(EventBus.class).asEagerSingleton();
        bind(new TypeLiteral<MessageSender<String>>(){}).to(MessageSenderImpl.class);
    }
}