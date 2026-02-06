package com.nhnacademy.messenger.client.domain.room.handler;

import com.nhnacademy.messenger.client.domain.error.event.ErrorEvent;
import com.nhnacademy.messenger.client.domain.room.event.EnterRoomSuccessEvent;
import com.nhnacademy.messenger.client.domain.room.service.ChatRoomClientService;
import com.nhnacademy.messenger.client.network.ResponseHandler;
import com.nhnacademy.messenger.client.network.MessageHandler;
import com.nhnacademy.messenger.client.session.ClientSession;
import com.nhnacademy.messenger.common.event.EventBus;
import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.message.data.room.EnterRoomResponse;
import com.nhnacademy.messenger.common.message.header.MessageType;
import com.nhnacademy.messenger.common.util.converter.MessageConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@MessageHandler(MessageType.CHAT_ROOM_ENTER_SUCCESS)
public class EnterRoomResponseHandler implements ResponseHandler {

    private final ChatRoomClientService chatRoomClientService;

    @Override
    public void handle(Message message) {
        try {
            EnterRoomResponse response = (EnterRoomResponse) MessageConverter.toData(message);
            if (response == null) {
                throw new IllegalStateException("입장 응답 데이터가 비어있습니다.");
            }

            ClientSession.INSTANCE.setCurrentRoomId(response.roomId());
            EventBus.INSTANCE.publish(new EnterRoomSuccessEvent(response.roomId(), response.users()));

            // 입장 성공 시 자동으로 최근 채팅 내역 조회 (기본값 사용)
            chatRoomClientService.getChatHistory(response.roomId(), null, null);

        } catch (Exception e) {
            log.error("채팅방 입장 처리 중 오류", e);
            EventBus.INSTANCE.publish(new ErrorEvent("채팅방 입장에 실패했습니다."));
        }
    }
}
