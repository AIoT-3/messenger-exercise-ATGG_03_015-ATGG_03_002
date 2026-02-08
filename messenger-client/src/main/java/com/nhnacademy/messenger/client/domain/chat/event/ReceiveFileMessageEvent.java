package com.nhnacademy.messenger.client.domain.chat.event;

public record ReceiveFileMessageEvent(
        Long roomId,
        Long messageId,
        String senderId,
        String fileName,
        Long fileSize,
        String fileData
) {
}
