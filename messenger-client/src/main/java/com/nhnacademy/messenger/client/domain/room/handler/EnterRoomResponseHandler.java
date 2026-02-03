package com.nhnacademy.messenger.client.domain.room.handler;

import com.nhnacademy.messenger.client.domain.error.event.ErrorEvent;
import com.nhnacademy.messenger.client.domain.room.event.EnterRoomSuccessEvent;
import com.nhnacademy.messenger.client.network.ResponseHandler;
import com.nhnacademy.messenger.common.event.EventBus;
import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.message.data.room.EnterRoomResponse;
import com.nhnacademy.messenger.common.util.converter.MessageConverter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EnterRoomResponseHandler implements ResponseHandler {

    @Override
    public void handle(Message message) {
        try {
            EnterRoomResponse response = (EnterRoomResponse) MessageConverter.toData(message);
            if (response == null) {
                throw new IllegalStateException("입장 응답 데이터가 비어있습니다.");
            }

            EventBus.INSTANCE.publish(new EnterRoomSuccessEvent(response.roomId(), response.users()));

        } catch (Exception e) {
            log.error("채팅방 입장 처리 중 오류", e);
            EventBus.INSTANCE.publish(new ErrorEvent("채팅방 입장에 실패했습니다."));
        }
    }
}
