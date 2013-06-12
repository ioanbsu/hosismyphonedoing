/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

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