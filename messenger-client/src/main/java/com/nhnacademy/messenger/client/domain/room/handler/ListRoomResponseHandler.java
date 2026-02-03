package com.nhnacademy.messenger.client.domain.room.handler;

import com.nhnacademy.messenger.client.domain.error.event.ErrorEvent;
import com.nhnacademy.messenger.client.domain.room.event.ListRoomSuccessEvent;
import com.nhnacademy.messenger.client.network.ResponseHandler;
import com.nhnacademy.messenger.common.event.EventBus;
import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.message.data.room.ListRoomResponse;
import com.nhnacademy.messenger.common.util.converter.MessageConverter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ListRoomResponseHandler implements ResponseHandler {

    @Override
    public void handle(Message message) {
        try {
            ListRoomResponse response = (ListRoomResponse) MessageConverter.toData(message);
            if (response == null) {
                throw new IllegalStateException("응답 데이터가 비어있습니다.");
            }

            // UI에 보여줄 문자열 리스트로 변환 (ID, 이름, 인원)
            List<String> formattedList = response.rooms().stream()
                    .map(info -> String.format("[%d] %s (%d명)",
                            info.roomId(), info.roomName(), info.userCount()))
                    .toList();

            EventBus.INSTANCE.publish(new ListRoomSuccessEvent(formattedList));

        } catch (Exception e) {
            log.error("채팅방 목록 처리 중 오류", e);
            EventBus.INSTANCE.publish(new ErrorEvent("채팅방 목록을 불러오지 못했습니다."));
        }
    }
}
