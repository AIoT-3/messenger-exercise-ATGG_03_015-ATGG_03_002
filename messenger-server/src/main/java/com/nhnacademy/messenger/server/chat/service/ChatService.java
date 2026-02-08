package com.nhnacademy.messenger.server.chat.service;

import com.nhnacademy.messenger.server.chat.domain.Chat;

import java.util.List;

public interface ChatService {
    Chat saveTextMessage(Long roomId, String senderId, String content);
    Chat saveFileMessage(Long roomId, String senderId, String fileName, Long fileSize, String fileData);

    List<Chat> getChatHistory(Long roomId, int limit, Long beforeMessageId);
}