/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.howismyphonedoing.server;

import com.artigile.howismyphonedoing.server.config.AnnotationConfigMyPhoneWebApplicationContext;
import com.artigile.howismyphonedoing.server.config.RpcConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author IoaN, 6/2/13 8:54 PM
 */
@Configuration
@Import({AnnotationConfigMyPhoneWebApplicationContext.class})
@ComponentScan(basePackages = {"com.artigile.howismyphonedoing"})
@EnableWebMvc
public class UnitTestsConfiguration extends WebMvcConfigurationSupport{
}
