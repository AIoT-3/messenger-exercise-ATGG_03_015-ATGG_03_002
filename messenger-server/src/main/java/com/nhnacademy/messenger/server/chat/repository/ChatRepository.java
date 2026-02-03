package com.nhnacademy.messenger.server.chat.repository;

import com.nhnacademy.messenger.server.chat.domain.Chat;

public interface ChatRepository {
    void save(Chat chat);
}