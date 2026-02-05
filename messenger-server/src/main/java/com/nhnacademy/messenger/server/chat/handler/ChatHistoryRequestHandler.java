package com.nhnacademy.messenger.server.chat.handler;

import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.message.data.chat.ChatHistoryRequest;
import com.nhnacademy.messenger.common.message.data.chat.ChatHistoryResponse;
import com.nhnacademy.messenger.common.message.data.chat.MessageInfo;
import com.nhnacademy.messenger.common.message.header.MessageType;
import com.nhnacademy.messenger.common.message.header.ResponseHeader;
import com.nhnacademy.messenger.common.util.converter.MessageConverter;
import com.nhnacademy.messenger.server.chat.domain.Chat;
import com.nhnacademy.messenger.server.chat.service.ChatService;
import com.nhnacademy.messenger.server.network.RequestHandler;
import com.nhnacademy.messenger.server.room.service.ChatRoomService;
import com.nhnacademy.messenger.server.session.domain.Session;
import com.nhnacademy.messenger.server.user.domain.User;
import com.nhnacademy.messenger.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class ChatHistoryRequestHandler implements RequestHandler {

    private final UserService userService;
    private final ChatService chatService;
    private final ChatRoomService chatRoomService;

    @Override
    public void handle(Session session, Message message) {

        // 1. 메시지 데이터 파싱
        ChatHistoryRequest requestData = (ChatHistoryRequest) MessageConverter.toData(message);
        log.debug("채팅 기록 조회 요청: roomId={}, limit={}, beforeMessageId={}",
                requestData.roomId(), requestData.limit(), requestData.beforeMessageId());

        // 2. 채팅방 존재 여부 확인
        chatRoomService.getChatRoomById(requestData.roomId());

        // 3. 요청 Limit 결정 (기본값 전략 사용)
        int clientLimit = requestData.getLimitOrDefault();

        // 4. Service 호출 (hasMore 판단을 위해 +1 요청)
        List<Chat> chats = chatService.getChatHistory(
                requestData.roomId(),
                clientLimit + 1,
                requestData.beforeMessageId()
        );

        // 5. hasMore 판단 및 데이터 자르기
        boolean hasMore = false;
        if (chats.size() > clientLimit) {
            hasMore = true;
            chats = chats.subList(0, clientLimit);
        }

        // 6. 응답 DTO 변환
        List<MessageInfo> messageInfos = new ArrayList<>(chats.stream()
                .map(chat -> {
                    String senderName = userService.findById(chat.getSenderId())
                            .map(User::getUserName)
                            .orElse("[알 수 없음] " + chat.getSenderId());

                    return new MessageInfo(
                            chat.getMessageId(),
                            chat.getSenderId(),
                            senderName,
                            chat.getCreatedAt(),
                            chat.getContent()
                    );
                })
                .toList());

        // 7. 과거 -> 최신 순으로 정렬 뒤집기
        Collections.reverse(messageInfos);

        // 8. 응답 전송
        ChatHistoryResponse responseData = new ChatHistoryResponse(
                requestData.roomId(),
                messageInfos,
                hasMore
        );

        session.sendMessage(new Message(
                ResponseHeader.success(MessageType.CHAT_MESSAGE_HISTORY_SUCCESS),
                MessageConverter.toJsonNode(responseData)
        ));
    }
}
