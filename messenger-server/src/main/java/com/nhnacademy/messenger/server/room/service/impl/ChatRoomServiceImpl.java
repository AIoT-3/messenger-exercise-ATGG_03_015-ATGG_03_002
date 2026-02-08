package com.nhnacademy.messenger.server.room.service.impl;

import com.nhnacademy.messenger.common.event.EventListener;
import com.nhnacademy.messenger.common.exception.MessengerException;
import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.message.MessageBuilder;
import com.nhnacademy.messenger.common.message.data.push.PushRoomEnter;
import com.nhnacademy.messenger.common.message.data.push.PushRoomExit;
import com.nhnacademy.messenger.common.message.header.MessageType;
import com.nhnacademy.messenger.server.room.domain.ChatRoom;
import com.nhnacademy.messenger.server.room.repository.ChatRoomRepository;
import com.nhnacademy.messenger.server.room.service.ChatRoomService;
import com.nhnacademy.messenger.server.session.domain.Session;
import com.nhnacademy.messenger.server.session.event.SessionDisconnectedEvent;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.nhnacademy.messenger.common.message.data.error.ErrorCode.*;

@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    @Override
    public ChatRoom createChatRoom(ChatRoom chatRoom) {
        if (chatRoomRepository.findByName(chatRoom.getRoomName()).isPresent()) {
            throw new MessengerException(ROOM_ALREADY_EXISTS, "이미 존재하는 채팅방입니다.");
        }
        return chatRoomRepository.save(chatRoom);
    }

    @Override
    public ChatRoom getChatRoomById(Long roomId) {
        return chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new MessengerException(ROOM_NOT_FOUND, "채팅방을 찾을 수 없습니다: " + roomId));
    }

    @Override
    public List<ChatRoom> getAllChatRooms() {
        return chatRoomRepository.findAll();
    }

    @Override
    public void enterChatRoom(Long roomId, Session session) {
        session.validateLoggedIn();

        ChatRoom chatRoom = getChatRoomById(roomId);

        // 입장 알림 브로드캐스트 (세션 추가 전에 수행하여 본인 제외)
        PushRoomEnter pushData = new PushRoomEnter(
                roomId,
                session.getUser().getUserId(),
                session.getUser().getUserName()
        );
        Message pushMessage = MessageBuilder.with(MessageType.PUSH_ROOM_ENTER)
                .success(true)
                .data(pushData)
                .build();
        chatRoom.broadcast(pushMessage);

        chatRoom.addSession(session);
        session.joinRoom(roomId);
    }

    @Override
    public void leaveChatRoom(Long roomId, Session session) {
        // 유저 정보가 있을 때만 퇴장 알림 전송 (onSessionDisconnected 등에서 안전하게 호출하기 위함)
        String userId = (session.getUser() != null) ? session.getUser().getUserId() : null;

        ChatRoom chatRoom = getChatRoomById(roomId);

        chatRoom.removeSession(session);
        session.leaveRoom(roomId);

        // 퇴장 알림 브로드캐스트 (나간 사람 제외하고 남은 사람들에게만 전송)
        if (userId != null) {
            PushRoomExit pushData = new PushRoomExit(roomId, userId);
            Message pushMessage = MessageBuilder.with(MessageType.PUSH_ROOM_EXIT)
                    .success(true)
                    .data(pushData)
                    .build();
            chatRoom.broadcast(pushMessage);
        }

        if (chatRoom.getSessions().isEmpty()) {
            chatRoomRepository.deleteById(roomId);
        }
    }

    @EventListener
    public void onSessionDisconnected(SessionDisconnectedEvent event) {
        Session session = event.session();
        // ConcurrentModificationException 방지를 위해 복사본 순회
        new java.util.ArrayList<>(session.getJoinedRoomIds()).forEach(roomId -> {
            try {
                leaveChatRoom(roomId, session);
            } catch (Exception e) {
                // 방이 이미 삭제되었거나 다른 이유로 실패한 경우 무시
            }
        });
    }
}
