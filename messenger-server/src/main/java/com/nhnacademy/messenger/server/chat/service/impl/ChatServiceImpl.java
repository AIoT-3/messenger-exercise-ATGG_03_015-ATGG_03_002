package com.nhnacademy.messenger.server.chat.service.impl;

import com.nhnacademy.messenger.common.exception.MessengerException;
import com.nhnacademy.messenger.common.message.data.chat.ChatHistoryRequest;
import com.nhnacademy.messenger.common.message.data.error.ErrorCode;
import com.nhnacademy.messenger.server.chat.domain.Chat;
import com.nhnacademy.messenger.server.chat.repository.ChatRepository;
import com.nhnacademy.messenger.server.chat.service.ChatService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;

    @Override
    public Chat saveTextMessage(Long roomId, String senderId, String content) {
        return chatRepository.save(Chat.text(roomId, senderId, content));
    }

    @Override
    public List<Chat> getChatHistory(Long roomId, int limit, Long beforeMessageId) {

        // 1. roomId 필수 체크
        if (Objects.isNull(roomId)) {
            throw new MessengerException(ErrorCode.REQUEST_INVALID_MESSAGE, "RoomId는 필수입니다.");
        }

        // 2. limit 최대값 처리 (hasMore 판단을 위한 MAX_LIMIT + 1)
        int effectiveLimit = Math.min(limit, ChatHistoryRequest.MAX_LIMIT + 1);

        // 3. 채팅 기록 조회
        return chatRepository.findByRoomIdBeforeMessageId(roomId, effectiveLimit, beforeMessageId);
    }
}