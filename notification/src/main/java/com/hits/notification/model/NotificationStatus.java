package com.hits.notification.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum NotificationStatus {
    NOT_SEND("NOT_SEND"),
    SEND("SEND"),
    READ("READ");

    private final String value;

    NotificationStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

