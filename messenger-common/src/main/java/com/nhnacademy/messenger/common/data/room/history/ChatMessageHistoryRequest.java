package com.nhnacademy.messenger.common.data.room.history;

public record ChatMessageHistoryRequest(
        Long roomId,
        Integer limit,
        Long beforeMessageId
) {
}
