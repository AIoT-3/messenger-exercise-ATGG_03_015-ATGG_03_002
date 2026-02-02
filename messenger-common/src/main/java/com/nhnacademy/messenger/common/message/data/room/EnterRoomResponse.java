package com.nhnacademy.messenger.common.message.data.room;
import com.nhnacademy.messenger.common.message.data.MessageData;

import java.util.List;

public record EnterRoomResponse(
        Long roomId,
        List<String> users
) implements MessageData {
}
