package com.nhnacademy.messenger.common.message.data.room.history;

public record ChatMessageHistoryRequest(
        Long roomId,
        Integer limit,
        Long beforeMessageId
) {
}
