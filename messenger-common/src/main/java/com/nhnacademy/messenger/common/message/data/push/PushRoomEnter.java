package com.nhnacademy.messenger.common.message.data.push;

import com.nhnacademy.messenger.common.message.data.MessageData;

public record PushRoomEnter(
        Long roomId,
        String userId,
        String userName
) implements MessageData {
}
