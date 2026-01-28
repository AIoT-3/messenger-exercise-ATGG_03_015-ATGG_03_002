package com.nhnacademy.messenger.common.data.room.list;

import java.util.List;

public record ChatRoomListResponse(
        List<RoomInfo> rooms
) {
    public record RoomInfo(
            Long roomId,
            String roomName,
            Integer userCount
    ) {}
}
