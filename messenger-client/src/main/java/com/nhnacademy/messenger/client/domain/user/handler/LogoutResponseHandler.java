package com.nhnacademy.messenger.client.domain.user.handler;

import com.nhnacademy.messenger.client.domain.user.event.LogoutSuccessEvent;
import com.nhnacademy.messenger.client.network.ResponseHandler;
import com.nhnacademy.messenger.client.session.ClientSession;
import com.nhnacademy.messenger.common.event.EventBus;
import com.nhnacademy.messenger.common.message.Message;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogoutResponseHandler implements ResponseHandler {

    @Override
    public void handle(Message message) {
        // clear() 세션 정보 초기화
        ClientSession.INSTANCE.clear();
        log.info("로그아웃 성공, 세션 초기화 완료");

        EventBus.INSTANCE.publish(new LogoutSuccessEvent());
    }
}