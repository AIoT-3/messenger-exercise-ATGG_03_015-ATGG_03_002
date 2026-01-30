package com.nhnacademy.messenger.server.message.handler;

import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.message.header.MessageType;
import com.nhnacademy.messenger.server.session.domain.Session;

public interface MessageHandler {
    MessageType getType();
    void handle(Session session, Message message);
}
