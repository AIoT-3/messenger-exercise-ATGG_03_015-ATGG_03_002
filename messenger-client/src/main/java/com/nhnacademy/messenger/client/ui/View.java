package com.nhnacademy.messenger.client.ui;

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

    // 채팅방 관련
    void showRoomList(List<String> rooms); // 임시로 String 리스트, 추후 DTO로 변경
    void showRoomEnterSuccess(String roomName);
    
    // 메시지 출력
    void appendMessage(String sender, String content);
}
