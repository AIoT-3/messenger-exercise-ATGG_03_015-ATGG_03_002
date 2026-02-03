package com.nhnacademy.messenger.server.room.service.impl;

import com.nhnacademy.messenger.common.event.EventListener;
import com.nhnacademy.messenger.common.exception.MessengerException;
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
        chatRoom.addSession(session);
        session.joinRoom(roomId);
    }

    @Override
    public void leaveChatRoom(Long roomId, Session session) {
        session.validateLoggedIn();

        ChatRoom chatRoom = getChatRoomById(roomId);
        chatRoom.removeSession(session);
        session.leaveRoom(roomId);
        if (chatRoom.getSessions().isEmpty()) {
            chatRoomRepository.deleteById(roomId);
        }
    }

    @EventListener
    public void onSessionDisconnected(SessionDisconnectedEvent event) {
        Session session = event.session();
        session.getJoinedRoomIds().forEach(roomId -> {
            chatRoomRepository.findById(roomId).ifPresent(chatRoom -> {
                chatRoom.removeSession(session);
                if (chatRoom.getSessions().isEmpty()) {
                    chatRoomRepository.deleteById(roomId);
                }
            });
        });
    }
}
