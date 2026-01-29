package com.nhnacademy.messenger.common.message.header;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record MessageHeader(

        MessageType type,
        Boolean success,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime timestamp,
        String sessionId
) {
}
