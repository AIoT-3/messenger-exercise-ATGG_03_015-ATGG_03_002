package com.nhnacademy.messenger.common.message.data.chat;
import com.nhnacademy.messenger.common.message.data.MessageData;

public record PrivateChatRequest(
        String senderId,
        String receiverId,
        String message
) implements MessageData {
}
