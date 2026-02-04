package com.nhnacademy.messenger.server.user.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.message.data.user.UserListResponse;
import com.nhnacademy.messenger.common.message.header.MessageType;
import com.nhnacademy.messenger.common.message.header.ResponseHeader;
import com.nhnacademy.messenger.common.util.converter.MessageConverter;
import com.nhnacademy.messenger.server.network.RequestHandler;
import com.nhnacademy.messenger.server.session.domain.Session;
import com.nhnacademy.messenger.server.session.manager.SessionManager;
import com.nhnacademy.messenger.server.user.domain.User;
import com.nhnacademy.messenger.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class UserListRequestHandler implements RequestHandler {

    private final UserService userService;
    private final SessionManager sessionManager;

    @Override
    public void handle(Session session, Message message) {
        // 이미 Session.validateMessage에서 세션 검증은 완료됨
        
        List<User> allUsers = userService.getAllUsers();
        
        List<UserListResponse.UserInfo> userInfos = allUsers.stream()
                .map(user -> new UserListResponse.UserInfo(
                        user.getUserId(),
                        user.getUserName(),
                        sessionManager.getSessionByUserId(user.getUserId()).isPresent()
                ))
                .toList();

        UserListResponse responseData = new UserListResponse(userInfos);
        
        ResponseHeader header = ResponseHeader.success(MessageType.USER_LIST_SUCCESS);
        JsonNode data = MessageConverter.objectMapper.valueToTree(responseData);
        
        session.sendMessage(new Message(header, data));
        log.info("사용자 목록 전송 완료: 요청자={}, 사용자 수={}", session.getUser().getUserId(), userInfos.size());
    }
}
