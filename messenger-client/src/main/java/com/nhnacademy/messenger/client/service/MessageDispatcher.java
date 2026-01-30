package com.nhnacademy.messenger.client.service;

import com.nhnacademy.messenger.client.service.handler.FunctionalMessageHandler;
import com.nhnacademy.messenger.client.service.handler.MessageHandler;
import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.message.header.MessageType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EnumMap;
import java.util.Map;

/**
 * 수신된 Message를 MessageType에 따라 적절한 핸들러에게 전달(dispatch)하고,
 * 어노테이션 기반으로 핸들러를 자동 등록하는 클래스.
 */
public class MessageDispatcher {
    private final Map<MessageType, FunctionalMessageHandler> handlers = new EnumMap<>(MessageType.class);

    /**
     * 서비스 객체들을 스캔하여 @MessageHandler 어노테이션이 붙은 메서드를 자동으로 핸들러 맵에 등록합니다.
     * @param services 스캔할 서비스 객체들
     */
    public void scanAndRegisterHandlers(Object... services) {
        for (Object service : services) {
            for (Method method : service.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(MessageHandler.class)) {
                    MessageHandler annotation = method.getAnnotation(MessageHandler.class);
                    MessageType messageType = annotation.value();

                    // Reflection을 사용해 메서드를 호출하는 람다를 핸들러로 등록
                    FunctionalMessageHandler handler = (data) -> {
                        try {
                            // 핸들러 메서드 호출
                            method.invoke(service, data);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            System.err.println("Error invoking handler method: " + method.getName());
                            e.printStackTrace();
                        }
                    };
                    
                    handlers.put(messageType, handler);
                    System.out.println("[Dispatcher] Registered handler for " + messageType + ": " + service.getClass().getSimpleName() + "." + method.getName());
                }
            }
        }
    }

    /**
     * Message를 받아 적절한 핸들러에게 전달합니다.
     * @param message 서버로부터 수신된 메시지
     */
    public void dispatch(Message message) {
        MessageType type = message.getHeader().getMessageType();
        FunctionalMessageHandler handler = handlers.get(type);

        if (handler != null) {
            handler.handle(message.getData());
        } else {
            // 처리할 수 없는 메시지 타입에 대한 로그 또는 예외 처리
            System.err.println("[Dispatcher] No handler registered for message type: " + type);
        }
    }
}
