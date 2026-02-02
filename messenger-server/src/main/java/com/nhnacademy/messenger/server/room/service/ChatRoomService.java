package com.nhnacademy.messenger.server.room.service;

import com.nhnacademy.messenger.server.room.domain.ChatRoom;
import com.nhnacademy.messenger.server.session.domain.Session;

import java.util.List;

public interface ChatRoomService {
    ChatRoom createChatRoom(ChatRoom chatRoom);

    ChatRoom getChatRoomById(Long roomId);

    List<ChatRoom> getAllChatRooms();

    void enterChatRoom(Long roomId, Session session);

    void leaveChatRoom(Long roomId, Session session);
}
