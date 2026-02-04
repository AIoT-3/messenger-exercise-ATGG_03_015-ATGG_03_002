package com.nhnacademy.messenger.server.chat.repository.impl;

import com.nhnacademy.messenger.server.chat.domain.Chat;
import com.nhnacademy.messenger.server.chat.repository.ChatRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryChatRepository implements ChatRepository {

    // Key: roomId, Value: List<Chat>
    private final Map<Long, List<Chat>> chatStore = new ConcurrentHashMap<>();
    private final AtomicLong chatIdGenerator = new AtomicLong(0);

    @Override
    public Chat save(Chat chat) {
        if (Objects.isNull(chat.getMessageId())) {
            chat.assignMessageId(chatIdGenerator.incrementAndGet());
        }
        chatStore.computeIfAbsent(chat.getRoomId(), k -> Collections.synchronizedList(new ArrayList<>()))
                .add(chat);
        return chat;
    }
}