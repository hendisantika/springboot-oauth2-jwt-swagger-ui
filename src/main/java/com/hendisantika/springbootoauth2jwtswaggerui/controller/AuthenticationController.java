package com.hendisantika.springbootoauth2jwtswaggerui.controller;

import com.hendisantika.springbootoauth2jwtswaggerui.model.UserTokenSession;
import com.hendisantika.springbootoauth2jwtswaggerui.service.UserTokenSessionServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Arrays;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-oauth2-jwt-swagger-ui
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 30/09/20
 * Time: 07.34
 */
@RestController
@RequestMapping("/oauth")
@Log4j2
@Tag(name = "Authentication Controller", description = "Authenticate user using authorization token.")
public class AuthenticationController {

    private static final List<String> SUPPORTED_SCOPES = List.of("read", "write");

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtEncoder jwtEncoder;

    @Value("${config.oauth2.tokenTimeout}")
    private long tokenExpiryTime;

    @Value("${config.oauth2.clientID}")
    private String clientId;

    @Value("${config.oauth2.clientSecret}")
    private String clientSecret;

    @Value("${config.oauth2.issuer}")
    private String issuer;

    @Value("${config.oauth2.resource.id}")
    private String resourceId;

    @Autowired
    private UserTokenSessionServiceImpl userTokenSessionService;

    /**
     * OAuth2 token endpoint (resource owner password credentials grant).
     * The client authenticates either with HTTP Basic or with {@code client_id}/{@code client_secret} form params.
     */
    @PostMapping(value = "/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Issue a JWT access token using the password grant")
    @SecurityRequirements
    public ResponseEntity<Map<String, Object>> token(
            @RequestParam("grant_type") String grantType,
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam(required = false) String scope,
            @RequestParam(name = "client_id", required = false) String formClientId,
            @RequestParam(name = "client_secret", required = false) String formClientSecret,
            @Parameter(hidden = true) @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization,
            @RequestHeader(value = "session-id", required = false) String sessionId) {

        if (!isValidClient(authorization, formClientId, formClientSecret)) {
            return error(HttpStatus.UNAUTHORIZED, "invalid_client", "Client authentication failed");
        }
        if (!"password".equals(grantType)) {
            return error(HttpStatus.BAD_REQUEST, "unsupported_grant_type", "Only the password grant is supported");
        }

        List<String> scopes = (scope == null || scope.isBlank())
                ? SUPPORTED_SCOPES
                : Arrays.stream(scope.trim().split("\\s+")).distinct().toList();
        if (!SUPPORTED_SCOPES.containsAll(scopes)) {
            return error(HttpStatus.BAD_REQUEST, "invalid_scope", "Supported scopes: " + SUPPORTED_SCOPES);
        }

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken.unauthenticated(username, password));
        } catch (AuthenticationException e) {
            log.info("Authentication failed for user {}: {}", username, e.getMessage());
            return error(HttpStatus.BAD_REQUEST, "invalid_grant", "Bad credentials");
        }

        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .subject(authentication.getName())
                .audience(List.of(resourceId))
                .issuedAt(now)
                .expiresAt(now.plusSeconds(tokenExpiryTime))
                .claim("client_id", clientId)
                .claim("scope", String.join(" ", scopes))
                .build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        String session = (sessionId == null || sessionId.isBlank()) ? UUID.randomUUID().toString() : sessionId;
        userTokenSessionService.saveUserTokenSessionMapping(
                new UserTokenSession(authentication.getName(), token, session, tokenExpiryTime));

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("access_token", token);
        body.put("token_type", "bearer");
        body.put("expires_in", tokenExpiryTime);
        body.put("scope", String.join(" ", scopes));
        body.put("session_id", session);
        return ResponseEntity.ok()
                .header(HttpHeaders.CACHE_CONTROL, "no-store")
                .body(body);
    }

    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Return the authenticated user extracted from the bearer token")
    public Map<String, Object> me(@AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("username", jwt.getSubject());
        body.put("scope", jwt.getClaimAsString("scope"));
        body.put("client_id", jwt.getClaimAsString("client_id"));
        body.put("issued_at", jwt.getIssuedAt());
        body.put("expires_at", jwt.getExpiresAt());
        return body;
    }

    private boolean isValidClient(String authorization, String formClientId, String formClientSecret) {
        String id = formClientId;
        String secret = formClientSecret;
        if (authorization != null && authorization.regionMatches(true, 0, "Basic ", 0, 6)) {
            try {
                String decoded = new String(Base64.getDecoder().decode(authorization.substring(6).trim()),
                        StandardCharsets.UTF_8);
                int idx = decoded.indexOf(':');
                if (idx < 0) {
                    return false;
                }
                id = decoded.substring(0, idx);
                secret = decoded.substring(idx + 1);
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
        return id != null && secret != null
                && MessageDigest.isEqual(clientId.getBytes(StandardCharsets.UTF_8), id.getBytes(StandardCharsets.UTF_8))
                && MessageDigest.isEqual(clientSecret.getBytes(StandardCharsets.UTF_8),
                secret.getBytes(StandardCharsets.UTF_8));
    }

    private static ResponseEntity<Map<String, Object>> error(HttpStatus status, String error, String description) {
        return ResponseEntity.status(status).body(Map.of("error", error, "error_description", description));
    }
}
