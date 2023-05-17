package com.hits.common.enums;


import com.fasterxml.jackson.annotation.JsonValue;

public enum NotificationType {
    FRIEND_ADD("FRIEND_ADD"),
    FRIEND_DELETE("FRIEND_DELETE"),
    NEW_MESSAGE("NEW_MESSAGE"),
    BLOCK_ADD("BLOCK_ADD"),
    BLOCK_DELETE("BLOCK_DELETE"),
    LOGIN("LOGIN");

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }

    NotificationType(String value) {
        this.value = value;
    }

}
