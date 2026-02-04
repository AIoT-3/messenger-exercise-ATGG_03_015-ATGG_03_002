package com.nhnacademy.messenger.server.chat.repository.impl;

import com.nhnacademy.messenger.server.chat.domain.Chat;
import com.nhnacademy.messenger.server.chat.repository.ChatRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryChatRepository implements ChatRepository {

    // Key: roomId, Value: List<Chat>
    private final Map<Long, List<Chat>> chatStore = new ConcurrentHashMap<>();

    @Override
    public void save(Chat chat) {
        chatStore.computeIfAbsent(chat.getRoomId(), k -> Collections.synchronizedList(new ArrayList<>()))
                .add(chat);
    }
}