package com.nhnacademy.messenger.common.message.data.room;
import com.nhnacademy.messenger.common.message.data.MessageData;

public record CreateRoomRequest(
        String roomName
) implements MessageData {
}
