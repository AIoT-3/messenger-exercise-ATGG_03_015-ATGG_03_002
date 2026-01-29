package com.nhnacademy.messenger.common.message.data.chat;

public record ChatMessageRequest(
        Long roomId,
        String message
) {
}
