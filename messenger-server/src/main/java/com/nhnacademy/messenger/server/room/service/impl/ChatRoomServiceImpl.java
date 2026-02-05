package com.nhnacademy.messenger.server.room.service.impl;

import com.nhnacademy.messenger.common.event.EventListener;
import com.nhnacademy.messenger.common.exception.MessengerException;
import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.message.data.push.PushRoomEnter;
import com.nhnacademy.messenger.common.message.data.push.PushRoomExit;
import com.nhnacademy.messenger.common.message.header.MessageType;
import com.nhnacademy.messenger.common.message.header.ResponseHeader;
import com.nhnacademy.messenger.common.util.converter.MessageConverter;
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
        chatRoom.addSession(session);
        session.joinRoom(roomId);

        // 입장 알림 브로드캐스트
        PushRoomEnter pushData = new PushRoomEnter(
                roomId,
                session.getUser().getUserId(),
                session.getUser().getUserName()
        );
        Message pushMessage = new Message(
                ResponseHeader.success(MessageType.PUSH_ROOM_ENTER),
                MessageConverter.toJsonNode(pushData)
        );
        chatRoom.broadcast(pushMessage);
    }

    @Override
    public void leaveChatRoom(Long roomId, Session session) {
        session.validateLoggedIn();

        ChatRoom chatRoom = getChatRoomById(roomId);

        // 퇴장 알림 브로드캐스트
        PushRoomExit pushData = new PushRoomExit(roomId, session.getUser().getUserId());
        Message pushMessage = new Message(
                ResponseHeader.success(MessageType.PUSH_ROOM_EXIT),
                MessageConverter.toJsonNode(pushData)
        );
        chatRoom.broadcast(pushMessage);

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
                // 연결 끊김 시에도 퇴장 알림 브로드캐스트
                PushRoomExit pushData = new PushRoomExit(roomId, session.getUser().getUserId());
                Message pushMessage = new Message(
                        ResponseHeader.success(MessageType.PUSH_ROOM_EXIT),
                        MessageConverter.toJsonNode(pushData)
                );
                chatRoom.broadcast(pushMessage);

                chatRoom.removeSession(session);
                if (chatRoom.getSessions().isEmpty()) {
                    chatRoomRepository.deleteById(roomId);
                }
            });
        });
    }
}
