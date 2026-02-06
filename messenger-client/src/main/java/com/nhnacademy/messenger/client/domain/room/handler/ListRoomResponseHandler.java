package com.nhnacademy.messenger.client.domain.room.handler;

import com.nhnacademy.messenger.client.domain.error.event.ErrorEvent;
import com.nhnacademy.messenger.client.domain.room.event.ListRoomSuccessEvent;
import com.nhnacademy.messenger.client.network.ResponseHandler;
import com.nhnacademy.messenger.client.network.MessageHandler;
import com.nhnacademy.messenger.common.event.EventBus;
import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.message.data.room.ListRoomResponse;
import com.nhnacademy.messenger.common.message.header.MessageType;
import com.nhnacademy.messenger.common.util.converter.MessageConverter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@MessageHandler(MessageType.CHAT_ROOM_LIST_SUCCESS)
public class ListRoomResponseHandler implements ResponseHandler {

    @Override
    public void handle(Message message) {
        try {
            ListRoomResponse response = (ListRoomResponse) MessageConverter.toData(message);
            if (response == null) {
                throw new IllegalStateException("응답 데이터가 비어있습니다.");
            }

            EventBus.INSTANCE.publish(new ListRoomSuccessEvent(response.rooms()));

        } catch (Exception e) {
            log.error("채팅방 목록 처리 중 오류", e);
            EventBus.INSTANCE.publish(new ErrorEvent("채팅방 목록을 불러오지 못했습니다."));
        }
    }
}
