package com.nhnacademy.messenger.common.message.data.chat;

public record PrivateMessageRequest(
        String senderId,
        String receiverId,
        String message
) {
}
