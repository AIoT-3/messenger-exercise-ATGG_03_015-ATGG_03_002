package com.nhnacademy.messenger.server.chat.handler;

import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.message.MessageBuilder;
import com.nhnacademy.messenger.common.message.data.chat.ChatRequest;
import com.nhnacademy.messenger.common.message.data.chat.ChatResponse;
import com.nhnacademy.messenger.common.message.data.push.PushNewMessage;
import com.nhnacademy.messenger.common.util.converter.MessageConverter;
import com.nhnacademy.messenger.server.chat.domain.Chat;
import com.nhnacademy.messenger.server.chat.service.ChatService;
import com.nhnacademy.messenger.server.network.RequestHandler;
import com.nhnacademy.messenger.server.room.domain.ChatRoom;
import com.nhnacademy.messenger.server.room.service.ChatRoomService;
import com.nhnacademy.messenger.server.session.domain.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.nhnacademy.messenger.common.message.header.MessageType.CHAT_MESSAGE_SUCCESS;
import static com.nhnacademy.messenger.common.message.header.MessageType.PUSH_NEW_MESSAGE;

@Slf4j
@RequiredArgsConstructor
public class ChatRequestHandler implements RequestHandler {

    private final ChatService chatService;
    private final ChatRoomService chatRoomService;

    @Override
    public void handle(Session session, Message message) {

        // 1. 메시지 데이터 파싱
        ChatRequest request = (ChatRequest) MessageConverter.toData(message);
        Long roomId = request.roomId();
        String content = request.message();
        String senderId = session.getUser().getUserId();
        log.debug("채팅 메시지 요청: session={}, roomId={}", session.getId(), roomId);

        // 2. 메시지 저장
        Chat chat = chatService.saveTextMessage(roomId, senderId, content);

        // 3. 채팅방 조회 및 브로드캐스트
        ChatRoom room = chatRoomService.getChatRoomById(roomId);

        PushNewMessage pushData = new PushNewMessage(
                chat.getRoomId(),
                chat.getMessageId(),
                chat.getSenderId(),
                chat.getContent(),
                chat.getType(),
                chat.getFileName(),
                chat.getFileSize()
        );

        Message pushMessage = MessageBuilder.with(PUSH_NEW_MESSAGE)
                .success(true)
                .data(pushData)
                .build();

        room.broadcast(pushMessage);

        // 4. 클라이언트에 성공 응답 전송
        ChatResponse responseData = new ChatResponse(roomId, chat.getMessageId());
        session.sendMessage(MessageBuilder.with(CHAT_MESSAGE_SUCCESS)
                .success(true)
                .data(responseData)
                .build());

        log.debug("채팅 메시지 처리 완료: roomId={}, messageId={}", roomId, chat.getMessageId());
    }
}
