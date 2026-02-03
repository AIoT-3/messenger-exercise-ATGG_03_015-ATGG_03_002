package com.nhnacademy.messenger.client.domain.chat.event;

public record ReceiveMessageEvent(
        Long roomId,
        Long messageId,
        String senderId,
        String content
) {}
