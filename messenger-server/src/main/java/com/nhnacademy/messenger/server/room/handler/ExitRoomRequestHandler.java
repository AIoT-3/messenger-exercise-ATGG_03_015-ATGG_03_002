package com.nhnacademy.messenger.server.room.handler;

import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.message.MessageBuilder;
import com.nhnacademy.messenger.common.message.data.room.ExitRoomRequest;
import com.nhnacademy.messenger.common.message.data.room.ExitRoomResponse;
import com.nhnacademy.messenger.common.message.header.MessageType;
import com.nhnacademy.messenger.common.util.converter.MessageConverter;
import com.nhnacademy.messenger.server.network.RequestHandler;
import com.nhnacademy.messenger.server.room.service.ChatRoomService;
import com.nhnacademy.messenger.server.session.domain.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ExitRoomRequestHandler implements RequestHandler {

    private final ChatRoomService chatRoomService;

    @Override
    public void handle(Session session, Message message) {
        ExitRoomRequest request = (ExitRoomRequest) MessageConverter.toData(message);
        Long roomId = request.roomId();

        log.debug("채팅방 퇴장 요청: session={}, roomId={}", session.getId(), roomId);

        chatRoomService.leaveChatRoom(roomId, session);

        ExitRoomResponse responseData = new ExitRoomResponse(roomId, "채팅방에서 나갔습니다.");

        session.sendMessage(MessageBuilder.with(MessageType.CHAT_ROOM_EXIT_SUCCESS)
                .success(true)
                .data(responseData)
                .build());
        log.debug("채팅방 퇴장 성공 응답 전송: session={}, roomId={}", session.getId(), roomId);
    }
}