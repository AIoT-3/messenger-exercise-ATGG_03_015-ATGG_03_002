package com.nhnacademy.messenger.server.message.handler.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.message.data.auth.LoginRequest;
import com.nhnacademy.messenger.common.message.data.auth.LoginResponse;
import com.nhnacademy.messenger.common.message.data.error.ErrorCode;
import com.nhnacademy.messenger.common.message.header.MessageType;
import com.nhnacademy.messenger.common.message.header.ResponseHeader;
import com.nhnacademy.messenger.common.util.converter.MessageConverter;
import com.nhnacademy.messenger.server.message.handler.MessageHandler;
import com.nhnacademy.messenger.server.session.domain.Session;
import com.nhnacademy.messenger.server.user.domain.User;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class LoginMessageHandler implements MessageHandler {

    @Override
    public MessageType getType() {
        return MessageType.LOGIN;
    }

    @Override
    public void handle(Session session, Message message) {
        LoginRequest loginData = (LoginRequest) MessageConverter.toData(message);

        // 1. 유저 인증
        User authenticatedUser = session.getUserService().doLogin(loginData.userId(), loginData.password());

        session.getSessionManager()
                .getSessionByUserId(authenticatedUser.getUserId())
                .ifPresent(existing -> existing.closeWithReason(
                        ErrorCode.AUTH_INVALID_SESSION,
                        "다른 위치에서 로그인되어 현재 세션이 종료됩니다."
                ));
        String sessionId = UUID.randomUUID().toString();

        // 2. 세션 상태 업데이트
        session.registerUser(authenticatedUser, sessionId);

        // 3. 매니저 등록
        session.getSessionManager().addSession(session);

        // 4. 성공 응답 전송
        ResponseHeader header = ResponseHeader.success(MessageType.LOGIN_SUCCESS);
        JsonNode data = MessageConverter.objectMapper.valueToTree(new LoginResponse(
                authenticatedUser.getUserId(),
                sessionId,
                "Welcome!"
        ));
        
        session.sendMessage(new Message(header, data));
        log.info("사용자 로그인 성공: {}", authenticatedUser.getUserId());
    }
}
