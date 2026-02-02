package com.nhnacademy.messenger.common.message.header;

import com.nhnacademy.messenger.common.message.data.MessageData;
import com.nhnacademy.messenger.common.message.data.auth.LoginRequest;
import com.nhnacademy.messenger.common.message.data.auth.LoginResponse;
import com.nhnacademy.messenger.common.message.data.auth.LogoutResponse;
import com.nhnacademy.messenger.common.message.data.chat.ChatRequest;
import com.nhnacademy.messenger.common.message.data.chat.ChatResponse;
import com.nhnacademy.messenger.common.message.data.chat.PrivateChatRequest;
import com.nhnacademy.messenger.common.message.data.chat.PrivateChatResponse;
import com.nhnacademy.messenger.common.message.data.error.ErrorResponse;
import com.nhnacademy.messenger.common.message.data.file.FileTransferRequest;
import com.nhnacademy.messenger.common.message.data.file.FileTransferResponse;
import com.nhnacademy.messenger.common.message.data.room.CreateRoomRequest;
import com.nhnacademy.messenger.common.message.data.room.CreateRoomResponse;
import com.nhnacademy.messenger.common.message.data.room.EnterRoomRequest;
import com.nhnacademy.messenger.common.message.data.room.EnterRoomResponse;
import com.nhnacademy.messenger.common.message.data.room.ExitRoomRequest;
import com.nhnacademy.messenger.common.message.data.room.ExitRoomResponse;
import com.nhnacademy.messenger.common.message.data.chat.ChatHistoryRequest;
import com.nhnacademy.messenger.common.message.data.chat.ChatHistoryResponse;
import com.nhnacademy.messenger.common.message.data.room.ListRoomResponse;
import com.nhnacademy.messenger.common.message.data.user.UserListResponse;
import lombok.Getter;

@Getter
public enum MessageType {
    // api 스펙에서는 -로 구분하지만, 중요하진 않은 것 같아 _ 그대로 사용함

    // auth
    LOGIN(LoginRequest.class),
    LOGIN_SUCCESS(LoginResponse.class),
    LOGOUT(null),
    LOGOUT_SUCCESS(LogoutResponse.class),

    // user
    USER_LIST(null),
    USER_LIST_SUCCESS(UserListResponse.class),

    // chat
    CHAT_MESSAGE(ChatRequest.class),
    CHAT_MESSAGE_SUCCESS(ChatResponse.class),
    PRIVATE_MESSAGE(PrivateChatRequest.class),
    PRIVATE_MESSAGE_SUCCESS(PrivateChatResponse.class),
    CHAT_MESSAGE_HISTORY(ChatHistoryRequest.class),
    CHAT_MESSAGE_HISTORY_SUCCESS(ChatHistoryResponse.class),

    // room
    CHAT_ROOM_CREATE(CreateRoomRequest.class),
    CHAT_ROOM_CREATE_SUCCESS(CreateRoomResponse.class),
    CHAT_ROOM_LIST(null),
    CHAT_ROOM_LIST_SUCCESS(ListRoomResponse.class),
    CHAT_ROOM_ENTER(EnterRoomRequest.class),
    CHAT_ROOM_ENTER_SUCCESS(EnterRoomResponse.class),
    CHAT_ROOM_EXIT(ExitRoomRequest.class),
    CHAT_ROOM_EXIT_SUCCESS(ExitRoomResponse.class),

    // push
    PUSH_NEW_MESSAGE(null),
    PUSH_ROOM_ENTER(null),
    PUSH_ROOM_EXIT(null),

    // file
    FILE_TRANSFER(FileTransferRequest.class),
    FILE_TRANSFER_SUCCESS(FileTransferResponse.class),

    // error
    ERROR(ErrorResponse.class);

    private final Class<? extends MessageData> dataClass;

    MessageType(Class<? extends MessageData> dataClass) {
        this.dataClass = dataClass;
    }
}
