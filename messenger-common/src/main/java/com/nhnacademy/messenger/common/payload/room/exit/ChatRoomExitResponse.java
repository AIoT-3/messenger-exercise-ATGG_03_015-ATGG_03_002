package com.nhnacademy.messenger.common.payload.room.exit;

public record ChatRoomExitResponse(
        Long roomId,
        String message
) {
}
