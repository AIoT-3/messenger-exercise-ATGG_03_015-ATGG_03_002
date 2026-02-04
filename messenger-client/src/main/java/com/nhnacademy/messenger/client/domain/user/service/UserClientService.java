package com.nhnacademy.messenger.client.domain.user.service;

import com.nhnacademy.messenger.client.network.MessageClient;
import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.message.data.auth.LoginRequest;
import com.nhnacademy.messenger.common.message.header.MessageType;
import com.nhnacademy.messenger.common.message.header.RequestHeader;
import com.nhnacademy.messenger.common.util.converter.MessageConverter;
import lombok.RequiredArgsConstructor;

import com.nhnacademy.messenger.client.session.ClientSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class UserClientService {

    private final MessageClient messageClient;

    public void login(String userId, String password) {
        LoginRequest data = new LoginRequest(userId, password);
        RequestHeader header = RequestHeader.login();
        Message message = new Message(header, MessageConverter.toJsonNode(data));
        
        messageClient.send(message);
    }

    public void logout() {
        String sessionId = ClientSession.INSTANCE.getSessionId();
        if (sessionId == null) {
            log.error("세션에 접속되지 않은 상태로 로그아웃을 시도하였습니다.");
            return;
        }
        RequestHeader header = RequestHeader.of(MessageType.LOGOUT, sessionId);
        Message message = new Message(header, null);

        messageClient.send(message);
    }

    public void getUserList() {
        String sessionId = ClientSession.INSTANCE.getSessionId();
        if (sessionId == null) {
            log.error("세션에 접속되지 않은 상태로 사용자 목록 조회를 시도하였습니다.");
            return;
        }
        RequestHeader header = RequestHeader.of(MessageType.USER_LIST, sessionId);
        Message message = new Message(header, null);
        messageClient.send(message);
    }
}
