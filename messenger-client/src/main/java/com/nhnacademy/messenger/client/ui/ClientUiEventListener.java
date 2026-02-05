package com.nhnacademy.messenger.client.ui;

import com.nhnacademy.messenger.client.domain.chat.event.*;
import com.nhnacademy.messenger.client.domain.error.event.ErrorEvent;
import com.nhnacademy.messenger.client.domain.room.event.*;
import com.nhnacademy.messenger.client.domain.user.event.*;
import com.nhnacademy.messenger.common.event.EventListener;
import lombok.RequiredArgsConstructor;

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