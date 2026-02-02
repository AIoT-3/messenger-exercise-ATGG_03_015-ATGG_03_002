package com.nhnacademy.messenger.common.event;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

@Slf4j
public record Subscriber(
        Object instance,
        Method method
) {
    public void invoke(Object event) {
        try {
            method.invoke(instance, event);
        } catch (Exception e) {
            log.error("이벤트 처리 중 오류 발생: {}", event.getClass().getSimpleName(), e);
        }
    }
}
