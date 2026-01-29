package com.nhnacademy.messenger.common.message.data.room;
import com.nhnacademy.messenger.common.message.data.MessageData;

public record ChatRoomCreateResponse(
        Long roomId,
        String roomName
) implements MessageData {
}
