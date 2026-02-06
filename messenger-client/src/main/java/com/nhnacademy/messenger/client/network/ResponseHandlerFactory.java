package com.nhnacademy.messenger.client.network;

import com.nhnacademy.messenger.client.domain.room.service.ChatRoomClientService;
import com.nhnacademy.messenger.common.message.header.MessageType;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

@Slf4j
public class ResponseHandlerFactory {
    private final Map<MessageType, ResponseHandler> handlerMap = new EnumMap<>(MessageType.class);
    private final ChatRoomClientService chatRoomClientService;

    public ResponseHandlerFactory(ChatRoomClientService chatRoomClientService) {
        this.chatRoomClientService = chatRoomClientService;
        initHandlers();
    }

    private void initHandlers() {
        // MessageHandler가 포함된 패키지 스캔
        // 해당
        Reflections reflections = new Reflections("com.nhnacademy.messenger.client.domain");
        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(MessageHandler.class);

        for (Class<?> clazz : annotatedClasses) {
            try {
                if (!ResponseHandler.class.isAssignableFrom(clazz)) {
                    log.warn("MessageHandler 어노테이션이 ResponseHandler가 아닌 클래스에 사용되었습니다: {}", clazz.getName());
                    continue;
                }

                MessageHandler annotation = clazz.getAnnotation(MessageHandler.class);
                MessageType type = annotation.value();
                ResponseHandler handler = createInstance(clazz);

                handlerMap.put(type, handler);
                log.info("응답 핸들러 자동 등록: {} -> {}", type, clazz.getSimpleName());
            } catch (Exception e) {
                log.error("핸들러 인스턴스 생성 실패: {}", clazz.getName(), e);
            }
        }
    }

    private ResponseHandler createInstance(Class<?> clazz) throws Exception {
        // 1. ChatRoomClientService를 필요로 하는 생성자 확인 (EnterRoomResponseHandler 등)
        try {
            return (ResponseHandler) clazz.getConstructor(ChatRoomClientService.class)
                    .newInstance(chatRoomClientService);
        } catch (NoSuchMethodException ignored) {

        }

        return (ResponseHandler) clazz.getDeclaredConstructor().newInstance();
    }

    public void registerAll(ClientMessageDispatcher dispatcher) {
        // dispatcher.register(MessageType, ResponseHandler) 실행
        handlerMap.forEach(dispatcher::register);
    }
}