package com.hendisantika.springbootoauth2jwtswaggerui.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
public class SwaggerConfig {

    public static final String securitySchemaOAuth2 = "oauth2schema";
    public static final String securitySchemaBearer = "bearerAuth";

    @Value("${config.oauth2.accessTokenUri}")
    private String accessTokenUri;

    @Bean
    public OpenAPI productApi() {
        return new OpenAPI()
                .info(apiInfo())
                .components(new Components()
                        .addSecuritySchemes(securitySchemaOAuth2, securitySchema())
                        .addSecuritySchemes(securitySchemaBearer, new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Bearer access token")))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemaOAuth2, "read").addList(securitySchemaBearer));
    }

    private SecurityScheme securitySchema() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.OAUTH2)
                .flows(new OAuthFlows().password(new OAuthFlow()
                        .tokenUrl(accessTokenUri)
                        .scopes(new Scopes()
                                .addString("read", "read all")
                                .addString("write", "access all"))));
    }

    /**
     * @return ApiInfo
     */
    private Info apiInfo() {
        return new Info().title("Authentication API").description("Spring Boot OAuth2 (password grant) + JWT + Swagger UI")
                .termsOfService("https://www.example.com/api")
                .contact(new Contact().name("Developers").url("https://spring.io/projects/spring-boot"))
                .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0"))
                .version("1.0.0");
    }
}
