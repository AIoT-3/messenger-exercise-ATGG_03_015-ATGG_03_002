package com.nhnacademy.messenger.common.message.data.chat;

public record PrivateMessageResponse(
        String senderId,
        String receiverId,
        String message,
        Long messageId
) {
}
