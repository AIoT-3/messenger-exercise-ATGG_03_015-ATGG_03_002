package com.nhnacademy.messenger.client.domain.room.event;

public record CreateRoomSuccessEvent (
        Long roomId,
        String roomName
) {
}
