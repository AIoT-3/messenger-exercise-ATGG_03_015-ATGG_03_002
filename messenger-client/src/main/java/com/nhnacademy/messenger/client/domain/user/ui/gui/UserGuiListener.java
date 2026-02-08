package com.nhnacademy.messenger.client.domain.user.ui.gui;

import com.nhnacademy.messenger.client.domain.user.event.LoginSuccessEvent;
import com.nhnacademy.messenger.client.domain.user.event.LogoutSuccessEvent;
import com.nhnacademy.messenger.client.domain.user.event.UserListSuccessEvent;
import com.nhnacademy.messenger.client.ui.gui.GuiView;
import com.nhnacademy.messenger.common.event.EventListener;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserGuiListener {

    private final GuiView view;

    @EventListener
    public void onLoginSuccess(LoginSuccessEvent event) {
        view.showLoginSuccessDialog(event.userId());
        view.clearLoginFields();
        view.showRoomListPanel();
        view.requestRoomListInitialData();
    }

    @EventListener
    public void onLogoutSuccess(LogoutSuccessEvent event) {
        view.showLogoutSuccessDialog();
        view.clearAllData();
        view.showLoginPanel();
    }

    @EventListener
    public void onUserListReceived(UserListSuccessEvent event) {
        view.updateUserList(event.userList());
    }
}
