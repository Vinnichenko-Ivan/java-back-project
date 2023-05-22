CREATE TABLE blocking
(
    id                UUID NOT NULL,
    date_start        TIMESTAMP WITHOUT TIME ZONE,
    date_end          TIMESTAMP WITHOUT TIME ZONE,
    main_user         UUID,
    target_user       UUID,
    name_target       VARCHAR(255),
    surname_target    VARCHAR(255),
    patronymic_target VARCHAR(255),
    CONSTRAINT pk_blocking PRIMARY KEY (id)
);

ALTER TABLE blocking
    ADD CONSTRAINT uc_4bf8792d51b0a74d40c4743e1 UNIQUE (main_user, target_user);

CREATE TABLE friendship
(
    id                UUID NOT NULL,
    date_start        TIMESTAMP WITHOUT TIME ZONE,
    date_end          TIMESTAMP WITHOUT TIME ZONE,
    main_user         UUID,
    target_user       UUID,
    name_target       VARCHAR(255),
    surname_target    VARCHAR(255),
    patronymic_target VARCHAR(255),
    CONSTRAINT pk_friendship PRIMARY KEY (id)
);

ALTER TABLE friendship
    ADD CONSTRAINT uc_ee0e07283eb07edc6c29199ae UNIQUE (main_user, target_user);