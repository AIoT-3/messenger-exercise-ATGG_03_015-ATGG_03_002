package com.nhnacademy.messenger.server.room.handler;

import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.message.data.room.EnterRoomRequest;
import com.nhnacademy.messenger.common.message.data.room.EnterRoomResponse;
import com.nhnacademy.messenger.common.message.header.ResponseHeader;
import com.nhnacademy.messenger.common.util.converter.MessageConverter;
import com.nhnacademy.messenger.server.network.RequestHandler;
import com.nhnacademy.messenger.server.room.domain.ChatRoom;
import com.nhnacademy.messenger.server.room.service.ChatRoomService;
import com.nhnacademy.messenger.server.session.domain.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.nhnacademy.messenger.common.message.header.MessageType.CHAT_ROOM_ENTER_SUCCESS;

@Slf4j
@RequiredArgsConstructor
public class EnterRoomRequestHandler implements RequestHandler {

    private final ChatRoomService chatRoomService;

    @Override
    public void handle(Session session, Message message) {
        EnterRoomRequest request = (EnterRoomRequest) MessageConverter.toData(message);
        Long roomId = request.roomId();

        log.debug("채팅방 입장 요청: session={}, roomId={}", session.getId(), roomId);

        // 1. 서비스 호출 (입장 처리)
        chatRoomService.enterChatRoom(roomId, session);

        // 2. 방 정보 조회 (참여자 목록 포함)
        ChatRoom room = chatRoomService.getChatRoomById(roomId);
        List<String> userIds = room.getSessions().stream()
                .map(s -> s.getUser().getUserId())
                .toList();

        // 3. 응답 전송
        EnterRoomResponse responseData = new EnterRoomResponse(roomId, userIds);
        ResponseHeader header = ResponseHeader.success(CHAT_ROOM_ENTER_SUCCESS);

        session.sendMessage(new Message(header, MessageConverter.toJsonNode(responseData)));
        log.debug("채팅방 입장 성공 응답 전송: session={}, roomId={}", session.getId(), roomId);
    }
}
