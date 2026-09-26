package com.hendisantika.springbootoauth2jwtswaggerui.model;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-oauth2-jwt-swagger-ui
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 30/09/20
 * Time: 07.18
 */
@Entity
@Table(name = "users")
public class User implements UserDetails {

    static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, updatable = false)
    @Schema(description = "The database generated user, token and session mapping ID.")
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    @Schema(description = "user name")
    private String username;

    @Column(name = "password", nullable = false)
    @Schema(description = "User password")
    private String password;

    @Column(name = "enabled", nullable = false)
    @Schema(description = "Indicates whether the user is enabled or disabled. A disabled user cannot be " +
            "authenticated.")
    private boolean enabled;

    @Column(name = "created_time", insertable = true, updatable = false)
    @Schema(description = "The database generated user, token and session mapping created time.")
    private LocalDateTime createdTime;

    @Column(name = "updated_time", insertable = false, updatable = true)
    @Schema(description = "The database generated user, token and session mapping updated time.")
    private LocalDateTime updatedTime;

    @PrePersist
    protected void onCreate() {
        createdTime = LocalDateTime.now();
        updatedTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedTime = LocalDateTime.now();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }
}
