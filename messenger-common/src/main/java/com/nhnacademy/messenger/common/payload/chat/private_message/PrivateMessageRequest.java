package com.nhnacademy.messenger.common.payload.chat.private_message;

public record PrivateMessageRequest(
        String senderId,
        String receiverId,
        String message
) {
}
