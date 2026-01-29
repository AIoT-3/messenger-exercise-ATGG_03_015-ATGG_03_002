package com.nhnacademy.messenger.common.message.data.chat;

public record ChatMessageResponse(
        Long roomId,
        Long messageId
) {
}
