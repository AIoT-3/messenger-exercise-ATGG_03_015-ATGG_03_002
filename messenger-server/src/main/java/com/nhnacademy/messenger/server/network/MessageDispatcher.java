package com.nhnacademy.messenger.server.network;

import com.nhnacademy.messenger.common.exception.MessengerException;
import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.message.data.error.ErrorCode;
import com.nhnacademy.messenger.common.message.header.MessageType;
import com.nhnacademy.messenger.server.network.annotation.RequestMapping;
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

    private static final Map<MessageType, RequestHandler> handlerMap = new EnumMap<>(MessageType.class);

    static {
        initHandlers();
    }

    private static void initHandlers() {
        // 1. 서버 패키지 전체 스캔하여 @RequestMapping 검색
        Reflections reflections = new Reflections(new org.reflections.util.ConfigurationBuilder()
                .forPackages("com.nhnacademy.messenger.server")
                .addScanners(org.reflections.scanners.Scanners.TypesAnnotated));
        
        Set<Class<?>> handlerClasses = reflections.getTypesAnnotatedWith(RequestMapping.class);
        log.info("스캔된 핸들러 클래스 개수: {}", handlerClasses.size());

        // 2. 각 핸들러 등록
        handlerClasses.forEach(MessageDispatcher::registerHandler);
    }

    private static void registerHandler(Class<?> clazz) {
        if (!RequestHandler.class.isAssignableFrom(clazz)) {
            log.warn("@MessageMapping이 있지만 MessageHandler를 구현하지 않음: {}", clazz.getName());
            return;
        }

        try {
            // 1. 핸들러 인스턴스 생성
            RequestHandler handler = (RequestHandler) clazz.getDeclaredConstructor().newInstance();
            
            // 2. 어노테이션에서 타입 추출
            RequestMapping mapping = clazz.getAnnotation(RequestMapping.class);
            MessageType type = mapping.type();

            // 3. 핸들러 등록
            handlerMap.put(type, handler);
            log.info("핸들러 등록 완료: {} -> {}", type, clazz.getSimpleName());

        } catch (Exception e) {
            log.error("핸들러 인스턴스 생성 실패: {}", clazz.getName(), e);
            throw new RuntimeException("핸들러 등록 실패", e);
        }
    }

    public static void dispatch(Session session, Message message) {
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
