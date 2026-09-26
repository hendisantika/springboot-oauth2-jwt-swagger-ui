package com.hendisantika.springbootoauth2jwtswaggerui.model;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-oauth2-jwt-swagger-ui
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 30/09/20
 * Time: 07.19
 */
@Entity
@Table(name = "tbl_user_token_session")
public class UserTokenSession {

    static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    @Schema(description = "The database generated user, token and session mapping ID.")
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    @Schema(description = "user name.")
    private String username;

    @Column(name = "token", nullable = false, unique = true, length = 2000)
    @Schema(description = "Authorization token.")
    private String token;

    @Column(name = "session_id", nullable = false, unique = true)
    @Schema(description = "Session-id received in request header.")
    private String sessionId;

    @Column(name = "expiry_time", nullable = false)
    @Schema(description = "Authorization token expiry time.")
    private Long expiryTime;

    @Column(name = "created_time", insertable = true, updatable = false)
    @Schema(description = "The database generated user, token and session mapping created time.")
    private LocalDateTime createdTime;

    @Column(name = "updated_time", insertable = false, updatable = true)
    @Schema(description = "The database generated user, token and session mapping updated time.")
    private LocalDateTime updatedTime;

    public UserTokenSession() {
    }

    public UserTokenSession(String username, String token, String sessionId, Long expiryTime) {
        this.username = username;
        this.token = token;
        this.sessionId = sessionId;
        this.expiryTime = expiryTime;
    }

    @PrePersist
    protected void onCreate() {
        createdTime = LocalDateTime.now();
        updatedTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedTime = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    public String getSessionId() {
        return sessionId;
    }

    public Long getExpiryTime() {
        return expiryTime;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserTokenSession that = (UserTokenSession) o;
        return Objects.equals(username, that.username) &&
                Objects.equals(token, that.token) &&
                Objects.equals(sessionId, that.sessionId) &&
                Objects.equals(expiryTime, that.expiryTime);
    }

    @Override
    public int hashCode() {

        return Objects.hash(username, token, sessionId, expiryTime);
    }

    @Override
    public String toString() {
        return "UserTokenSession{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", token='" + token + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", expiryTime=" + expiryTime +
                ", createdTime=" + createdTime +
                ", updatedTime=" + updatedTime +
                '}';
    }
}
