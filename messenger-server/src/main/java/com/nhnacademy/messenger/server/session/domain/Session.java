package com.nhnacademy.messenger.server.session.domain;

import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.message.data.error.ErrorCode;
import com.nhnacademy.messenger.server.user.domain.User;

import java.util.Set;

/**
 * Session Interface
 * 모든 통신 모델(BIO, NIO 등)의 세션 공통 기능을 정의함
 */
public interface Session {
    String getId();
    User getUser();
    Set<Long> getJoinedRoomIds();
    
    void registerUser(User user, String sessionId);
    void sendMessage(Message message);
    void sendError(ErrorCode code, String message);
    void closeWithReason(ErrorCode code, String message);
    void validateLoggedIn();
    
    void joinRoom(Long roomId);
    void leaveRoom(Long roomId);
    void logout();
    
    void validateMessage(Message message);
    void processRequest(Message message); // 메시지 처리 및 에러 응답 전담
    void disconnect();
}