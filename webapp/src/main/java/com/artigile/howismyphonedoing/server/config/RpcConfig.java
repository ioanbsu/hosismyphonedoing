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

import com.artigile.howismyphonedoing.api.CommonConstants;
import com.artigile.howismyphonedoing.client.rpc.AuthRpcService;
import com.artigile.howismyphonedoing.client.rpc.MessageRpcService;
import com.artigile.howismyphonedoing.client.rpc.UserInfoRpcService;
import org.gwtwidgets.server.spring.GWTRPCServiceExporter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

import java.util.Properties;

/**
 * User: ioanbsu
 * Date: 5/21/13
 * Time: 5:51 PM
 */
@Controller
public class RpcConfig extends GenericWebApplicationContext {
    @Bean
    public SimpleUrlHandlerMapping simpleUrlHandlerMapping() {
        SimpleUrlHandlerMapping simpleUrlHandlerMapping = new SimpleUrlHandlerMapping();
        Properties mappings = new Properties();
        mappings.put("/mainHowIsMyPhoneDoing", "mainHowIsMyPhoneDoingServiceExporter");
        mappings.put("/authService", "authServiceExporter");
        mappings.put("/userInfoService", "userInfoServiceExporter");
        mappings.put(CommonConstants.MESSAGES_COMMUNICATION_URL, "messageCommunicationServlet");
        simpleUrlHandlerMapping.setMappings(mappings);
        return simpleUrlHandlerMapping;
    }

    @Bean
    public GWTRPCServiceExporter mainHowIsMyPhoneDoingServiceExporter(MessageRpcService creetingRpcService) {
        GWTRPCServiceExporter invoiceAppServiceExporter = new GWTRPCServiceExporter();
        invoiceAppServiceExporter.setService(creetingRpcService);
        return invoiceAppServiceExporter;
    }

    @Bean
    public GWTRPCServiceExporter authServiceExporter(AuthRpcService authRpcService) {
        GWTRPCServiceExporter invoiceAppServiceExporter = new GWTRPCServiceExporter();
        invoiceAppServiceExporter.setService(authRpcService);
        return invoiceAppServiceExporter;
    }

    @Bean
    public GWTRPCServiceExporter userInfoServiceExporter(UserInfoRpcService userInfoRpcService) {
        GWTRPCServiceExporter invoiceAppServiceExporter = new GWTRPCServiceExporter();
        invoiceAppServiceExporter.setService(userInfoRpcService);
        return invoiceAppServiceExporter;
    }


}
