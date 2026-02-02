package com.nhnacademy.messenger.server.room.handler;

import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.message.header.MessageType;
import com.nhnacademy.messenger.server.network.RequestHandler;
import com.nhnacademy.messenger.server.network.annotation.RequestMapping;
import com.nhnacademy.messenger.server.session.domain.Session;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping(type = MessageType.CHAT_ROOM_ENTER)
public class EnterRoomRequestHandler implements RequestHandler {

    @Override
    public void handle(Session session, Message message) {

    }
}
