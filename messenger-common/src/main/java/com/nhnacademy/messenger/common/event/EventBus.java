package com.nhnacademy.messenger.common.event;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 이벤트 버스
 * 역할
 * 1. 이벤트 리스너 등록
 * 2. 이벤트 발행
 */
@Slf4j
public enum EventBus {
    INSTANCE;

    private final Map<Class<?>, List<Subscriber>> subscribers = new ConcurrentHashMap<>();

    public void register(Object listener) {
        for (Method method : listener.getClass().getMethods()) {
            if (method.isAnnotationPresent(EventListener.class)) {
                if (method.getParameterCount() != 1) {
                    log.warn("@EventListener 메서드는 파라미터가 정확히 하나여야 합니다: {}", method.getName());
                    continue;
                }
                Class<?> eventType = method.getParameterTypes()[0];
                subscribers.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>())
                           .add(new Subscriber(listener, method));
            }
        }
    }

    public void publish(Object event) {
        Optional.ofNullable(subscribers.get(event.getClass()))
                .ifPresent(list -> list.forEach(subscriber -> subscriber.invoke(event)));
    }
}
