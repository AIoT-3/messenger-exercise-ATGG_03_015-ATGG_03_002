package com.nhnacademy.messenger.client.domain.room.service;

import com.nhnacademy.messenger.client.network.MessageClient;
import com.nhnacademy.messenger.client.session.ClientSession;
import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.message.data.room.CreateRoomRequest;
import com.nhnacademy.messenger.common.message.header.MessageType;
import com.nhnacademy.messenger.common.message.header.RequestHeader;
import com.nhnacademy.messenger.common.util.converter.MessageConverter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChatRoomClientService {

    private final MessageClient messageClient;

    public void createRoom(String roomName) {
        CreateRoomRequest data = new CreateRoomRequest(roomName);
        RequestHeader header = RequestHeader.of(
                MessageType.CHAT_ROOM_CREATE,
                ClientSession.INSTANCE.getSessionId());
        Message message = new Message(header, MessageConverter.toJsonNode(data));

        messageClient.send(message);
    }
}
