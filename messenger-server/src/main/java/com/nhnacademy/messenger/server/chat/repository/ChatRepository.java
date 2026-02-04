package com.nhnacademy.messenger.server.chat.repository;

import com.nhnacademy.messenger.server.chat.domain.Chat;

import java.util.List;

public interface ChatRepository {
    Chat save(Chat chat);

    List<Chat> findByRoomIdBeforeMessageId(Long roomId, int limit, Long beforeMessageId);
}