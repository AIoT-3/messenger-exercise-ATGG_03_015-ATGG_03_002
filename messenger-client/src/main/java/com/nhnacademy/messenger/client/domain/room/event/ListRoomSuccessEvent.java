package com.nhnacademy.messenger.client.domain.room.event;

import com.nhnacademy.messenger.common.message.data.room.RoomInfo;
import java.util.List;

public record ListRoomSuccessEvent(
        List<RoomInfo> roomList
) {}
