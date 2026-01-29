package com.nhnacademy.messenger.common.message.data.chat;
import com.nhnacademy.messenger.common.message.data.MessageData;

public record ChatMessageHistoryRequest(
        Long roomId,
        Integer limit,
        Long beforeMessageId
) implements MessageData {
}
