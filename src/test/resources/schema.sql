DROP TABLE IF EXISTS users;

CREATE TABLE users (
     id BIGINT AUTO_INCREMENT PRIMARY KEY,
     username VARCHAR(255) NOT NULL UNIQUE,
     password VARCHAR(255) NOT NULL,
     nickname VARCHAR(255) NOT NULL,
     authority_name VARCHAR(255) NOT NULL CHECK (authority_name IN ('ROLE_USER')),
     refresh_token VARCHAR(255)
);
