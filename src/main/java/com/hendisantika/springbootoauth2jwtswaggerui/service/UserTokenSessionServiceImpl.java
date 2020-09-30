package com.hendisantika.springbootoauth2jwtswaggerui.service;

import com.hendisantika.springbootoauth2jwtswaggerui.repository.UserTokenSessionRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-oauth2-jwt-swagger-ui
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 30/09/20
 * Time: 07.23
 */
@Service
@Log4j2
public class UserTokenSessionServiceImpl implements UserTokenSessionService {

    @Autowired
    private UserTokenSessionRepository userTokenSessionRepository;


}
