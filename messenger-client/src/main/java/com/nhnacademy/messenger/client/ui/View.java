package com.nhnacademy.messenger.client.ui;

import com.nhnacademy.messenger.common.message.data.chat.MessageInfo;
import com.nhnacademy.messenger.common.message.data.room.RoomInfo;
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
    void showRoomList(List<RoomInfo> rooms);
    void showRoomEnterSuccess(Long roomId, List<String> users);
    void showRoomExitSuccess(Long roomId);
    
    // 메시지 출력
    void appendMessage(Long roomId, Long messageId, String sender, String content);
    void showChatHistory(Long roomId, List<MessageInfo> messages, boolean hasMore);
}
