package com.nhnacademy.messenger.client.domain.chat.handler;

import com.nhnacademy.messenger.client.network.MessageHandler;
import com.nhnacademy.messenger.client.network.ResponseHandler;
import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.message.data.chat.PrivateChatResponse;
import com.nhnacademy.messenger.common.message.header.MessageType;
import com.nhnacademy.messenger.common.util.converter.MessageConverter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@MessageHandler(MessageType.PRIVATE_MESSAGE_SUCCESS)
public class PrivateChatResponseHandler implements ResponseHandler {
    @Override
    public void handle(Message message) {
        try {
            PrivateChatResponse response = (PrivateChatResponse) MessageConverter.toData(message);
            log.debug("귓속말 전송 성공 확인 (ACK): sender={}, receiver={}, messageId={}",
                    response.senderId(), response.receiverId(), response.messageId());
        } catch (Exception e) {
            log.warn("귓속말 응답 처리 중 오류 발생: {}", e.getMessage());
        }
    }
}
