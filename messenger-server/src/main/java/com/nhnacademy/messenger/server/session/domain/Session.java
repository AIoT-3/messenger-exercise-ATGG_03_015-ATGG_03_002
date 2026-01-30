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

    private StreamMessageReader reader;
    private MessageWriter writer;

    private final Socket socket;

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
            log.error("스트림 초기화 실패", e);
        }
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Message request = reader.readMessage();
                    if (!validateMessage(request)) {
                        continue;
                    }
                    MessageDispatcher.dispatch(this, request);
                } catch (MessageConvertException e) {
                    log.warn("메시지 변환 실패: {}", e.getMessage());
                    sendError(ErrorCode.REQUEST_INVALID_MESSAGE, "유효하지 않은 메시지 형식입니다.");
                } catch (MessengerException e) {
                    sendError(e.getErrorCode(), e.getMessage());
                } catch (RuntimeException e) {
                    log.error("메시지 처리 중 알 수 없는 오류", e);
                    sendError(ErrorCode.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.");
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

    // 외부 핸들러에서 상태 변경을 위해 호출
    public void registerUser(User user, String sessionId) {
        if (this.user != null) {
            throw new IllegalStateException("이미 로그인된 세션입니다.");
        }
        this.user = user;
        this.id = sessionId;
    }

    public void sendMessage(Message message) {
        writer.writeMessage(message);
    }

    public void sendError(ErrorCode code, String message) {
        ResponseHeader header = ResponseHeader.fail(MessageType.ERROR);
        JsonNode data = MessageConverter.objectMapper.valueToTree(new ErrorResponse(code, message));
        sendMessage(new Message(header, data));
    }

    private boolean validateMessage(Message message) {
        if (message == null || message.header() == null || message.header().type() == null) {
            sendError(ErrorCode.REQUEST_INVALID_MESSAGE, "유효하지 않은 메시지입니다.");
            return false;
        }

        if (!(message.header() instanceof RequestHeader requestHeader)) {
            sendError(ErrorCode.REQUEST_INVALID_MESSAGE, "요청 헤더가 아닙니다.");
            return false;
        }

        MessageType type = requestHeader.type();
        if (type != MessageType.LOGIN) {
            if (requestHeader.sessionId() == null) {
                sendError(ErrorCode.AUTH_UNAUTHORIZED, "세션이 필요합니다.");
                return false;
            }

            if (sessionManager.getSession(requestHeader.sessionId())
                    .filter(existing -> existing == this)
                    .isEmpty()) {
                sendError(ErrorCode.AUTH_INVALID_SESSION, "유효하지 않은 세션입니다.");
                return false;
            }
        }

        return true;
    }

    private void disconnect() {
        if (this.id != null) {
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
