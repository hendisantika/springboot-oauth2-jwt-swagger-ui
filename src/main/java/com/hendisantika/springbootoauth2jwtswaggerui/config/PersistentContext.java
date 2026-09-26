package com.hendisantika.springbootoauth2jwtswaggerui.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-oauth2-jwt-swagger-ui
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 30/09/20
 * Time: 07.09
 * <p>
 * The embedded H2 DataSource, EntityManagerFactory and TransactionManager are auto-configured by Spring Boot.
 * Schema and seed data are loaded from {@code schema.sql} and {@code data.sql}.
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("com.hendisantika.springbootoauth2jwtswaggerui.repository")
public class PersistentContext {
}
