package com.nhnacademy.messenger.server.chat.domain;

import com.nhnacademy.messenger.common.message.data.push.PushMessageType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Chat {
    private Long messageId;
    private Long roomId;
    private String senderId;
    private String content;
    private PushMessageType type;
    private LocalDateTime createdAt;

    // 파일 전송을 위한 필드 (Optional)
    private String fileName;
    private Long fileSize;

    // 일반 텍스트 메시지 생성자
    public static Chat text(Long roomId, String senderId, String content) {
        return new Chat(null, roomId, senderId, content,
                PushMessageType.TEXT, LocalDateTime.now(), null, 0L);
    }

    // 파일 메시지 생성자
    public static Chat file(Long roomId, String senderId, String content,
                            String fileName, Long fileSize) {
        return new Chat(null, roomId, senderId, content,
                PushMessageType.FILE, LocalDateTime.now(), fileName, fileSize);
    }

    // messageId 할당
    public void assignMessageId(Long messageId) {
        this.messageId = messageId;
    }
}