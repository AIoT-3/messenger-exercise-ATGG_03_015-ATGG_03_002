package com.nhnacademy.messenger.common.message.data.room.exit;

public record ChatRoomExitResponse(
        Long roomId,
        String message
) {
}
