package com.nhnacademy.messenger.common.payload.room.history;

public record ChatMessageHistoryRequest(
        Long roomId,
        Integer limit,
        Long beforeMessageId
) {
}
