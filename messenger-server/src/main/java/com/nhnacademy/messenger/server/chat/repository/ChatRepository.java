package com.nhnacademy.messenger.server.chat.repository;

import com.nhnacademy.messenger.server.chat.domain.Chat;

public interface ChatRepository {
    Chat save(Chat chat);
}