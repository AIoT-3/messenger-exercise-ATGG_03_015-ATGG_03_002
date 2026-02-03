package com.nhnacademy.messenger.server.chat.service.impl;

import com.nhnacademy.messenger.server.chat.domain.Chat;
import com.nhnacademy.messenger.server.chat.repository.ChatRepository;
import com.nhnacademy.messenger.server.chat.service.ChatService;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.atomic.AtomicLong;

@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final AtomicLong messageIdCounter = new AtomicLong(System.currentTimeMillis());

    @Override
    public Chat saveTextMessage(Long roomId, String senderId, String content) {
        long messageId = messageIdCounter.incrementAndGet();
        Chat chat = Chat.text(messageId, roomId, senderId, content);
        chatRepository.save(chat);
        return chat;
    }
}