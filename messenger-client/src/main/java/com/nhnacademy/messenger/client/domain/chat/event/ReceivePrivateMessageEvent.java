package com.nhnacademy.messenger.client.domain.chat.event;

public record ReceivePrivateMessageEvent(
        String senderId,
        String content
) {
}
