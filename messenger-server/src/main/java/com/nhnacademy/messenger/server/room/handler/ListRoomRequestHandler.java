package com.nhnacademy.messenger.server.room.handler;

import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.message.data.room.ListRoomResponse;
import com.nhnacademy.messenger.common.message.data.room.RoomInfo;
import com.nhnacademy.messenger.common.message.header.MessageType;
import com.nhnacademy.messenger.common.message.header.ResponseHeader;
import com.nhnacademy.messenger.common.util.converter.MessageConverter;
import com.nhnacademy.messenger.server.network.RequestHandler;
import com.nhnacademy.messenger.server.room.domain.ChatRoom;
import com.nhnacademy.messenger.server.room.service.ChatRoomService;
import com.nhnacademy.messenger.server.session.domain.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class ListRoomRequestHandler implements RequestHandler {

    private final ChatRoomService chatRoomService;

    @Override
    public void handle(Session session, Message message) {
        log.debug("채팅방 목록 조회 요청 수신: session={}", session.getId());

        List<ChatRoom> chatRooms = chatRoomService.getAllChatRooms();

        List<RoomInfo> roomInfos = chatRooms.stream()
                .map(room -> new RoomInfo(
                        room.getRoomId(),
                        room.getRoomName(),
                        room.getSessions().size()
                ))
                .toList();

        ResponseHeader header = ResponseHeader.success(MessageType.CHAT_ROOM_LIST_SUCCESS);
        ListRoomResponse responseData = new ListRoomResponse(roomInfos);

        session.sendMessage(new Message(header, MessageConverter.toJsonNode(responseData)));
        log.debug("채팅방 목록 전송 완료: 개수={}", roomInfos.size());
    }
}
