package com.hendisantika.springbootoauth2jwtswaggerui.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.converter.RsaKeyConverters;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimValidator;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.io.IOException;
import java.io.InputStream;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-oauth2-jwt-swagger-ui
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 30/09/20
 * Time: 07.07
 * <p>
 * Signs (private key) and verifies (public key) the RS256 JWT access tokens issued by {@code /oauth/token}.
 */
@Configuration
@Log4j2
public class AuthorizationServerConfig {

    @Value("${config.oauth2.issuer}")
    private String issuer;

    @Value("${config.oauth2.resource.id}")
    private String resourceId;

    @Bean
    public RSAPublicKey jwtPublicKey(@Value("${config.oauth2.publicKey}") Resource publicKey) throws IOException {
        log.info("Initializing JWT with public key: {}", publicKey);
        try (InputStream in = publicKey.getInputStream()) {
            return RsaKeyConverters.x509().convert(in);
        }
    }

    @Bean
    public RSAPrivateKey jwtPrivateKey(@Value("${config.oauth2.privateKey}") Resource privateKey) throws IOException {
        try (InputStream in = privateKey.getInputStream()) {
            return RsaKeyConverters.pkcs8().convert(in);
        }
    }

    @Bean
    public JwtEncoder jwtEncoder(RSAPublicKey jwtPublicKey, RSAPrivateKey jwtPrivateKey) {
        RSAKey rsaKey = new RSAKey.Builder(jwtPublicKey).privateKey(jwtPrivateKey).build();
        return new NimbusJwtEncoder(new ImmutableJWKSet<>(new JWKSet(rsaKey)));
    }

    @Bean
    public JwtDecoder jwtDecoder(RSAPublicKey jwtPublicKey) {
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withPublicKey(jwtPublicKey).build();
        OAuth2TokenValidator<Jwt> audience =
                new JwtClaimValidator<List<String>>("aud", aud -> aud != null && aud.contains(resourceId));
        decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(
                JwtValidators.createDefaultWithIssuer(issuer), audience));
        return decoder;
    }
}
