package com.nhnacademy.messenger.client.service.handler;

import com.nhnacademy.messenger.common.message.data.MessageData;

/**
 * 특정 MessageType에 해당하는 MessageData를 처리하는 핸들러 인터페이스
 */
@FunctionalInterface
public interface FunctionalMessageHandler {
    void handle(MessageData messageData);
}
