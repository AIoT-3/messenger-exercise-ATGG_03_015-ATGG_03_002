package com.nhnacademy.messenger.client.domain.room.service;

import com.nhnacademy.messenger.client.network.MessageClient;
import com.nhnacademy.messenger.client.session.ClientSession;
import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.message.data.room.CreateRoomRequest;
import com.nhnacademy.messenger.common.message.data.room.EnterRoomRequest;
import com.nhnacademy.messenger.common.message.header.MessageType;
import com.nhnacademy.messenger.common.message.header.RequestHeader;
import com.nhnacademy.messenger.common.util.converter.MessageConverter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChatRoomClientService {

    private final MessageClient messageClient;

    public void createRoom(String roomName) {
        String sessionId = ClientSession.INSTANCE.getSessionId();

        CreateRoomRequest data = new CreateRoomRequest(roomName);
        RequestHeader header = RequestHeader.of(MessageType.CHAT_ROOM_CREATE, sessionId);
        Message message = new Message(header, MessageConverter.toJsonNode(data));

        messageClient.send(message);
    }

    public void getRoomList() {
        String sessionId = ClientSession.INSTANCE.getSessionId();

        // 요청 데이터는 없음
        RequestHeader header = RequestHeader.of(MessageType.CHAT_ROOM_LIST, sessionId);
        Message message = new Message(header, null);

        messageClient.send(message);
    }

    public void enterRoom(long roomId) {
        String sessionId = ClientSession.INSTANCE.getSessionId();

        EnterRoomRequest data = new EnterRoomRequest(roomId);
        RequestHeader header = RequestHeader.of(MessageType.CHAT_ROOM_ENTER, sessionId);
        Message message = new Message(header, MessageConverter.toJsonNode(data));

        messageClient.send(message);
    }
}
