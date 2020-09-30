package com.hendisantika.springbootoauth2jwtswaggerui.service;

import com.hendisantika.springbootoauth2jwtswaggerui.model.UserTokenSession;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-oauth2-jwt-swagger-ui
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 30/09/20
 * Time: 07.22
 */
public interface UserTokenSessionService {

    /**
     * Check whether there is mapping between oauth token, username and session-id.
     * And the token is not yet expired.
     *
     * @param userTokenSession
     * @return ValidMappingResponse if valid mapping else throw {@link UsernameNotFoundException}
     */
    ValidMappingResponse isValidUserTokenSessionMapping(UserTokenSession userTokenSession) throws UsernameNotFoundException;

    /**
     * @param userTokenSession
     * @return token session record from data base.
     */
    UserTokenSession saveUserTokenSessionMapping(UserTokenSession userTokenSession);


    /**
     * Class to store isValidUserTokenSessionMapping() response.
     */
    class ValidMappingResponse {

        private boolean valid;
        private UserTokenSession userTokenSession;

        public ValidMappingResponse() {
        }

        public ValidMappingResponse(boolean valid, UserTokenSession userTokenSession) {
            this.valid = valid;
            this.userTokenSession = userTokenSession;
        }

        public boolean isValid() {
            return valid;
        }

        public void setValid(boolean valid) {
            this.valid = valid;
        }

        public UserTokenSession getUserTokenSession() {
            return userTokenSession;
        }

        public void setUserTokenSession(UserTokenSession userTokenSession) {
            this.userTokenSession = userTokenSession;
        }
    }

}
