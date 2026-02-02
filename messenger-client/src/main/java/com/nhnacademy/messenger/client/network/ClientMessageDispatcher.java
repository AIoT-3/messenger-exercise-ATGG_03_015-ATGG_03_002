package com.nhnacademy.messenger.client.network;

import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.message.header.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class ClientMessageDispatcher {

    private final Map<MessageType, ResponseHandler> handlerMap = new EnumMap<>(MessageType.class);

    public void register(MessageType type, ResponseHandler handler) {
        if (handlerMap.containsKey(type)) {
            log.warn("이미 등록된 핸들러가 교체됩니다: {}", type);
        }
        handlerMap.put(type, handler);
        log.info("응답 핸들러 등록: {} -> {}", type, handler.getClass().getSimpleName());
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