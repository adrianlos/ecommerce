DROP TABLE IF EXISTS users;

CREATE TABLE users (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    login VARCHAR(255) NOT NULL,

    password BINARY(256) NOT NULL,

    role VARCHAR(8) NOT NULL,

    avatar_url VARCHAR(511) NOT NULL,

    contact_preference VARCHAR(5) NOT NULL,

    country VARCHAR(64) NOT NULL,

    city VARCHAR(64) NOT NULL,

    street VARCHAR(64) NOT NULL,

    zip_code VARCHAR(16) NOT NULL
);
