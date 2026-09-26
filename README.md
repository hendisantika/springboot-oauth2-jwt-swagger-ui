# springboot-oauth2-jwt-swagger-ui

[![Java CI with Maven](https://github.com/hendisantika/springboot-oauth2-jwt-swagger-ui/actions/workflows/maven.yml/badge.svg)](https://github.com/hendisantika/springboot-oauth2-jwt-swagger-ui/actions/workflows/maven.yml)

Spring Boot demo that issues **RS256-signed JWT access tokens** through an OAuth2 *password grant* endpoint,
protects the API as an **OAuth2 resource server**, and documents everything with **Swagger UI (OpenAPI 3)**.

## Tech stack

| Component        | Version / Library                                                |
|------------------|------------------------------------------------------------------|
| Java             | 25                                                               |
| Spring Boot      | 4.1.x                                                            |
| Security         | Spring Security 7 — `spring-boot-starter-oauth2-resource-server` |
| JWT              | Nimbus JOSE (`NimbusJwtEncoder` / `NimbusJwtDecoder`), RSA 2048  |
| API docs         | springdoc-openapi 3 (Swagger UI)                                 |
| Persistence      | Spring Data JPA + H2 (in-memory)                                 |
| Build            | Maven 3.9 (wrapper included)                                     |

## Requirements

- JDK 25 (e.g. Temurin, Corretto)
- No local Maven install needed — use `./mvnw`

## Build, test and run

```bash
./mvnw clean package      # compile + run tests
./mvnw spring-boot:run    # start on http://localhost:8080/api
```

## Seeded users

| Username            | Password   |
|---------------------|------------|
| `user1@example.com` | `password` |
| `user2@example.com` | `password` |
| `user3@example.com` | `password` |

OAuth2 client: `client` / `secret` — scopes `read`, `write`.

## Endpoints

All paths are under the context path `/api`.

| Method | Path                  | Auth                    | Description                                 |
|--------|-----------------------|-------------------------|---------------------------------------------|
| POST   | `/oauth/token`        | Client (Basic or form)  | Password grant, returns a JWT access token  |
| GET    | `/oauth/me`           | `Bearer <access_token>` | Returns the user/scopes from the token      |
| GET    | `/swagger-ui.html`    | public                  | Swagger UI                                  |
| GET    | `/v3/api-docs`        | public                  | OpenAPI 3 JSON                              |

### Get a token

```bash
curl -s -u client:secret \
  -d grant_type=password \
  -d username=user1@example.com \
  -d password=password \
  -d scope="read write" \
  http://localhost:8080/api/oauth/token
```

```json
{
  "access_token": "eyJhbGciOiJSUzI1NiJ9...",
  "token_type": "bearer",
  "expires_in": 3600,
  "scope": "read write",
  "session_id": "0b6d..."
}
```

An optional `session-id` request header is stored with the token in `tbl_user_token_session`
(a random one is generated when absent).

### Call a protected endpoint

```bash
TOKEN=$(curl -s -u client:secret -d grant_type=password -d username=user1@example.com -d password=password \
  http://localhost:8080/api/oauth/token | jq -r .access_token)

curl -s -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/oauth/me
```

### Swagger UI

Open <http://localhost:8080/api/swagger-ui.html>, click **Authorize**, and either:

- use the **oauth2schema (password)** flow with a seeded user (client id/secret are pre-filled), or
- paste an access token into **bearerAuth**.

## Configuration

Key properties in `src/main/resources/application.properties`:

| Property                       | Default                                 |
|--------------------------------|-----------------------------------------|
| `config.oauth2.privateKey`     | `classpath:jwt/private.pem` (PKCS#8)    |
| `config.oauth2.publicKey`      | `classpath:jwt/public.pem` (X.509)      |
| `config.oauth2.issuer`         | `http://localhost:8080/api`             |
| `config.oauth2.resource.id`    | `oauth2-resource` (JWT `aud` claim)     |
| `config.oauth2.tokenTimeout`   | `3600` seconds                          |
| `config.oauth2.clientID`       | `client`                                |
| `config.oauth2.clientSecret`   | `secret`                                |
| `config.oauth2.accessTokenUri` | `http://localhost:8080/api/oauth/token` |

The bundled key pair is for demo purposes only. Generate your own for anything real:

```bash
openssl genpkey -algorithm RSA -pkeyopt rsa_keygen_bits:2048 -out private.pem
openssl pkey -in private.pem -pubout -out public.pem
```

## CI

GitHub Actions (`.github/workflows/maven.yml`) builds and tests the project with Temurin JDK 25 on every push
and pull request to `master`.
