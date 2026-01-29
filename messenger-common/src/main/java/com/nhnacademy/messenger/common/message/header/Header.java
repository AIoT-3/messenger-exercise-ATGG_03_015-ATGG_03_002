package com.nhnacademy.messenger.common.message.header;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.time.LocalDateTime;

/**
 * Header 인터페이스
 * Sealed Interface: 구현 가능한 하위 타입을 RequestHeader와 ResponseHeader로 제한
 * Jackson - Deduction 기능: JSON 데이터의 필드를 기반으로 적절한 하위 타입을 자동으로 추론
 * Deduction - defaultImpl: sessionId 필드가 없는 로그인 요청을 위해 기본 구현체로 RequestHeader 지정
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.DEDUCTION,
    defaultImpl = RequestHeader.class
)
@JsonSubTypes({
    @JsonSubTypes.Type(RequestHeader.class),
    @JsonSubTypes.Type(ResponseHeader.class)
})
public sealed interface Header permits RequestHeader, ResponseHeader {
    MessageType type();
    LocalDateTime timestamp();
}