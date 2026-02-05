package com.nhnacademy.messenger.client.domain.room.event;

public record PushRoomEnterEvent(
        Long roomId,
        String userId,
        String userName
) {
}
