package com.nhnacademy.messenger.common.message.header;

import com.nhnacademy.messenger.common.message.data.MessageData;
import com.nhnacademy.messenger.common.message.data.auth.LoginRequest;
import com.nhnacademy.messenger.common.message.data.auth.LoginResponse;
import com.nhnacademy.messenger.common.message.data.auth.LogoutResponse;
import com.nhnacademy.messenger.common.message.data.chat.ChatMessageRequest;
import com.nhnacademy.messenger.common.message.data.chat.ChatMessageResponse;
import com.nhnacademy.messenger.common.message.data.chat.PrivateChatMessageRequest;
import com.nhnacademy.messenger.common.message.data.chat.PrivateChatMessageResponse;
import com.nhnacademy.messenger.common.message.data.error.ErrorResponse;
import com.nhnacademy.messenger.common.message.data.room.ChatRoomCreateRequest;
import com.nhnacademy.messenger.common.message.data.room.ChatRoomCreateResponse;
import com.nhnacademy.messenger.common.message.data.room.ChatRoomEnterRequest;
import com.nhnacademy.messenger.common.message.data.room.ChatRoomEnterResponse;
import com.nhnacademy.messenger.common.message.data.room.ChatRoomExitRequest;
import com.nhnacademy.messenger.common.message.data.room.ChatRoomExitResponse;
import com.nhnacademy.messenger.common.message.data.chat.ChatMessageHistoryRequest;
import com.nhnacademy.messenger.common.message.data.chat.ChatMessageHistoryResponse;
import com.nhnacademy.messenger.common.message.data.room.ChatRoomListResponse;
import com.nhnacademy.messenger.common.message.data.user.UserListResponse;
import lombok.Getter;

@Getter
public enum MessageType {

    // auth
    LOGIN(LoginRequest.class),
    LOGIN_SUCCESS(LoginResponse.class),
    LOGOUT(null),
    LOGOUT_SUCCESS(LogoutResponse.class),

    // user
    USER_LIST(null),
    USER_LIST_SUCCESS(UserListResponse.class),

    // chat
    CHAT_MESSAGE(ChatMessageRequest.class),
    CHAT_MESSAGE_SUCCESS(ChatMessageResponse.class),
    PRIVATE_MESSAGE(PrivateChatMessageRequest.class),
    PRIVATE_MESSAGE_SUCCESS(PrivateChatMessageResponse.class),

    // room
    CHAT_ROOM_CREATE(ChatRoomCreateRequest.class),
    CHAT_ROOM_CREATE_SUCCESS(ChatRoomCreateResponse.class),
    CHAT_ROOM_LIST(null),
    CHAT_ROOM_LIST_SUCCESS(ChatRoomListResponse.class),
    CHAT_ROOM_ENTER(ChatRoomEnterRequest.class),
    CHAT_ROOM_ENTER_SUCCESS(ChatRoomEnterResponse.class),
    CHAT_ROOM_EXIT(ChatRoomExitRequest.class),
    CHAT_ROOM_EXIT_SUCCESS(ChatRoomExitResponse.class),
    CHAT_MESSAGE_HISTORY(ChatMessageHistoryRequest.class),
    CHAT_MESSAGE_HISTORY_SUCCESS(ChatMessageHistoryResponse.class),

    // push
    PUSH_NEW_MESSAGE(null),
    PUSH_ROOM_ENTER(null),
    PUSH_ROOM_EXIT(null),

    // file
    FILE_TRANSFER(null),
    FILE_TRANSFER_SUCCESS(null),

    // error
    ERROR(ErrorResponse.class);

    private final Class<? extends MessageData> dataClass;

    MessageType(Class<? extends MessageData> dataClass) {
        this.dataClass = dataClass;
    }
}
