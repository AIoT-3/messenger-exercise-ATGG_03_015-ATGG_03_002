package com.nhnacademy.messenger.server.message.handler;

import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.message.header.MessageType;
import com.nhnacademy.messenger.server.session.domain.Session;

/**
 * Message Handler
 * 역할
 * 1. request 검증, 변환
 * 2. request 처리(비즈니스 로직 수행)
 * 3. response 생성
 */
public interface MessageHandler {
    MessageType getType();
    void handle(Session session, Message message);
}
