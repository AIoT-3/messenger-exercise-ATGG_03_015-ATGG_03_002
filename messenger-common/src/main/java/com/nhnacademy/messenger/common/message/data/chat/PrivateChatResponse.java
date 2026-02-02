package com.nhnacademy.messenger.common.message.data.chat;
import com.nhnacademy.messenger.common.message.data.MessageData;

public record PrivateChatResponse(
        String senderId,
        String receiverId,
        String message,
        Long messageId
) implements MessageData {
}
