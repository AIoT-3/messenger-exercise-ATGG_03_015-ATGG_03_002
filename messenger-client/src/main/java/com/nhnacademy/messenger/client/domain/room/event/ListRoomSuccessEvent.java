package com.nhnacademy.messenger.client.domain.room.event;

import java.util.List;

public record ListRoomSuccessEvent(
        List<String> roomList
) {}
