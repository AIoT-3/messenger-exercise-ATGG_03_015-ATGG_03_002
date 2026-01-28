package com.nhnacademy.messenger.common.data.chat.private_message;

public record PrivateMessageRequest(
        String senderId,
        String receiverId,
        String message
) {
}
