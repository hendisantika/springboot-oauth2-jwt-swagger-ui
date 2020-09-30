package com.hendisantika.springbootoauth2jwtswaggerui.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-oauth2-jwt-swagger-ui
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 30/09/20
 * Time: 07.09
 */
@Configuration
@ComponentScan("com.hendisantika")
@EnableJpaRepositories("com.hendisantika.repository")
public class PersistentContext {
}
