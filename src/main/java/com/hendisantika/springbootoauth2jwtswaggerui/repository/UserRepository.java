package com.hendisantika.springbootoauth2jwtswaggerui.repository;

import com.hendisantika.springbootoauth2jwtswaggerui.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-oauth2-jwt-swagger-ui
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 30/09/20
 * Time: 07.20
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * @param username
     * @return @{@link User}
     */
    User findOneByUsername(String username);
}
