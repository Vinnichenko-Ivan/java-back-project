CREATE TABLE notification
(
    id                  UUID NOT NULL,
    notification_type   VARCHAR(255),
    text                VARCHAR(255),
    user_id             UUID,
    notification_status VARCHAR(255),
    created_date        TIMESTAMP WITHOUT TIME ZONE,
    read_date           TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_notification PRIMARY KEY (id)
);