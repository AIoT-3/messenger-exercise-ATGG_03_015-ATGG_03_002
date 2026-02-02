package com.nhnacademy.messenger.client.domain.user.handler;

import com.nhnacademy.messenger.client.domain.user.event.LoginSuccessEvent;
import com.nhnacademy.messenger.client.event.EventBus;
import com.nhnacademy.messenger.client.network.ResponseHandler;
import com.nhnacademy.messenger.client.network.annotation.ResponseMapping;
import com.nhnacademy.messenger.client.session.ClientSession;
import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.message.data.auth.LoginResponse;
import com.nhnacademy.messenger.common.message.header.MessageType;
import com.nhnacademy.messenger.common.util.converter.MessageConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@ResponseMapping(type = MessageType.LOGIN_SUCCESS)
public class LoginResponseHandler implements ResponseHandler {

    private final EventBus eventBus;

    @Override
    public void handle(Message message) {
        try {
            LoginResponse response = (LoginResponse) MessageConverter.toData(message);
            
            if (response == null) {
                throw new IllegalStateException("로그인 응답 데이터가 비어있습니다.");
            }

            // 세션 정보 갱신
            ClientSession session = ClientSession.INSTANCE;
            session.setSessionId(response.sessionId());
            session.setUserId(response.userId());
            session.setUserName(response.userId());
            
            eventBus.publish(new LoginSuccessEvent(response.userId()));
            
        } catch (Exception e) {
            log.error("로그인 응답 처리 중 오류 발생", e);
            // TODO: 에러 이벤트도 발행? 해볼지 생각
        }
    }
}