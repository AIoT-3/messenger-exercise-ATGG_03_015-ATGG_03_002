package com.nhnacademy.messenger.common.protocol;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum MessageType {

    @JsonProperty("LOGIN") LOGIN,
    @JsonProperty("LOGIN-SUCCESS") LOGIN_SUCCESS,
    @JsonProperty("LOGOUT") LOGOUT,
    @JsonProperty("LOGOUT-SUCCESS") LOGOUT_SUCCESS,

    @JsonProperty("USER-LIST") USER_LIST,
    @JsonProperty("USER-LIST-SUCCESS") USER_LIST_SUCCESS,

    @JsonProperty("CHAT-MESSAGE") CHAT_MESSAGE,
    @JsonProperty("CHAT-MESSAGE-SUCCESS") CHAT_MESSAGE_SUCCESS,
    @JsonProperty("PRIVATE-MESSAGE") PRIVATE_MESSAGE,
    @JsonProperty("PRIVATE-MESSAGE-SUCCESS") PRIVATE_MESSAGE_SUCCESS,
    @JsonProperty("CHAT-ROOM-CREATE") CHAT_ROOM_CREATE,
    @JsonProperty("CHAT-ROOM-CREATE-SUCCESS") CHAT_ROOM_CREATE_SUCCESS,
    @JsonProperty("CHAT-ROOM-LIST") CHAT_ROOM_LIST,
    @JsonProperty("CHAT-ROOM-LIST-SUCCESS") CHAT_ROOM_LIST_SUCCESS,
    @JsonProperty("CHAT-ROOM-ENTER") CHAT_ROOM_ENTER,
    @JsonProperty("CHAT-ROOM-ENTER-SUCCESS") CHAT_ROOM_ENTER_SUCCESS,
    @JsonProperty("CHAT-ROOM-EXIT") CHAT_ROOM_EXIT,
    @JsonProperty("CHAT-ROOM-EXIT-SUCCESS") CHAT_ROOM_EXIT_SUCCESS,
    @JsonProperty("CHAT-MESSAGE-HISTORY") CHAT_MESSAGE_HISTORY,
    @JsonProperty("CHAT-MESSAGE-HISTORY-SUCCESS") CHAT_MESSAGE_HISTORY_SUCCESS,

    @JsonProperty("PUSH-NEW-MESSAGE") PUSH_NEW_MESSAGE,
    @JsonProperty("PUSH-ROOM-ENTER") PUSH_ROOM_ENTER,
    @JsonProperty("PUSH-ROOM-EXIT") PUSH_ROOM_EXIT,

    @JsonProperty("FILE-TRANSFER") FILE_TRANSFER,
    @JsonProperty("FILE-TRANSFER-SUCCESS") FILE_TRANSFER_SUCCESS,

    @JsonProperty("ERROR") ERROR,
}
