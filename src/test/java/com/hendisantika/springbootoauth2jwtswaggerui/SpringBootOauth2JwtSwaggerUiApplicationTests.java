package com.hendisantika.springbootoauth2jwtswaggerui;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SpringBootOauth2JwtSwaggerUiApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
    }

    @Test
    void passwordGrantIssuesJwtThatAuthenticatesProtectedEndpoint() throws Exception {
        MvcResult result = mockMvc.perform(post("/oauth/token")
                        .with(httpBasic("client", "secret"))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("grant_type", "password")
                        .param("username", "user1@example.com")
                        .param("password", "password"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token_type").value("bearer"))
                .andExpect(jsonPath("$.scope").value("read write"))
                .andReturn();
        String token = JsonPath.read(result.getResponse().getContentAsString(), "$.access_token");

        mockMvc.perform(get("/oauth/me").header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user1@example.com"));
    }

    @Test
    void clientCredentialsInFormBodyAreAccepted() throws Exception {
        mockMvc.perform(post("/oauth/token")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("grant_type", "password")
                        .param("username", "user2@example.com")
                        .param("password", "password")
                        .param("scope", "read")
                        .param("client_id", "client")
                        .param("client_secret", "secret"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.scope").value("read"));
    }

    @Test
    void badUserCredentialsAreRejected() throws Exception {
        mockMvc.perform(post("/oauth/token")
                        .with(httpBasic("client", "secret"))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("grant_type", "password")
                        .param("username", "user1@example.com")
                        .param("password", "wrong"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("invalid_grant"));
    }

    @Test
    void badClientCredentialsAreRejected() throws Exception {
        mockMvc.perform(post("/oauth/token")
                        .with(httpBasic("client", "nope"))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("grant_type", "password")
                        .param("username", "user1@example.com")
                        .param("password", "password"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("invalid_client"));
    }

    @Test
    void protectedEndpointRequiresToken() throws Exception {
        mockMvc.perform(get("/oauth/me")).andExpect(status().isUnauthorized());
        mockMvc.perform(get("/oauth/me").header(HttpHeaders.AUTHORIZATION, "Bearer not-a-jwt"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void swaggerUiAndApiDocsArePublic() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.components.securitySchemes.oauth2schema.flows.password.tokenUrl")
                        .value("http://localhost:8080/api/oauth/token"));
        mockMvc.perform(get("/swagger-ui/index.html")).andExpect(status().isOk());
    }
}
