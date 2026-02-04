package com.nhnacademy.messenger.client.ui;

import com.nhnacademy.messenger.common.message.data.room.RoomInfo;
import com.nhnacademy.messenger.common.message.data.user.UserInfo;
import java.util.List;

/**
 * UI 계층의 공통 인터페이스.
 */
public interface View {
    // 시스템 알림
    void showSystemMessage(String message);
    void showErrorMessage(String message);

    // 로그인 관련
    void showLoginSuccess(String userName);
    void showLogoutSuccess();

    // 사용자 관련
    void showUserList(List<UserInfo> users);

    // 채팅방 관련
    void showRoomList(List<RoomInfo> rooms);
    void showRoomEnterSuccess(Long roomId, List<String> users);
    void showRoomExitSuccess(Long roomId);
    
    // 메시지 출력
    void appendMessage(Long roomId, String sender, String content);
    void appendPrivateMessage(String senderId, String content);
}
