package com.nhnacademy.messenger.common.message.data.chat;
import com.nhnacademy.messenger.common.message.data.MessageData;

public record ChatHistoryRequest(
        Long roomId,
        Integer limit,
        Long beforeMessageId
) implements MessageData {
}
