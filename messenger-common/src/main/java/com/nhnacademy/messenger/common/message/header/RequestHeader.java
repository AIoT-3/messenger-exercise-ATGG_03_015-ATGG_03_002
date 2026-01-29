package com.nhnacademy.messenger.common.message.header;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.Objects;

public record RequestHeader(
        MessageType type,
        
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime timestamp,
        
        String sessionId

) implements Header {

    public RequestHeader {
        Objects.requireNonNull(type, "MessageType은 null일 수 없습니다.");
        Objects.requireNonNull(timestamp, "Timestamp은 null일 수 없습니다.");
        
        // LOGIN 타입이 아닐 때만 SessionId 필수 검증
        if (!type.equals(MessageType.LOGIN) && Objects.isNull(sessionId)) {
            throw new IllegalArgumentException("로그인 요청을 제외한 모든 요청에는 SessionId가 필수입니다.");
        }
    }

    // 일반 요청용 편의 생성자
    public static RequestHeader of(MessageType type, String sessionId) {
        return new RequestHeader(type, LocalDateTime.now(), sessionId);
    }
    
    // 로그인 요청용 편의 생성자
    public static RequestHeader login() {
        return new RequestHeader(MessageType.LOGIN, LocalDateTime.now(), null);
    }
}