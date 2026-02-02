package com.nhnacademy.messenger.server.room.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.message.data.room.CreateRoomRequest;
import com.nhnacademy.messenger.common.message.data.room.CreateRoomResponse;
import com.nhnacademy.messenger.common.message.header.MessageType;
import com.nhnacademy.messenger.common.message.header.ResponseHeader;
import com.nhnacademy.messenger.common.util.converter.MessageConverter;
import com.nhnacademy.messenger.server.network.RequestHandler;
import com.nhnacademy.messenger.server.room.domain.ChatRoom;
import com.nhnacademy.messenger.server.room.service.ChatRoomService;
import com.nhnacademy.messenger.server.session.domain.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CreateRoomRequestHandler implements RequestHandler {

    private final ChatRoomService chatRoomService;

    @Override
    public void handle(Session session, Message message) {
        // 1. 요청 데이터 파싱
        CreateRoomRequest requestData = (CreateRoomRequest) MessageConverter.toData(message);
        log.debug("채팅방 생성 요청 수신: roomName={}, session={}", requestData.roomName(), session.getId());

        // 2. 방 생성
        ChatRoom newRoom = new ChatRoom(null, requestData.roomName());
        ChatRoom savedRoom = chatRoomService.createChatRoom(newRoom);

        // 3. 성공 응답 구성
        ResponseHeader header = ResponseHeader.success(MessageType.CHAT_ROOM_CREATE_SUCCESS);
        CreateRoomResponse responseData = new CreateRoomResponse(savedRoom.getRoomId(), savedRoom.getRoomName());
        JsonNode data = MessageConverter.objectMapper.valueToTree(responseData);

        // 4. 메시지 전송
        session.sendMessage(new Message(header, data));
        log.debug("채팅방 생성 완료: roomId={}, roomName={}", savedRoom.getRoomId(), savedRoom.getRoomName());
    }
}

