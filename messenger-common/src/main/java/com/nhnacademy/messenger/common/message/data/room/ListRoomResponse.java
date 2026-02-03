package com.nhnacademy.messenger.common.message.data.room;

import com.nhnacademy.messenger.common.message.data.MessageData;

import java.util.List;

public record ListRoomResponse(
        List<RoomInfo> rooms
) implements MessageData {
}
