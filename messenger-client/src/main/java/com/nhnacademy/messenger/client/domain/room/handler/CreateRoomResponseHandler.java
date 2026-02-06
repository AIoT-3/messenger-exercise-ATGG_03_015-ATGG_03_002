package com.nhnacademy.messenger.client.domain.room.handler;

import com.nhnacademy.messenger.client.domain.error.event.ErrorEvent;
import com.nhnacademy.messenger.client.domain.room.event.CreateRoomSuccessEvent;
import com.nhnacademy.messenger.client.network.ResponseHandler;
import com.nhnacademy.messenger.client.network.MessageHandler;
import com.nhnacademy.messenger.common.event.EventBus;
import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.message.data.room.CreateRoomResponse;
import com.nhnacademy.messenger.common.message.header.MessageType;
import com.nhnacademy.messenger.common.util.converter.MessageConverter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@MessageHandler(MessageType.CHAT_ROOM_CREATE_SUCCESS)
public class CreateRoomResponseHandler implements ResponseHandler {

    @Override
    public void handle(Message message) {
        try {
            CreateRoomResponse response = (CreateRoomResponse) MessageConverter.toData(message);

            if (response == null) {
                throw new IllegalStateException("채팅방 생성 응답 데이터가 비어있습니다.");
            }

            EventBus.INSTANCE.publish(new CreateRoomSuccessEvent(
                    response.roomId(),
                    response.roomName()
            ));

        } catch (Exception e) {
            EventBus.INSTANCE.publish(new ErrorEvent("채팅방 생성 응답 처리 중 오류가 발생했습니다."));
            log.error("채팅방 생성 응답 처리 중 오류 발생", e);
        }
    }
}
