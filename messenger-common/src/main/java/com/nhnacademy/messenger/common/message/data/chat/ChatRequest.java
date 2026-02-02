package com.nhnacademy.messenger.common.message.data.chat;
import com.nhnacademy.messenger.common.message.data.MessageData;

public record ChatRequest(
        Long roomId,
        String message
) implements MessageData {
}
