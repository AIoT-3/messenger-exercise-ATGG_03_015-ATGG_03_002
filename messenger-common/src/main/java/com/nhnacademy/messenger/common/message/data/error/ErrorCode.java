package com.nhnacademy.messenger.common.message.data.error;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ErrorCode {
    // Auth
    AUTH_INVALID_CREDENTIALS("AUTH.INVALID_CREDENTIALS"),
    AUTH_INVALID_SESSION("AUTH.INVALID_SESSION"),
    AUTH_UNAUTHORIZED("AUTH.UNAUTHORIZED"),

    // User
    USER_NOT_FOUND("USER.NOT_FOUND"),
    USER_ALREADY_EXISTS("USER.ALREADY_EXISTS"),

    // Room
    ROOM_NOT_FOUND("ROOM.NOT_FOUND"),
    ROOM_ALREADY_EXISTS("ROOM.ALREADY_EXISTS"),

    // File
    FILE_SIZE_EXCEEDED("FILE.SIZE_EXCEEDED"),

    // Request
    REQUEST_INVALID_MESSAGE("REQUEST.INVALID_MESSAGE"),
    MESSAGE_TYPE_UNSUPPORTED("REQUEST.MESSAGE_TYPE_UNSUPPORTED"),

    // Server
    INTERNAL_SERVER_ERROR("SERVER.INTERNAL_SERVER_ERROR");

    private final String value;

    ErrorCode(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
