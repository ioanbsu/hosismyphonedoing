/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.howismyphonedoing.server.config;

import com.artigile.howismyphonedoing.api.MessageParser;
import com.artigile.howismyphonedoing.api.MessageParserImpl;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * User: ioanbsu
 * Date: 5/21/13
 * Time: 5:54 PM
 */
@Configuration
@EnableWebMvc
@Import({SpringPersistenceConfig.class})
@ComponentScan({"com.artigile.howismyphonedoing", "com.artigile.howismyphonedoing.client.rpc"})
@PropertySource(value = {"classpath:application.properties"})
public class AnnotationConfigMyPhoneWebApplicationContext {

    @Bean
    public AnnotationAwareAspectJAutoProxyCreator annotationAwareAspectJAutoProxyCreator() {
        return new AnnotationAwareAspectJAutoProxyCreator();
    }

    @Bean
    public MessageParser messageParser() {
        return new MessageParserImpl();
    }


}
