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

    public static final String FRIEND = "/friends";
    public static final String FRIEND_FRIENDSHIP = FRIEND + "/friends";
    public static final String FRIEND_FRIENDSHIP_GET_FRIENDS = FRIEND_FRIENDSHIP + "/friends";
    public static final String FRIEND_FRIENDSHIP_GET_FRIEND = FRIEND_FRIENDSHIP + "/friend/{id}";
    public static final String FRIEND_FRIENDSHIP_ADD = FRIEND_FRIENDSHIP + "/friend/add";
    public static final String FRIEND_FRIENDSHIP_DELETE = FRIEND_FRIENDSHIP + "/friend/{id}";
    public static final String FRIEND_FRIENDSHIP_FIND = FRIEND_FRIENDSHIP + "/friend/find";

    public static final String FRIEND_BLOCKING = FRIEND + "/blocking";
    public static final String FRIEND_BLOCKING_GET_BLOCKING = FRIEND_BLOCKING + "/blocking";
    public static final String FRIEND_BLOCKING_GET_BLOCK = FRIEND_BLOCKING + "/blocking/{id}";
    public static final String FRIEND_BLOCKING_ADD = FRIEND_BLOCKING + "/blocking/add";
    public static final String FRIEND_BLOCKING_DELETE = FRIEND_BLOCKING + "/blocking/{id}";
    public static final String FRIEND_BLOCKING_FIND = FRIEND_BLOCKING + "/blocking/find";
    public static final String FRIEND_BLOCKING_CHECK = FRIEND_BLOCKING + "/blocking/check/{id}";

    public static final String FRIEND_SYNC = "/friends/common" + FRIEND_NAME_SYNC;
    public static final String FRIEND_BLOCK_CHECK = "/friends/common" + "/blocking";

    public static final String NOTIFICATION = "/notification";

    public static final String NOTIFICATION_GET = NOTIFICATION;
    public static final String NOTIFICATION_NOT_READ = NOTIFICATION + "/not-read";
    public static final String NOTIFICATION_READ = NOTIFICATION + "/read";
}
