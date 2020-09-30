package com.hendisantika.springbootoauth2jwtswaggerui.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-oauth2-jwt-swagger-ui
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 30/09/20
 * Time: 07.14
 */
@Configuration
@EnableSwagger2
@ComponentScan(basePackages = "com.hendisantika.controller")
public class SwaggerConfig {


    public static final String securitySchemaOAuth2 = "oauth2schema";
    public static final String authorizationScopeGlobal = "global";
    public static final String authorizationScopeGlobalDesc = "accessEverything";
    @Value("${config.oauth2.accessTokenUri}")
    private String accessTokenUri;
}
