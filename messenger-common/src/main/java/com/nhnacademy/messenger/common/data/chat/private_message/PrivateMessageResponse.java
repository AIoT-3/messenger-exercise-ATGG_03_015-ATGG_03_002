package com.nhnacademy.messenger.common.data.chat.private_message;

public record PrivateMessageResponse(
        String senderId,
        String receiverId,
        String message,
        Long messageId
) {
}
