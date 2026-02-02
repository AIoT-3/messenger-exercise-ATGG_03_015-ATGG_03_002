package com.nhnacademy.messenger.client.domain.user.service;

import com.nhnacademy.messenger.client.network.MessageClient;
import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.message.data.auth.LoginRequest;
import com.nhnacademy.messenger.common.message.header.MessageType;
import com.nhnacademy.messenger.common.message.header.RequestHeader;
import com.nhnacademy.messenger.common.util.converter.MessageConverter;
import lombok.RequiredArgsConstructor;

import java.time.ZonedDateTime;

@RequiredArgsConstructor
public class UserClientService {
    private final MessageClient messageClient;

    public void login(String userId, String password) {
        LoginRequest data = new LoginRequest(userId, password);
        RequestHeader header = new RequestHeader(MessageType.LOGIN, ZonedDateTime.now(), null);
        Message message = new Message(header, MessageConverter.objectMapper.valueToTree(data));
        
        messageClient.send(message);
    }
}
