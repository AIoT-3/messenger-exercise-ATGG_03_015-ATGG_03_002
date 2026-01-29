package com.nhnacademy.messenger.common.message.data.room;
import com.nhnacademy.messenger.common.message.data.MessageData;

public record ChatRoomEnterRequest(
        Long roomId
) implements MessageData {
}
