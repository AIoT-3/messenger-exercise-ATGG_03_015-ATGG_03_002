package com.nhnacademy.messenger.server.user.handler;

import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.message.MessageBuilder;
import com.nhnacademy.messenger.common.message.data.auth.LoginRequest;
import com.nhnacademy.messenger.common.message.data.auth.LoginResponse;
import com.nhnacademy.messenger.common.message.data.error.ErrorCode;
import com.nhnacademy.messenger.common.message.header.MessageType;
import com.nhnacademy.messenger.common.util.converter.MessageConverter;
import com.nhnacademy.messenger.server.network.RequestHandler;
import com.nhnacademy.messenger.server.session.domain.Session;
import com.nhnacademy.messenger.server.session.manager.SessionManager;
import com.nhnacademy.messenger.server.user.domain.User;
import com.nhnacademy.messenger.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class LoginRequestHandler implements RequestHandler {

    private final UserService userService;
    private final SessionManager sessionManager;

    @Override
    public void handle(Session session, Message message) {
        LoginRequest loginData = (LoginRequest) MessageConverter.toData(message);

        // 1. 유저 인증
        User authenticatedUser = userService.doLogin(loginData.userId(), loginData.password());

        sessionManager.getSessionByUserId(authenticatedUser.getUserId())
                .ifPresent(existing -> existing.closeWithReason(
                        ErrorCode.AUTH_INVALID_SESSION,
                        "다른 위치에서 로그인되어 현재 세션이 종료됩니다."
                ));
        String sessionId = UUID.randomUUID().toString();

        // 2. 세션 상태 업데이트
        session.registerUser(authenticatedUser, sessionId);

        // 3. 매니저 등록
        sessionManager.addSession(session);

        // 4. 성공 응답 전송
        Message response = MessageBuilder.with(MessageType.LOGIN_SUCCESS)
                .success(true)
                .data(new LoginResponse(
                        authenticatedUser.getUserId(),
                        sessionId,
                        "Welcome!"
                ))
                .build();
        
        session.sendMessage(response);
        log.info("사용자 로그인 성공: {}", authenticatedUser.getUserId());
    }
}
