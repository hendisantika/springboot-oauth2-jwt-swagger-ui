DROP TABLE IF EXISTS tbl_user_token_session;
DROP TABLE IF EXISTS users;
CREATE TABLE users
(
    user_id      BIGINT PRIMARY KEY AUTO_INCREMENT,
    username     VARCHAR(128) NOT NULL UNIQUE,
    password     VARCHAR(256) NOT NULL,
    enabled      BOOLEAN      NOT NULL,
    created_time TIMESTAMP,
    updated_time TIMESTAMP
);
CREATE TABLE tbl_user_token_session
(
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    username     VARCHAR(255)  NOT NULL UNIQUE,
    token        VARCHAR(2000) NOT NULL UNIQUE,
    session_id   VARCHAR(255)  NOT NULL UNIQUE,
    expiry_time  BIGINT        NOT NULL,
    created_time TIMESTAMP,
    updated_time TIMESTAMP
);
