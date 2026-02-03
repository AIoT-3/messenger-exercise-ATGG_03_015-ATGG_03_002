package com.nhnacademy.messenger.common.message.data.room;

public record RoomInfo(
        Long roomId,
        String roomName,
        Integer userCount
) {
}
