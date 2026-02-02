package com.nhnacademy.messenger.server.network;

import com.nhnacademy.messenger.common.exception.MessengerException;
import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.message.data.error.ErrorCode;
import com.nhnacademy.messenger.common.message.header.MessageType;
import com.nhnacademy.messenger.server.session.domain.Session;
import lombok.extern.slf4j.Slf4j;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/**
 * Message Dispatcher
 * 역할
 * 1. 요청 메시지 지원 여부 판단
 * 2. 요청 메시지 라우팅
 */
@Slf4j
public class MessageDispatcher {

    private final Map<MessageType, RequestHandler> handlerMap = new EnumMap<>(MessageType.class);

    public void register(MessageType type, RequestHandler handler) {
        if (handlerMap.containsKey(type)) {
            log.warn("이미 등록된 핸들러가 교체됩니다: {}", type);
        }
        handlerMap.put(type, handler);
        log.info("핸들러 등록: {} -> {}", type, handler.getClass().getSimpleName());
    }

    public void dispatch(Session session, Message message) {
        // 1. 메시지 타입에 맞는 핸들러 조회
        MessageType type = message.header().type();
        RequestHandler handler = handlerMap.get(type);

        // 2. 핸들러가 없으면 에러 응답 전송
        if (Objects.isNull(handler)) {
            throw new MessengerException(
                    ErrorCode.MESSAGE_TYPE_UNSUPPORTED,
                    "지원하지 않는 메시지 타입입니다: " + type
            );
        }

        // 3. 핸들러에 메시지 처리 위임
        handler.handle(session, message);
    }
}
