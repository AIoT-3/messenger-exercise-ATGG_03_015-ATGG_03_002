package com.nhnacademy.messenger.client.network.annotation;

import com.nhnacademy.messenger.common.message.header.MessageType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 응답 핸들러 매핑을 위한 어노테이션
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResponseMapping {
    MessageType type();
}
