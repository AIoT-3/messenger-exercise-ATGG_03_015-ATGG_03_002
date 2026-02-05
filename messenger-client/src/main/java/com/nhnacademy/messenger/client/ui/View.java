package com.nhnacademy.messenger.client.ui;

import com.nhnacademy.messenger.common.message.data.chat.MessageInfo;
import com.nhnacademy.messenger.common.message.data.room.RoomInfo;
import com.nhnacademy.messenger.common.message.data.user.UserInfo;
import java.util.List;

/**
 * UI 계층의 공통 인터페이스.
 * TODO: 기능이 추가될 때마다 메서드가 계속 추가됨. 추상화 시키는 범위가 너무 넓은 것 같다.
 * 아니면 뭐 show랑 model이랑 model map? 같은 걸로 분리해보면 좋지 않을까?
 * 추상 메서드로 만들어두면, 이것도 key-value로 등록할 수 있으니까.
 * 그렇게 추상화해서 각각의 기능에 한정해서 구현할 수 있다.
 * 전략 패턴으로 만들어보면 좋을 듯.
 *
 * 여기에는 차라리 콘솔 출력하는 메서드 하나 gui에 출력하는 메서드 하나 이렇게 해서
 * 이걸 구현하는 느낌으로 가는 게 좋을듯.
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

    // 실시간 알림 (Push)
    void showPushRoomEnter(Long roomId, String userId, String userName);
    void showPushRoomExit(Long roomId, String userId);
    
    // 메시지 출력
    void appendMessage(Long roomId, Long messageId, String sender, String content);
    void appendPrivateMessage(String senderId, String content);
    void showChatHistory(Long roomId, List<MessageInfo> messages, boolean hasMore);
}
