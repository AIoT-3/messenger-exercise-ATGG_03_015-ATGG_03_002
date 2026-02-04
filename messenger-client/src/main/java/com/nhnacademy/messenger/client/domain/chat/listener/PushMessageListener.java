package com.nhnacademy.messenger.client.domain.chat.listener;

import com.nhnacademy.messenger.client.domain.chat.event.ReceiveMessageEvent;
import com.nhnacademy.messenger.client.domain.chat.event.ReceivePrivateMessageEvent;
import com.nhnacademy.messenger.client.network.ResponseHandler;
import com.nhnacademy.messenger.common.event.EventBus;
import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.message.data.push.PushNewMessage;
import com.nhnacademy.messenger.common.util.converter.MessageConverter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PushMessageListener implements ResponseHandler {
    // TODO: 리스너와 액션 리스너의 이름 관계가 모호해서 추후에 다른 리스너들 이름 리펙터하기
    @Override
    public void handle(Message message) {
        try {
            PushNewMessage data = (PushNewMessage) MessageConverter.toData(message);

            // 귓속말 roomId = -1
            if (data.roomId() != null && data.roomId() == -1L) {
                EventBus.INSTANCE.publish(new ReceivePrivateMessageEvent(
                        data.senderId(),
                        data.content()
                ));
            } else {
                // 일반 채팅
                EventBus.INSTANCE.publish(new ReceiveMessageEvent(
                        data.roomId(),
                        data.messageId(),
                        data.senderId(),
                        data.content()
                ));
            }

        } catch (Exception e) {
            log.error("메시지 수신 처리 실패", e);
        }
    }
}
