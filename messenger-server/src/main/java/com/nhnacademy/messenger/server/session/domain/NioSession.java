package com.nhnacademy.messenger.server.session.domain;

import com.nhnacademy.messenger.common.event.EventBus;
import com.nhnacademy.messenger.common.exception.MessengerException;
import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.message.MessageBuilder;
import com.nhnacademy.messenger.common.message.data.error.ErrorCode;
import com.nhnacademy.messenger.common.message.data.error.ErrorResponse;
import com.nhnacademy.messenger.common.message.header.MessageType;
import com.nhnacademy.messenger.common.message.header.RequestHeader;
import com.nhnacademy.messenger.common.util.reader.nio.NioMessageReader;
import com.nhnacademy.messenger.common.util.writer.MessageWriter;
import com.nhnacademy.messenger.common.util.writer.nio.NioMessageWriter;
import com.nhnacademy.messenger.server.session.event.SessionDisconnectedEvent;
import com.nhnacademy.messenger.server.session.manager.SessionManager;
import com.nhnacademy.messenger.server.user.domain.User;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

import static com.nhnacademy.messenger.common.message.data.error.ErrorCode.*;

/**
 * NioSession
 * 역할
 * 1. NIO 채널 기반 메시지 수신/전송
 * 2. 공통 규칙 검사 (sessionId 검사, 세션 무결성)
 * 3. 중앙 에러 응답 생성
 */
@Slf4j
public class NioSession implements Session {

    @Getter
    private String id;
    @Getter
    private User user;
    @Getter
    private final Set<Long> joinedRoomIds = ConcurrentHashMap.newKeySet();

    private final AtomicBoolean closed = new AtomicBoolean(false);

    private final SocketChannel channel;
    private final NioMessageReader reader;
    private final MessageWriter writer;

    private final SessionManager sessionManager;
    
    // 가상 스레드 친화적인 ReentrantLock 사용 (버퍼 조작 동기화)
    public final ReentrantLock lock = new ReentrantLock();

    public NioSession(
            SocketChannel channel,
            SessionManager sessionManager
    ) {
        this.channel = channel;
        this.sessionManager = sessionManager;
        this.reader = new NioMessageReader();
        this.writer = new NioMessageWriter(channel);
    }

    /**
     * 채널에서 메시지 읽기 수행 (NioMessageReader에 위임)
     */
    public Message readMessage() throws IOException {
        lock.lock();
        try {
            return reader.read(channel);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void registerUser(User user, String sessionId) {
        if (Objects.nonNull(this.user)) {
            throw new MessengerException(REQUEST_INVALID_MESSAGE, "이미 로그인된 세션입니다.");
        }
        this.user = user;
        this.id = sessionId;
    }

    @Override
    public void sendMessage(Message message) {
        writer.writeMessage(message);
    }

    @Override
    public void sendError(ErrorCode code, String message) {
        Message errorMessage = MessageBuilder.with(MessageType.ERROR)
                .success(false)
                .data(new ErrorResponse(code, message))
                .build();
        sendMessage(errorMessage);
    }

    @Override
    public void closeWithReason(ErrorCode code, String message) {
        try {
            sendError(code, message);
        } catch (RuntimeException e) {
            log.warn("NIO 종료 알림 전송 실패: {}", e.getMessage());
        } finally {
            disconnect();
        }
    }

    @Override
    public void validateLoggedIn() {
        if (Objects.isNull(this.user)) {
            throw new MessengerException(AUTH_UNAUTHORIZED, "로그인 후 이용 가능합니다.");
        }
    }

    @Override
    public void joinRoom(Long roomId) {
        joinedRoomIds.add(roomId);
    }

    @Override
    public void leaveRoom(Long roomId) {
        joinedRoomIds.remove(roomId);
    }

    @Override
    public void logout() {
        if (Objects.nonNull(this.id)) {
            sessionManager.removeSession(this.id);
        }
        // 로그아웃 시 리소스 정리를 위해 이벤트 발행
        EventBus.INSTANCE.publish(new SessionDisconnectedEvent(this));
        
        this.user = null;
        this.id = null;
        this.joinedRoomIds.clear();
        log.info("NIO 사용자 로그아웃 완료");
    }

    @Override
    public void validateMessage(Message message) {
        if (Objects.isNull(message) || Objects.isNull(message.header()) || Objects.isNull(message.header().type())) {
            throw new MessengerException(REQUEST_INVALID_MESSAGE, "메시지 형식이 올바르지 않습니다.");
        }

        if (!(message.header() instanceof RequestHeader requestHeader)) {
            throw new MessengerException(REQUEST_INVALID_MESSAGE, "요청 헤더 형식이 아닙니다.");
        }

        if (requestHeader.type() == MessageType.LOGIN) {
            return;
        }

        if (Objects.isNull(requestHeader.sessionId())) {
            throw new MessengerException(AUTH_UNAUTHORIZED, "sessionId가 누락되었습니다.");
        }

        validateSessionIntegrity(requestHeader.sessionId());
    }

    private void validateSessionIntegrity(String requestSessionId) {
        boolean isValid = sessionManager.getSession(requestSessionId)
                .filter(existing -> existing == this)
                .isPresent();

        if (!isValid) {
            throw new MessengerException(AUTH_INVALID_SESSION, "유효하지 않은 세션입니다.");
        }
    }

    @Override
    public void disconnect() {
        if (closed.getAndSet(true)) {
            return;
        }

        if (Objects.nonNull(this.id)) {
            sessionManager.removeSession(this.id);
        }
        
        try {
            channel.close();
            // 기존 이벤트 핸들러와 호환되도록 이벤트 발행
            EventBus.INSTANCE.publish(new SessionDisconnectedEvent(this));
        } catch (IOException e) {
            // 무시
        }
        log.info("NIO 세션 종료: {}", this.id);
    }
}