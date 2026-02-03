package com.nhnacademy.messenger.server.user.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.message.data.auth.LogoutResponse;
import com.nhnacademy.messenger.common.message.header.MessageType;
import com.nhnacademy.messenger.common.message.header.ResponseHeader;
import com.nhnacademy.messenger.common.util.converter.MessageConverter;
import com.nhnacademy.messenger.server.network.RequestHandler;
import com.nhnacademy.messenger.server.session.domain.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class LogoutRequestHandler implements RequestHandler {

    @Override
    public void handle(Session session, Message message) {
        session.validateLoggedIn();
        String userId = session.getUser().getUserId();

        // 1. 세션 로그아웃 처리 (메모리 해제 및 채팅방 퇴장)
        session.logout();

        // 2. 성공 응답 전송
        ResponseHeader header = ResponseHeader.success(MessageType.LOGOUT_SUCCESS);
        JsonNode data = MessageConverter.objectMapper.valueToTree(new LogoutResponse("로그아웃 되었습니다."));

        session.sendMessage(new Message(header, data));
        log.info("사용자 로그아웃 완료: {}", userId);
    }
}
