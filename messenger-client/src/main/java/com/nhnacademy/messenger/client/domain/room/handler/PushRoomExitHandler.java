package com.nhnacademy.messenger.client.domain.room.handler;

import com.nhnacademy.messenger.client.domain.room.event.PushRoomExitEvent;
import com.nhnacademy.messenger.client.network.ResponseHandler;
import com.nhnacademy.messenger.common.event.EventBus;
import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.message.data.push.PushRoomExit;
import com.nhnacademy.messenger.common.util.converter.MessageConverter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PushRoomExitHandler implements ResponseHandler {
    @Override
    public void handle(Message message) {
        try {
            PushRoomExit data = (PushRoomExit) MessageConverter.toData(message);
            EventBus.INSTANCE.publish(new PushRoomExitEvent(
                    data.roomId(),
                    data.userId()
            ));
        } catch (Exception e) {
            log.error("채팅방 퇴장 알림 처리 실패", e);
        }
    }
}
