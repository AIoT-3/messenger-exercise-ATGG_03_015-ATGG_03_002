package com.nhnacademy.messenger.server.message.handler.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.nhnacademy.messenger.common.exception.MessengerException;
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
        try {
            LoginRequest loginData = (LoginRequest) MessageConverter.toData(message);

            // 1. 유저 인증
            User authenticatedUser = session.getUserService().doLogin(loginData.userId(), loginData.password());
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

        } catch (MessengerException e) {
            session.sendError(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            log.error("로그인 처리 중 오류", e);
            session.sendError(ErrorCode.INTERNAL_SERVER_ERROR, "로그인 처리 중 오류 발생");
        }
    }
}
