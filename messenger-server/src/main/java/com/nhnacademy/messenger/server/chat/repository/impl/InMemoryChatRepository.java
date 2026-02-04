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

    @Override
    public List<Chat> findByRoomIdBeforeMessageId(Long roomId, int limit, Long beforeMessageId) {
        List<Chat> chats = chatStore.getOrDefault(roomId, Collections.emptyList());
        if (chats.isEmpty()) {
            return Collections.emptyList();
        }

        List<Chat> result = new ArrayList<>();

        synchronized (chats) {
            int index = chats.size() - 1;

            if (beforeMessageId != null) {
                while (index >= 0 && chats.get(index).getMessageId() >= beforeMessageId) {
                    index--;
                }
            }

            while (index >= 0 && result.size() < limit) {
                result.add(chats.get(index));
                index--;
            }
        }

        return result;
    }
}
