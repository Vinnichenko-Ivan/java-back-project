package com.hits.common;

public class Paths {
    public static final String USER_SERVICE_NAME = "user";
    public static final String API_SECURE_HEADER = "api-key";
    public static final String USER_CHECK_USER = "/check/{id}";
    public static final String USER_USER_NAME = "/user/{id}";
    public static final String USER_SERVICE_PATH = "http://localhost:8081/users/common";
    public static final String CHAT = "/chat/chat";
    public static final String CHAT_SEND_PRIVATE_MESSAGE = CHAT + "/private/message";
    public static final String CHAT_CREATE = CHAT + "/";
    public static final String CHAT_EDIT = CHAT + "/";
    public static final String CHAT_SEND_MESSAGE = CHAT + "/message";
    public static final String CHAT_INFO = CHAT + "/{id}";
    public static final String CHAT_FIND = CHAT + "/find";
    public static final String CHAT_GET_MESSAGES = CHAT + "/{id}/message";
    public static final String CHAT_FIND_MESSAGES = CHAT + "/message";
}
