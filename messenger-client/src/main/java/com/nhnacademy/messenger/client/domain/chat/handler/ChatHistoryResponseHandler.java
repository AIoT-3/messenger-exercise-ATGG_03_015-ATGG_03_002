package com.nhnacademy.messenger.client.domain.chat.handler;

import com.nhnacademy.messenger.client.domain.chat.event.ChatHistoryResponseEvent;
import com.nhnacademy.messenger.client.network.ResponseHandler;
import com.nhnacademy.messenger.common.event.EventBus;
import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.message.data.chat.ChatHistoryResponse;
import com.nhnacademy.messenger.common.util.converter.MessageConverter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChatHistoryResponseHandler implements ResponseHandler {

    @Override
    public void handle(Message message) {
        try {
            ChatHistoryResponse response = (ChatHistoryResponse) MessageConverter.toData(message);
            EventBus.INSTANCE.publish(new ChatHistoryResponseEvent(
                    response.roomId(),
                    response.messages(),
                    response.hasMore()
            ));
        } catch (Exception e) {
            log.error("채팅 히스토리 처리 중 오류 발생", e);
        }
    }
}
