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

import java.util.ArrayList;
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

        chatRoom.removeSession(session);
        session.leaveRoom(roomId);

        // 퇴장 알림 브로드캐스트 (나간 사람 제외하고 남은 사람들에게만 전송)
        PushRoomExit pushData = new PushRoomExit(roomId, session.getUser().getUserId());
        Message pushMessage = new Message(
                ResponseHeader.success(MessageType.PUSH_ROOM_EXIT),
                MessageConverter.toJsonNode(pushData)
        );
        chatRoom.broadcast(pushMessage);

        if (chatRoom.getSessions().isEmpty()) {
            chatRoomRepository.deleteById(roomId);
        }
    }

    // TODO: 별도의 handler로 분리 고려
    @EventListener
    public void onSessionDisconnected(SessionDisconnectedEvent event) {
        Session session = event.session();
        // 복사본 순회: leaveChatRoom 호출 시 session의 joinedRoomIds가 변경될 수 있으므로
        List<Long> joinedRooms = new ArrayList<>(session.getJoinedRoomIds());

        for (Long roomId : joinedRooms) {
            try {
                leaveChatRoom(roomId, session);
            } catch (Exception e) {
                // 이미 방이 삭제되었거나 다른 세션에 의해 처리된 경우 등 예외 무시
            }
        }
    }
}