package com.nhnacademy.messenger.client.ui;

import com.nhnacademy.messenger.client.domain.error.event.ErrorEvent;
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
    public void onError(ErrorEvent event) {
        view.showErrorMessage(event.message());
    }
}
