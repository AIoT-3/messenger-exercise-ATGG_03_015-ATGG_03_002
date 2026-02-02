package com.nhnacademy.messenger.server.network;

import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.server.session.domain.Session;

public interface RequestHandler {
    void handle(Session session, Message message);
}
