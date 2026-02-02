package com.nhnacademy.messenger.client.domain.error.handler;

import com.nhnacademy.messenger.client.domain.error.event.ErrorEvent;
import com.nhnacademy.messenger.common.event.EventBus;
import com.nhnacademy.messenger.client.network.ResponseHandler;
import com.nhnacademy.messenger.client.network.annotation.ResponseMapping;
import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.message.data.error.ErrorResponse;
import com.nhnacademy.messenger.common.message.header.MessageType;
import com.nhnacademy.messenger.common.util.converter.MessageConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@ResponseMapping(type = MessageType.ERROR)
public class ErrorResponseHandler implements ResponseHandler {

    private final EventBus eventBus;

    @Override
    public void handle(Message message) {
        try {
            ErrorResponse response = (ErrorResponse) MessageConverter.toData(message);
            
            String errorMessage;
            if (response != null) {
                log.error("서버 에러 발생: [{}] {}", response.code(), response.message());
                errorMessage = response.message();
            } else {
                log.error("서버 에러 발생 (데이터 없음)");
                errorMessage = "서버에서 알 수 없는 에러가 발생했습니다.";
            }
            
            eventBus.publish(new ErrorEvent(errorMessage));
            
        } catch (Exception e) {
            log.error("에러 메시지 처리 중 오류 발생", e);
            eventBus.publish(new ErrorEvent("서버 통신 중 오류가 발생했습니다."));
        }
    }
}
