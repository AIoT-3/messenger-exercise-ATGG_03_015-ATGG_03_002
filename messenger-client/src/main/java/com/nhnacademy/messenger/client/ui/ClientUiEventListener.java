package com.nhnacademy.messenger.client.ui;

import com.nhnacademy.messenger.client.domain.error.event.ErrorEvent;
import com.nhnacademy.messenger.client.domain.room.event.CreateRoomSuccessEvent;
import com.nhnacademy.messenger.client.domain.room.event.EnterRoomSuccessEvent;
import com.nhnacademy.messenger.client.domain.room.event.ListRoomSuccessEvent;
import com.nhnacademy.messenger.client.domain.user.event.LoginSuccessEvent;
import com.nhnacademy.messenger.client.domain.user.event.LogoutSuccessEvent;
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
    public void onRoomCreated(CreateRoomSuccessEvent event) {
        view.showSystemMessage("채팅방이 생성되었습니다: " + event.roomName() + " (ID: " + event.roomId() + ")");
    }

    @EventListener
    public void onRoomListReceived(ListRoomSuccessEvent event) { view.showRoomList(event.roomList()); }

    @EventListener
    public void onRoomEnter(EnterRoomSuccessEvent event) {
        view.showRoomEnterSuccess(event.roomId(), event.users());
    }

    @EventListener
    public void onError(ErrorEvent event) {
        view.showErrorMessage(event.message());
    }
}
