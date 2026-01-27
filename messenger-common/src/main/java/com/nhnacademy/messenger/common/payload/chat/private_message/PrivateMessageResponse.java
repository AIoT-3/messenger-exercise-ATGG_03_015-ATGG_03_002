package com.nhnacademy.messenger.common.payload.chat.private_message;

public record PrivateMessageResponse(
        String senderId,
        String receiverId,
        String message,
        Long messageId
) {
}
