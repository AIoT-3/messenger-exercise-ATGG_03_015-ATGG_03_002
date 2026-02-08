package com.nhnacademy.messenger.client.domain.user.service;

import com.nhnacademy.messenger.client.network.MessageClient;
import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.message.MessageBuilder;
import com.nhnacademy.messenger.common.message.data.auth.LoginRequest;
import com.nhnacademy.messenger.common.message.header.MessageType;
import lombok.RequiredArgsConstructor;

import com.nhnacademy.messenger.client.session.ClientSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class UserClientService {

    private final MessageClient messageClient;

    public void login(String userId, String password) {
        Message message = MessageBuilder.with(MessageType.LOGIN)
                .data(new LoginRequest(userId, password))
                .build();
        
        messageClient.send(message);
    }

    public void logout() {
        String sessionId = ClientSession.INSTANCE.getSessionId();
        if (sessionId == null) {
            log.error("세션에 접속되지 않은 상태로 로그아웃을 시도하였습니다.");
            return;
        }
        Message message = MessageBuilder.with(MessageType.LOGOUT)
                .sessionId(sessionId)
                .build();

        messageClient.send(message);
    }

    public void getUserList() {
        String sessionId = ClientSession.INSTANCE.getSessionId();
        if (sessionId == null) {
            log.error("세션에 접속되지 않은 상태로 사용자 목록 조회를 시도하였습니다.");
            return;
        }
        Message message = MessageBuilder.with(MessageType.USER_LIST)
                .sessionId(sessionId)
                .build();
        messageClient.send(message);
    }
}
