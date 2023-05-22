CREATE TABLE users
(
    id                UUID NOT NULL,
    registration_date TIMESTAMP WITHOUT TIME ZONE,
    login             VARCHAR(255),
    email             VARCHAR(255),
    password          VARCHAR(255),
    name              VARCHAR(255),
    surname           VARCHAR(255),
    patronymic        VARCHAR(255),
    birth_date        TIMESTAMP WITHOUT TIME ZONE,
    phone             VARCHAR(255),
    city              VARCHAR(255),
    avatar_id         UUID,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE users
    ADD CONSTRAINT uc_users_login UNIQUE (login);