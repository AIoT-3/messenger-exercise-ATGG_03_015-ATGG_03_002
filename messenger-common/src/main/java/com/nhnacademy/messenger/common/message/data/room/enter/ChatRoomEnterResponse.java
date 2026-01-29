package com.nhnacademy.messenger.common.message.data.room.enter;

import java.util.List;

public record ChatRoomEnterResponse(
        Long roomId,
        List<String> users
) {
}
