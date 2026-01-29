package com.nhnacademy.messenger.common.message.header;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.Objects;

public record ResponseHeader(
        MessageType type,
        
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime timestamp,
        
        boolean success

) implements Header {

    public ResponseHeader {
        Objects.requireNonNull(type, "MessageType은 null일 수 없습니다.");
        Objects.requireNonNull(timestamp, "Timestamp은 null일 수 없습니다.");
    }

    // 성공 응답 생성 팩토리
    public static ResponseHeader success(MessageType type) {
        return new ResponseHeader(type, LocalDateTime.now(), true);
    }

    // 실패 응답 생성 팩토리
    public static ResponseHeader fail(MessageType type) {
        return new ResponseHeader(type, LocalDateTime.now(), false);
    }
}
