package com.nhnacademy.messenger.client.domain.room.event;

public record PushRoomExitEvent(
        Long roomId,
        String userId
) {
}
