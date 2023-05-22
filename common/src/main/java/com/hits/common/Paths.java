package com.hits.common;

public class Paths {
    public static final String USER_SERVICE_NAME = "user";
    public static final String FRIEND_SERVICE_NAME = "friends";
    public static final String API_SECURE_HEADER = "api-key";
    public static final String USER_CHECK_USER = "/check/{id}";
    public static final String USER_USER_NAME = "/user/{id}";
    public static final String USER_SERVICE_PATH = "http://localhost:8081/users/common";

    public static final String FRIEND_NAME_SYNC = "/synchronise";
    public static final String FRIEND_CHECK_BLOCK = "/blocking";
    public static final String FRIEND_SERVICE_PATH = "http://localhost:8082/friends/common";

    public static final String USERS = "/users";
    public static final String USERS_SIGN_UP = USERS + "/sign-up";
    public static final String USERS_SIGN_IN = USERS + "/sign-in";
    public static final String USERS_GET_USERS = USERS + "/users";
    public static final String USERS_GET_USER = USERS + "/user/{login}";
    public static final String USERS_GET_ME = USERS + "/me";
    public static final String USERS_PUT_ME = USERS + "/me";
    public static final String USERS_COMMON = USERS + "/common";

    public static final String USERS_COMMON_CHECK_USER = USERS_COMMON + "/check/{id}";

    public static final String USERS_COMMON_GET_USER_NAME = USERS_COMMON + "/user/{id}";

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
