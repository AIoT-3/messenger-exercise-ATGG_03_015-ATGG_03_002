package com.nhnacademy.messenger.server.chat.service.impl;

import com.nhnacademy.messenger.server.chat.domain.Chat;
import com.nhnacademy.messenger.server.chat.repository.ChatRepository;
import com.nhnacademy.messenger.server.chat.service.ChatService;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.atomic.AtomicLong;

@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;

    @Override
    public Chat saveTextMessage(Long roomId, String senderId, String content) {
        return chatRepository.save(Chat.text(roomId, senderId, content));
    }
}