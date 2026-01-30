package com.nhnacademy.messenger.server.session.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.exception.MessageConvertException;
import com.nhnacademy.messenger.common.exception.MessengerException;
import com.nhnacademy.messenger.common.message.data.error.ErrorCode;
import com.nhnacademy.messenger.common.message.data.error.ErrorResponse;
import com.nhnacademy.messenger.common.message.header.MessageType;
import com.nhnacademy.messenger.common.message.header.RequestHeader;
import com.nhnacademy.messenger.common.message.header.ResponseHeader;
import com.nhnacademy.messenger.common.util.converter.MessageConverter;
import com.nhnacademy.messenger.common.util.reader.bio.StreamMessageReader;
import com.nhnacademy.messenger.common.util.writer.MessageWriter;
import com.nhnacademy.messenger.common.util.writer.bio.StreamMessageWriter;
import com.nhnacademy.messenger.server.message.dispatcher.MessageDispatcher;
import com.nhnacademy.messenger.server.session.manager.SessionManager;
import com.nhnacademy.messenger.server.user.domain.User;
import com.nhnacademy.messenger.server.user.service.UserService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

/**
 * Session
 * 역할
 * 1. 메시지 수신/전송
 * 2. 공통 규칙 검사 (sessionId 검사, 세션 유효성)
 * 3. 중앙 에러 응답 생성
 */
@Slf4j
public class Session implements Runnable {

    @Getter
    private String id; // 로그인 성공 시 발급
    @Getter
    private User user; // 로그인 전에는 null

    private final Socket socket;
    private final StreamMessageReader reader;
    private final MessageWriter writer;

    // Getter: 리플렉션에 의해 동적으로 생성되는 핸들러에서 주입이 힘들기 때문에 getter 사용
    @Getter
    private final SessionManager sessionManager;
    @Getter
    private final UserService userService;

    public Session(Socket socket, SessionManager sessionManager, UserService userService) {
        this.socket = socket;
        this.sessionManager = sessionManager;
        this.userService = userService;
        try {
            this.reader = new StreamMessageReader(socket.getInputStream());
            this.writer = new StreamMessageWriter(socket.getOutputStream());
        } catch (IOException e) {
            disconnect();
            throw new IllegalStateException("세션 초기화 실패", e);
        }
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    // 1. 메시지 수신
                    Message request = reader.readMessage();
                    // 2. 공통 규칙 검사
                    validateMessage(request);
                    // 3. 메시지 디스패치
                    MessageDispatcher.dispatch(this, request);

                } catch (MessageConvertException e) {
                    // 메시지 변환에 실패한 경우
                    sendError(ErrorCode.REQUEST_INVALID_MESSAGE, "메시지 형식이 올바르지 않습니다. 요청을 다시 확인해주세요.");

                } catch (MessengerException e) {
                    // 공통 규칙 검사 or 핸들러에서 발생한 메시지 처리에서 예외가 발생한 경우
                    sendError(e.getErrorCode(), e.getMessage());

                } catch (RuntimeException e) {
                    // 그 외 예기치 못한 예외가 발생한 경우
                    sendError(ErrorCode.INTERNAL_SERVER_ERROR, "서버 처리 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
                }
            }
        } catch (EOFException e) {
            log.info("클라이언트가 연결을 종료했습니다.");
        } catch (IOException e) {
            log.error("통신 오류: {}", e.getMessage());
        } finally {
            disconnect();
        }
    }

    // 세션에 사용자 등록
    public void registerUser(User user, String sessionId) {
        if (Objects.nonNull(this.user)) {
            throw new IllegalStateException("이미 로그인된 세션입니다.");
        }
        this.user = user;
        this.id = sessionId;
    }

    // 메시지 전송
    public void sendMessage(Message message) {
        writer.writeMessage(message);
    }

    // 에러 응답 전송
    public void sendError(ErrorCode code, String message) {
        ResponseHeader header = ResponseHeader.fail(MessageType.ERROR);
        JsonNode data = MessageConverter.objectMapper.valueToTree(new ErrorResponse(code, message));
        sendMessage(new Message(header, data));
    }

    // 에러 응답과 함께 세션 종료
    public void closeWithReason(ErrorCode code, String message) {
        try {
            sendError(code, message);
        } catch (RuntimeException e) {
            log.warn("종료 알림 전송 실패: {}", e.getMessage());
        } finally {
            disconnect();
        }
    }

    // 공통 규칙 검사
    private void validateMessage(Message message) {

        // 1. 메시지 헤더 및 타입 검사
        if (Objects.isNull(message) || Objects.isNull(message.header()) || Objects.isNull(message.header().type())) {
            throw new MessengerException(ErrorCode.REQUEST_INVALID_MESSAGE,
                    "메시지 헤더 또는 타입이 누락되었습니다.");
        }

        if (!(message.header() instanceof RequestHeader requestHeader)) {
            throw new MessengerException(ErrorCode.REQUEST_INVALID_MESSAGE,
                    "요청 헤더 형식이 아닙니다.");
        }

        // 로그인 요청은 세션 검증 제외
        if (requestHeader.type() == MessageType.LOGIN) {
            return;
        }

        if (Objects.isNull(requestHeader.sessionId())) {
            throw new MessengerException(ErrorCode.AUTH_UNAUTHORIZED,
                    "로그인 후 이용 가능합니다. sessionId가 누락되었습니다.");
        }

        validateSessionIntegrity(requestHeader.sessionId());
    }

    // 세션 무결성 검사
    private void validateSessionIntegrity(String requestSessionId) {
        boolean isValid = sessionManager.getSession(requestSessionId)
                .filter(existing -> existing == this) // 주소 비교로 동일 세션인지 확인
                .isPresent();

        if (!isValid) {
            throw new MessengerException(
                    ErrorCode.AUTH_INVALID_SESSION,
                    "유효하지 않은 세션입니다. 다시 로그인해주세요.");
        }
    }

    // 세션 종료 처리
    private void disconnect() {
        if (Objects.nonNull(this.id)) {
            sessionManager.removeSession(this.id);
        }
        try {
            socket.close();
        } catch (IOException e) {
            // 무시
        }
        log.info("세션 종료: {}", this.id);
    }
}
