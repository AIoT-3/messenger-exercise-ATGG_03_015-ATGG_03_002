package com.nhnacademy.messenger.client.domain.user.handler;

import com.nhnacademy.messenger.client.domain.error.event.ErrorEvent;
import com.nhnacademy.messenger.client.domain.user.event.UserListSuccessEvent;
import com.nhnacademy.messenger.client.network.ResponseHandler;
import com.nhnacademy.messenger.common.event.EventBus;
import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.message.data.user.UserListResponse;
import com.nhnacademy.messenger.common.util.converter.MessageConverter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserListResponseHandler implements ResponseHandler {

    @Override
    public void handle(Message message) {
        try {
            UserListResponse response = (UserListResponse) MessageConverter.toData(message);
            if (response == null) {
                throw new IllegalStateException("사용자 목록 응답 데이터가 비어있습니다.");
            }
            EventBus.INSTANCE.publish(new UserListSuccessEvent(response.users()));
        } catch (Exception e) {
            EventBus.INSTANCE.publish(new ErrorEvent("사용자 목록 응답 처리 중 오류가 발생했습니다."));
            log.error("사용자 목록 응답 처리 중 오류 발생", e);
        }
    }
}
