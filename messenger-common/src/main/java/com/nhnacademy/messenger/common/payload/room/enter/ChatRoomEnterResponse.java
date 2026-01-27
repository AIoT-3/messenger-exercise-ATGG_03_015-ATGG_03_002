package com.nhnacademy.messenger.common.payload.room.enter;

import java.util.List;

public record ChatRoomEnterResponse(
        Long roomId,
        List<String> users
) {
}
