package com.nhnacademy.messenger.server.message.dispatcher;

import com.fasterxml.jackson.databind.JsonNode;
import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.message.data.error.ErrorCode;
import com.nhnacademy.messenger.common.message.data.error.ErrorResponse;
import com.nhnacademy.messenger.common.message.header.MessageType;
import com.nhnacademy.messenger.common.message.header.ResponseHeader;
import com.nhnacademy.messenger.common.util.converter.MessageConverter;
import com.nhnacademy.messenger.server.message.handler.MessageHandler;
import com.nhnacademy.messenger.server.session.domain.Session;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Message Dispatcher
 * 역할
 * 1. 요청 메시지 지원 여부 판단
 * 2. 요청 메시지 라우팅
 */
@Slf4j
@UtilityClass
public class MessageDispatcher {

    private static final Map<MessageType, MessageHandler> handlerMap = new EnumMap<>(MessageType.class);

    static {
        initHandlers();
    }

    private static void initHandlers() {
        // 1. handler 패키지 하위를 스캔하여 MessageHandler 구현체 검색
        Reflections reflections = new Reflections("com.nhnacademy.messenger.server.message.handler.impl");
        Set<Class<? extends MessageHandler>> handlerClasses = reflections.getSubTypesOf(MessageHandler.class);

        // 2. 각 핸들러 등록
        handlerClasses.forEach(MessageDispatcher::registerHandler);
    }

    private static void registerHandler(Class<? extends MessageHandler> clazz) {
        try {
            // 1. 핸들러 인스턴스 생성
            MessageHandler handler = clazz.getDeclaredConstructor().newInstance();
            MessageType type = handler.getType();

            // 2. 핸들러 타입 유효성 검사
            if (Objects.isNull(type)) {
                log.warn("핸들러 타입이 지정되지 않음: {}", clazz.getSimpleName());
                return;
            }

            // 3. 핸들러 등록
            handlerMap.put(type, handler);

        } catch (Exception e) {
            log.error("핸들러 인스턴스 생성 실패: {}", clazz.getName(), e);
            throw new RuntimeException("핸들러 등록 실패", e);
        }
    }

    public static void dispatch(Session session, Message message) {
        // 1. 메시지 타입에 맞는 핸들러 조회
        MessageType type = message.header().type();
        MessageHandler handler = handlerMap.get(type);

        // 2. 핸들러가 없으면 에러 응답 전송
        if (Objects.isNull(handler)) {
            ResponseHeader header = ResponseHeader.fail(MessageType.ERROR);
            JsonNode data = MessageConverter.objectMapper.valueToTree(new ErrorResponse(
                    ErrorCode.MESSAGE_TYPE_UNSUPPORTED,
                    "지원하지 않는 메시지 타입입니다: " + type
            ));
            session.sendMessage(new Message(header, data));
            return;
        }

        // 3. 핸들러에 메시지 처리 위임
        handler.handle(session, message);
    }
}
