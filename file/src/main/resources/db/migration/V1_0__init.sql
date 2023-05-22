CREATE TABLE file
(
    id                 UUID NOT NULL,
    name               VARCHAR(255),
    created_date       TIMESTAMP WITHOUT TIME ZONE,
    last_download_date TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_file PRIMARY KEY (id)
);