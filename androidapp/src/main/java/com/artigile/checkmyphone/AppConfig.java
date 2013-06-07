package com.artigile.checkmyphone;

import com.artigile.checkmyphone.service.AndroidMessageSender;
import com.artigile.howismyphonedoing.api.AndroidMessageProcessor;
import com.artigile.howismyphonedoing.api.MessageParser;
import com.artigile.howismyphonedoing.api.MessageParserImpl;
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
        bind(new TypeLiteral<AndroidMessageProcessor<String>>(){}).to(AndroidMessageSender.class);
        bind(MessageParser.class).to(MessageParserImpl.class);
    }
}