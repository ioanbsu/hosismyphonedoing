package com.artigile.howismyphonedoing.server.config;

import com.artigile.howismyphonedoing.client.rpc.GreetingRpcService;
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
        mappings.put("/register", "registerDeviceServlet");
        mappings.put("/sendAllPhones", "sendAllMessageServlet");
        simpleUrlHandlerMapping.setMappings(mappings);
        return simpleUrlHandlerMapping;
    }


}
