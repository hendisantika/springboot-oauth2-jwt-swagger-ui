package com.hendisantika.springbootoauth2jwtswaggerui.model;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false)
    @ApiModelProperty(notes = "The database generated user, token and session mapping ID.")
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    @ApiModelProperty(notes = "user name.")
    private String username;

    @Column(name = "token", nullable = false, unique = true, length = 500)
    @ApiModelProperty(notes = "Authorization token.")
    private String token;

    @Column(name = "session_id", nullable = false, unique = true)
    @ApiModelProperty(notes = "Session-id received in request header.")
    private String sessionId;

    @Column(name = "expiry_time", nullable = false)
    @ApiModelProperty(notes = "Authorization token expiry time.")
    private Long expiryTime;

    @Column(name = "created_time", insertable = true, updatable = false)
    @ApiModelProperty(notes = "The database generated user, token and session mapping created time.")
    private LocalDateTime createdTime;

    @Column(name = "updated_time", insertable = false, updatable = true)
    @ApiModelProperty(notes = "The database generated user, token and session mapping updated time.")
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
