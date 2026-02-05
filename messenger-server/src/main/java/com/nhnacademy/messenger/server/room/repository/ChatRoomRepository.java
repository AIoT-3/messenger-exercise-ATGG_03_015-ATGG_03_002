package com.nhnacademy.messenger.server.room.repository;

import com.nhnacademy.messenger.server.room.domain.ChatRoom;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository {
    ChatRoom save(ChatRoom chatRoom);

    Optional<ChatRoom> findById(Long roomId);

    Optional<ChatRoom> findByName(String roomName);

    List<ChatRoom> findAll();

    boolean existsById(Long roomId);

    void deleteById(Long roomId);
}
