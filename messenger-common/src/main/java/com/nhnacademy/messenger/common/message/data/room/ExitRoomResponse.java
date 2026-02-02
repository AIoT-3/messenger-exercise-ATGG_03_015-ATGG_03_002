package com.nhnacademy.messenger.common.message.data.room;
import com.nhnacademy.messenger.common.message.data.MessageData;

public record ExitRoomResponse(
        Long roomId,
        String message
) implements MessageData {
}
