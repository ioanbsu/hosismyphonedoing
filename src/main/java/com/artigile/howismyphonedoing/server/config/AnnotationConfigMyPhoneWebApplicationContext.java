package com.artigile.howismyphonedoing.server.config;

import com.artigile.howismyphonedoing.client.rpc.GreetingRpcService;
import org.gwtwidgets.server.spring.GWTRPCServiceExporter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * User: ioanbsu
 * Date: 5/21/13
 * Time: 5:54 PM
 */
@Configuration
@EnableWebMvc
@ComponentScan({"com.artigile.howismyphonedoing", "com.artigile.howismyphonedoing.client.rpc"})
@PropertySource(value = {"classpath:application.properties"})
public class AnnotationConfigMyPhoneWebApplicationContext {


    @Bean
    public GWTRPCServiceExporter mainHowIsMyPhoneDoingServiceExporter(GreetingRpcService creetingRpcService) {
        GWTRPCServiceExporter invoiceAppServiceExporter = new GWTRPCServiceExporter();
        invoiceAppServiceExporter.setService(creetingRpcService);
        return invoiceAppServiceExporter;
    }

}
