package com.nhnacademy.messenger.server.chat.service;

import com.nhnacademy.messenger.server.chat.domain.Chat;

public interface ChatService {
    Chat saveTextMessage(Long roomId, String senderId, String content);
}