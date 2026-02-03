package com.nhnacademy.messenger.common.message.data.push;

import com.nhnacademy.messenger.common.message.data.MessageData;

public record PushRoomExit(
        Long roomId,
        String userId
) implements MessageData {
}
