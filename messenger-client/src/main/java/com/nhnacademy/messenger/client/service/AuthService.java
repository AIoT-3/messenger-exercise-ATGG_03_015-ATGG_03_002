package com.nhnacademy.messenger.client.service;

import com.nhnacademy.messenger.client.net.ServerConnector;
import com.nhnacademy.messenger.client.service.handler.MessageHandler;
import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.message.data.auth.LoginRequest;
import com.nhnacademy.messenger.common.message.data.auth.LoginResponse;
import com.nhnacademy.messenger.common.message.header.MessageType;
import com.nhnacademy.messenger.common.message.header.RequestHeader;

public class AuthService {
    private final ServerConnector serverConnector;

    // ServerConnector를 주입받습니다.
    public AuthService(ServerConnector serverConnector) {
        this.serverConnector = serverConnector;
    }

    /**
     * 사용자 로그인 요청을 서버로 전송합니다.
     * @param userId 사용자 ID
     * @param password 비밀번호
     */
    public void requestLogin(String userId, String password) {
        // 1. 전송할 요청 데이터 (MessageData) 객체 생성
        LoginRequest loginRequest = new LoginRequest(userId, password);

        // 2. 요청 메시지 헤더 (RequestHeader) 객체 생성
        //    common 모듈의 MessageType enum을 사용하여 메시지 종류를 지정합니다.
        RequestHeader header = new RequestHeader(MessageType.LOGIN_REQUEST);

        // 3. 헤더와 데이터를 포함하는 최종 Message 객체 생성
        Message message = new Message(header, loginRequest);

        // 4. ServerConnector를 통해 서버로 Message 객체 전송
        serverConnector.send(message);

        System.out.println("[AuthService] Login request sent for user: " + userId);
    }

    /**
     * 서버로부터 LOGIN_RESPONSE 메시지를 받았을 때 자동으로 호출될 핸들러 메서드입니다.
     * @param response 로그인 응답 데이터
     */
    @MessageHandler(MessageType.LOGIN_RESPONSE)
    public void handleLoginResponse(LoginResponse response) {
        if (response.isSuccess()) {
            System.out.println("[AuthService] Login successful. Welcome!");
            // TODO: 로그인 성공 시 UI 전환, 사용자 상태 업데이트 등
            //       옵저버에게 로그인 성공을 notify 해야 함
        } else {
            System.err.println("[AuthService] Login failed: " + response.getErrorMessage());
            // TODO: 로그인 실패 시 UI에 에러 메시지 표시 등
            //       옵저버에게 로그인 실패를 notify 해야 함
        }
    }
}
