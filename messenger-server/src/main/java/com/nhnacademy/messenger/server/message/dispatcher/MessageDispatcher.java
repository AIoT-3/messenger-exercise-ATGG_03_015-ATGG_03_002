package com.nhnacademy.messenger.server.message.dispatcher;

import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.message.header.MessageType;
import com.nhnacademy.messenger.server.message.handler.MessageHandler;
import com.nhnacademy.messenger.server.session.domain.Session;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@UtilityClass
public class MessageDispatcher {

    private static final Map<MessageType, MessageHandler> handlerMap = new EnumMap<>(MessageType.class);

    static {
        initHandlers();
    }

    private static void initHandlers() {
        // handler 패키지 하위를 스캔하여 MessageHandler 구현체 검색
        Reflections reflections = new Reflections("com.nhnacademy.messenger.server.message.handler.impl");
        Set<Class<? extends MessageHandler>> handlerClasses = reflections.getSubTypesOf(MessageHandler.class);

        for (Class<? extends MessageHandler> clazz : handlerClasses) {
            // 추상 클래스나 인터페이스는 제외
            if (!Modifier.isAbstract(clazz.getModifiers()) && !clazz.isInterface()) {
                registerHandler(clazz);
            }
        }
    }

    private static void registerHandler(Class<? extends MessageHandler> clazz) {
        try {
            MessageHandler handler = clazz.getDeclaredConstructor().newInstance();
            MessageType type = handler.getType();
            
            if (type == null) {
                log.warn("핸들러 타입이 지정되지 않음: {}", clazz.getSimpleName());
                return;
            }

            handlerMap.put(type, handler);
            log.info("핸들러 등록 완료: {} -> {}", type, clazz.getSimpleName());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.error("핸들러 인스턴스 생성 실패: {}", clazz.getName(), e);
            throw new RuntimeException("핸들러 등록 실패", e);
        }
    }

    public static void dispatch(Session session, Message message) {
        MessageType type = message.header().type();
        MessageHandler handler = handlerMap.get(type);

        if (handler == null) {
            log.warn("핸들러를 찾을 수 없음: type={}", type);
            // 필요 시 에러 응답 전송 로직 추가 가능
            return;
        }

        try {
            handler.handle(session, message);
        } catch (Exception e) {
            log.error("메시지 처리 중 오류 발생: type={}", type, e);
        }
    }
}
