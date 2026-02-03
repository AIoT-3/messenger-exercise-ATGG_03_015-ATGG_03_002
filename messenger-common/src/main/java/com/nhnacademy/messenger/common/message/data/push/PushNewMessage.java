package com.nhnacademy.messenger.common.message.data.push;

import com.nhnacademy.messenger.common.message.data.MessageData;

public record PushNewMessage(
        Long roomId,
        Long messageId,
        String senderId,
        String content,
        PushMessageType type,
        String fileName,
        Long fileSize
) implements MessageData {
}
