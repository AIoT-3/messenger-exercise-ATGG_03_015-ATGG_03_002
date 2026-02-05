package com.nhnacademy.messenger.client.ui;

import com.nhnacademy.messenger.client.domain.chat.event.*;
import com.nhnacademy.messenger.client.domain.error.event.ErrorEvent;
import com.nhnacademy.messenger.client.domain.room.event.*;
import com.nhnacademy.messenger.client.domain.user.event.*;
import com.nhnacademy.messenger.common.event.EventListener;
import lombok.RequiredArgsConstructor;

/**
 * TODO: View 인터페이스랑 같이 메서드가 계속 확장됨
 * 뭔가 중간에 하나 이걸 통해서 넘어가는 게 추가된 느낌?
 * 뭔가 이벤트가 넘어갈 때 공통적으로 처리하는 게 있는 게 아니라 그냥 추가만 하는 느낌?
 * 그럼 그냥 차라리 전형적인 observer 패턴으로 바꾸는 게 낫지 않나?
 * 어떤 뭐 Object라던가 Map을 쓴다던가 타입 파라메터로 해서 변환 시켜도 될 것 같고.
 * 복잡도를 줄여 보는 게 좋을 것 같다.
 */
@RequiredArgsConstructor
public class ClientUiEventListener {

    private final View view;

    @EventListener
    public void onLoginSuccess(LoginSuccessEvent event) {
        view.showLoginSuccess(event.userId());
    }

    @EventListener
    public void onLogoutSuccess(LogoutSuccessEvent event) { view.showLogoutSuccess(); }

    @EventListener
    public void onUserListReceived(UserListSuccessEvent event) {
        view.showUserList(event.userList());
    }

    @EventListener
    public void onRoomCreated(CreateRoomSuccessEvent event) {
        view.showSystemMessage("채팅방이 생성되었습니다: " + event.roomName() + " (ID: " + event.roomId() + ")");
    }

    @EventListener
    public void onRoomListReceived(ListRoomSuccessEvent event) {
        view.showRoomList(event.roomList());
    }

    @EventListener
    public void onRoomEnter(EnterRoomSuccessEvent event) {
        view.showRoomEnterSuccess(event.roomId(), event.users());
    }

    @EventListener
    public void onChatHistoryReceived(ChatHistoryResponseEvent event) {
        view.showChatHistory(event.roomId(), event.messages(), event.hasMore());
    }

    @EventListener
    public void onMessageReceived(ReceiveMessageEvent event) {
        view.appendMessage(event.roomId(), event.messageId(), event.senderId(), event.content());
    }

    @EventListener
    public void onRoomExit(ExitRoomSuccessEvent event) {
        view.showRoomExitSuccess(event.roomId());
    }

    @EventListener
    public void onPushRoomEnter(PushRoomEnterEvent event) {
        view.showPushRoomEnter(event.roomId(), event.userId(), event.userName());
    }

    @EventListener
    public void onPushRoomExit(PushRoomExitEvent event) {
        view.showPushRoomExit(event.roomId(), event.userId());
    }

    @EventListener
    public void onPrivateMessageReceived(ReceivePrivateMessageEvent event) {
        view.appendPrivateMessage(event.senderId(), event.content());
    }

    @EventListener
    public void onError(ErrorEvent event) {
        view.showErrorMessage(event.message());
    }
}