package com.nhnacademy.messenger.client.domain.chat.handler;

import com.nhnacademy.messenger.client.network.MessageHandler;
import com.nhnacademy.messenger.client.network.ResponseHandler;
import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.message.data.chat.ChatResponse;
import com.nhnacademy.messenger.common.message.header.MessageType;
import com.nhnacademy.messenger.common.util.converter.MessageConverter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@MessageHandler(MessageType.CHAT_MESSAGE_SUCCESS)
public class ChatResponseHandler implements ResponseHandler {

    @Override
    public void handle(Message message) {
        try {
            // 지금은 ACK 용도로만 두고.. 나중에 쓸 일이 있을지도?
            ChatResponse response = (ChatResponse) MessageConverter.toData(message);
            log.debug("메시지 전송 성공 확인 (ACK): roomId={}, messageId={}",
                    response.roomId(), response.messageId());
        } catch (Exception e) {
            log.warn("채팅 응답 처리 중 오류 발생: {}", e.getMessage());
        }
    }
}
