package com.nhnacademy.messenger.client.service.handler;

import com.nhnacademy.messenger.common.message.header.MessageType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // 이 어노테이션은 메서드에만 사용할 수 있습니다.
@Retention(RetentionPolicy.RUNTIME) // 런타임에 이 어노테이션 정보를 읽을 수 있습니다.
public @interface MessageHandler {
    MessageType value(); // 처리할 메시지 타입을 명시합니다.
}
