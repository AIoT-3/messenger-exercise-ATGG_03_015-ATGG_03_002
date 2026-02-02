package com.nhnacademy.messenger.client.domain.user.handler;

import com.nhnacademy.messenger.client.domain.error.event.ErrorEvent;
import com.nhnacademy.messenger.client.domain.user.event.LoginSuccessEvent;
import com.nhnacademy.messenger.client.network.ResponseHandler;
import com.nhnacademy.messenger.client.session.ClientSession;
import com.nhnacademy.messenger.common.event.EventBus;
import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.message.data.auth.LoginResponse;
import com.nhnacademy.messenger.common.util.converter.MessageConverter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginResponseHandler implements ResponseHandler {

    @Override
    public void handle(Message message) {
        try {
            LoginResponse response = (LoginResponse) MessageConverter.toData(message);
            
            if (response == null) {
                throw new IllegalStateException("로그인 응답 데이터가 비어있습니다.");
            }

            // 세션 정보 갱신
            ClientSession.INSTANCE.setSessionId(response.sessionId());
            ClientSession.INSTANCE.setUserId(response.userId());
            ClientSession.INSTANCE.setUserName(response.userId());
            
            EventBus.INSTANCE.publish(new LoginSuccessEvent(response.userId()));
            
        } catch (Exception e) {
            EventBus.INSTANCE.publish(new ErrorEvent("로그인 응답 처리 중 오류가 발생했습니다."));
            log.error("로그인 응답 처리 중 오류 발생", e);
        }
    }
}