package com.nhnacademy.messenger.server.room.repository.impl;

import com.nhnacademy.messenger.server.room.domain.ChatRoom;
import com.nhnacademy.messenger.server.room.repository.ChatRoomRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryChatRoomRepository implements ChatRoomRepository {

    private final AtomicLong roomIdGenerator = new AtomicLong(0);
    private final Map<Long, ChatRoom> chatRoomMap = new ConcurrentHashMap<>();

    @Override
    public ChatRoom save(ChatRoom chatRoom) {
        if (Objects.isNull(chatRoom.getRoomId())) {
            chatRoom.assignRoomId(roomIdGenerator.incrementAndGet());
        }
        chatRoomMap.put(chatRoom.getRoomId(), chatRoom);
        return chatRoom;
    }

    @Override
    public Optional<ChatRoom> findById(Long roomId) {
        return Optional.ofNullable(chatRoomMap.get(roomId));
    }

    @Override
    public List<ChatRoom> findAll() {
        return new ArrayList<>(chatRoomMap.values());
    }

    @Override
    public boolean existsById(Long roomId) {
        return chatRoomMap.containsKey(roomId);
    }

    @Override
    public void deleteById(Long roomId) {
        chatRoomMap.remove(roomId);
    }
}
