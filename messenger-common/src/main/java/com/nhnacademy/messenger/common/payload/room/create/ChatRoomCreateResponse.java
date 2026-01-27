package com.nhnacademy.messenger.common.payload.room.create;

public record ChatRoomCreateResponse(
        Long roomId,
        String roomName
) {
}
