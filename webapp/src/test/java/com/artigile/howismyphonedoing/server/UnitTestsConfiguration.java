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
