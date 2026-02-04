package com.nhnacademy.messenger.common.message.data.chat;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record MessageInfo(
        Long messageId,
        String senderId,
        String senderName,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        LocalDateTime timestamp,
        String content
) {}