package com.nhnacademy.messenger.common.payload.chat.public_message;

public record ChatMessageResponse(
        Long roomId,
        Long messageId
) {
}
