package com.nhnacademy.messenger.common.payload.chat.public_message;

public record ChatMessageRequest(
        Long roomId,
        String message
) {
}
