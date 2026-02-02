package com.nhnacademy.messenger.client.network;

import com.nhnacademy.messenger.client.network.annotation.ResponseMapping;
import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.message.header.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Constructor;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class ClientMessageDispatcher {

    private final Map<MessageType, ResponseHandler> handlerMap = new EnumMap<>(MessageType.class);

    public void init(String basePackage) {
        log.debug("핸들러 스캔 시작 (패키지: {})", basePackage);
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .forPackages(basePackage)
                .addScanners(Scanners.TypesAnnotated));
        
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(ResponseMapping.class);

        classes.stream()
                .filter(clazz -> {
                    if (ResponseHandler.class.isAssignableFrom(clazz)) {
                        return true;
                    }
                    log.warn("@MessageMapping이 붙어있지만 MessageHandler를 구현하지 않은 클래스 무시: {}", clazz.getName());
                    return false;
                })
                .forEach(clazz -> {
                    try {
                        ResponseMapping mapping = clazz.getAnnotation(ResponseMapping.class);
                        ResponseHandler handler = createHandlerInstance(clazz);
                        handlerMap.put(mapping.type(), handler);
                        log.info("핸들러 등록 완료: {} -> {}", mapping.type(), clazz.getSimpleName());
                    } catch (Exception e) {
                        log.error("핸들러 인스턴스 생성 실패: {}", clazz.getName(), e);
                    }
                });
    }

    private ResponseHandler createHandlerInstance(Class<?> clazz) throws Exception {
        Constructor<?> constructor = clazz.getConstructor();
        return (ResponseHandler) constructor.newInstance();
    }

    public void dispatch(Message message) {
        try {
            MessageType type = message.header().type();
            
            Optional.ofNullable(handlerMap.get(type))
                    .ifPresentOrElse(
                        handler -> handleSafely(handler, message, type),
                        () -> log.warn("등록된 핸들러가 없습니다: {}", type)
                    );
        } catch (Exception e) {
            log.error("메시지 타입 파싱 실패", e);
        }
    }

    private void handleSafely(ResponseHandler handler, Message message, MessageType type) {
        try {
            handler.handle(message);
        } catch (Exception e) {
            log.error("메시지 처리 중 오류 발생 (타입: {})", type, e);
        }
    }
}