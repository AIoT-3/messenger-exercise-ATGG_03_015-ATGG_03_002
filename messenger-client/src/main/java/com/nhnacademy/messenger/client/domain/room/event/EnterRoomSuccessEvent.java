package com.nhnacademy.messenger.client.domain.room.event;

import java.util.List;

public record EnterRoomSuccessEvent(
        Long roomId,
        List<String> users
) {}
