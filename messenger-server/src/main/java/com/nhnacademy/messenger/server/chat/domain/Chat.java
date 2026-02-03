package com.nhnacademy.messenger.server.chat.domain;

import com.nhnacademy.messenger.common.message.data.push.PushMessageType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class Chat {
    private final Long messageId;
    private final Long roomId;
    private final String senderId;
    private final String content;
    private final PushMessageType type;
    private final LocalDateTime createdAt;

    // 파일 전송을 위한 필드 (Optional)
    private final String fileName;
    private final Long fileSize;

    // 일반 텍스트 메시지 생성자
    public static Chat text(Long messageId, Long roomId, String senderId, String content) {
        return new Chat(messageId, roomId, senderId, content,
                PushMessageType.TEXT, LocalDateTime.now(), null, 0L);
    }
}