package com.nhnacademy.messenger.common.message.data.room;
import com.nhnacademy.messenger.common.message.data.MessageData;

import java.util.List;

public record ChatRoomListResponse(
        List<RoomInfo> rooms
) implements MessageData {
    public record RoomInfo(
            Long roomId,
            String roomName,
            Integer userCount
    ) {}
}
