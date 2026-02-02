package com.nhnacademy.messenger.common.message.data.chat;
import com.nhnacademy.messenger.common.message.data.MessageData;

public record ChatResponse(
        Long roomId,
        Long messageId
) implements MessageData {
}
