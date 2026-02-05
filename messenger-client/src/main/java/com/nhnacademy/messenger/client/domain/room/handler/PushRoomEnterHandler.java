package com.nhnacademy.messenger.client.domain.room.handler;

import com.nhnacademy.messenger.client.domain.room.event.PushRoomEnterEvent;
import com.nhnacademy.messenger.client.network.ResponseHandler;
import com.nhnacademy.messenger.common.event.EventBus;
import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.message.data.push.PushRoomEnter;
import com.nhnacademy.messenger.common.util.converter.MessageConverter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PushRoomEnterHandler implements ResponseHandler {
    @Override
    public void handle(Message message) {
        try {
            PushRoomEnter data = (PushRoomEnter) MessageConverter.toData(message);
            EventBus.INSTANCE.publish(new PushRoomEnterEvent(
                    data.roomId(),
                    data.userId(),
                    data.userName()
            ));
        } catch (Exception e) {
            log.error("채팅방 입장 알림 처리 실패", e);
        }
    }
}
