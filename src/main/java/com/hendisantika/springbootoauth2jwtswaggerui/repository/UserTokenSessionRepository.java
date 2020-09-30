package com.hendisantika.springbootoauth2jwtswaggerui.repository;

import com.hendisantika.springbootoauth2jwtswaggerui.model.UserTokenSession;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-oauth2-jwt-swagger-ui
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 30/09/20
 * Time: 07.21
 */
@Repository
public interface UserTokenSessionRepository extends CrudRepository<UserTokenSession, Long> {

    /**
     * Find {@link UserTokenSession} for the given username.
     *
     * @param username
     * @return @{@link UserTokenSession}
     */
    UserTokenSession findOneByUsername(String username);
}
