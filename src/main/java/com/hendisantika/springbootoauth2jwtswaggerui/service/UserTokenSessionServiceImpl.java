package com.hendisantika.springbootoauth2jwtswaggerui.service;

import com.hendisantika.springbootoauth2jwtswaggerui.model.UserTokenSession;
import com.hendisantika.springbootoauth2jwtswaggerui.repository.UserTokenSessionRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

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

    @Override
    public ValidMappingResponse isValidUserTokenSessionMapping(UserTokenSession userTokenSession) throws UsernameNotFoundException {

        String username = userTokenSession.getUsername();
        UserTokenSession userTokenSessionFromDB = userTokenSessionRepository.findOneByUsername(username);

        if (Objects.isNull(userTokenSessionFromDB)) {

            log.error("User " + username + " mapping with token is not found in the database.");
            throw new UsernameNotFoundException("User " + username + "  mapping with token is not found in the " +
                    "database.");
        }

        /**
         * TODO Time zone of data base and client may be different.
         */
        LocalDateTime currentSystemTime = LocalDateTime.now();
        ZonedDateTime currentZonedDateTime = currentSystemTime.atZone(ZoneId.systemDefault());
        long currentTimeInMillis = currentZonedDateTime.toInstant().toEpochMilli();

        ZonedDateTime dataBaseZonedDateTime = userTokenSessionFromDB.getCreatedTime().atZone(ZoneId.systemDefault());

        /**
         * tokenTimeInMillis = created_time in millis + expiry time (seconds) * 1000.
         */
        long tokenTimeInMillis =
                dataBaseZonedDateTime.toInstant().toEpochMilli() + (userTokenSessionFromDB.getExpiryTime() * 1000);

        if (currentTimeInMillis >= tokenTimeInMillis) {

            log.info("User " + username + " token has expired. Please generate new token. Deleting the expired token " +
                    "mapping.");
            userTokenSessionRepository.delete(userTokenSessionFromDB);
            throw new UsernameNotFoundException("User " + username + " token has expired. Please generate new token.");

        } else if (!userTokenSession.equals(userTokenSessionFromDB)) {

            if (!userTokenSessionFromDB.getToken().equals(userTokenSession.getToken())) {
                log.info("User " + userTokenSession.getUsername() + " has invalid user and token mapping. Please " +
                        "generate new token.");

            } else {
                log.info("User " + userTokenSession.getUsername() + " has invalid user and session-id mapping. Please" +
                        " generate new token.");
            }

            log.info("So, Deleting the invalid mapping.");
            userTokenSessionRepository.delete(userTokenSessionFromDB);
            throw new UsernameNotFoundException("User " + username + " has invalid user, session-id and token mapping" +
                    ". Please generate new token.");

        } else {

            log.info("User " + username + " has valid token.");
            ValidMappingResponse validMappingResponse = new ValidMappingResponse(true, userTokenSessionFromDB);
            return validMappingResponse;
        }

    }

    @Override
    public UserTokenSession saveUserTokenSessionMapping(UserTokenSession userTokenSession) {

        UserTokenSession userTokenSessionFromDB =
                userTokenSessionRepository.findOneByUsername(userTokenSession.getUsername());

        /**
         * 1. If User is making the login call again with the same session-id and token. Then delete the old mapping
         * and return the new inserted mapping.
         * 2. If same user is making login call with the new token or session-id. Then delete the old mapping and
         * return the new inserted mapping
         */
        if (Objects.nonNull(userTokenSessionFromDB)) {

            if (userTokenSessionFromDB.equals(userTokenSession)) {
                log.info("User " + userTokenSession.getUsername() + " making login call again with same token and " +
                        "session-id.");

            } else if (!userTokenSessionFromDB.getToken().equals(userTokenSession.getToken())) {
                log.info("User " + userTokenSession.getUsername() + " making login call with new token");

            } else {
                log.info("User " + userTokenSession.getUsername() + " making login call with different session-id");

            }
            log.info("So, Deleting older mapping from tbl_user_token_session." + userTokenSessionFromDB);
            userTokenSessionRepository.delete(userTokenSessionFromDB);

        }

        return userTokenSessionRepository.save(userTokenSession);
    }
}
