package com.nhnacademy.messenger.client.ui;

import com.nhnacademy.messenger.client.domain.error.event.ErrorEvent;
import com.nhnacademy.messenger.client.domain.user.event.LoginSuccessEvent;
import com.nhnacademy.messenger.client.event.EventListener;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClientUiEventListener {

    private final View view;

    @EventListener
    public void onLoginSuccess(LoginSuccessEvent event) {
        view.showLoginSuccess(event.userId());
    }

    @EventListener
    public void onError(ErrorEvent event) {
        view.showErrorMessage(event.message());
    }
}
