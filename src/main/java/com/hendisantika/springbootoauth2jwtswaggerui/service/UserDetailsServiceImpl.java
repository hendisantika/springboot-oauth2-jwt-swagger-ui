package com.hendisantika.springbootoauth2jwtswaggerui.service;

import com.hendisantika.springbootoauth2jwtswaggerui.model.User;
import com.hendisantika.springbootoauth2jwtswaggerui.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-oauth2-jwt-swagger-ui
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 30/09/20
 * Time: 07.21
 */
@Service("userDetailsService")
@Log4j2
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User userFromDataBase = userRepository.findOneByUsername(username);
        if (userFromDataBase == null) {
            log.info("User " + username + " was not found in the database");
            throw new UsernameNotFoundException("User " + username + " was not found in the database");
        }
        return userFromDataBase;

    }
}
