package com.nhnacademy.messenger.common.data.chat.public_message;

public record ChatMessageRequest(
        Long roomId,
        String message
) {
}
