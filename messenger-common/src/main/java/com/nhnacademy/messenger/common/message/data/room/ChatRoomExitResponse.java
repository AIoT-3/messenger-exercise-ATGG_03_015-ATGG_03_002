package com.nhnacademy.messenger.common.message.data.room;
import com.nhnacademy.messenger.common.message.data.MessageData;

public record ChatRoomExitResponse(
        Long roomId,
        String message
) implements MessageData {
}
