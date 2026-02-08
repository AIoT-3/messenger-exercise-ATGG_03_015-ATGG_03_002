package com.nhnacademy.messenger.client.domain.error.ui.console;

import com.nhnacademy.messenger.client.domain.error.event.ErrorEvent;
import com.nhnacademy.messenger.client.ui.cli.ConsoleView;
import com.nhnacademy.messenger.common.event.EventListener;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SystemConsoleListener {

    private final ConsoleView view;

    @EventListener
    public void onError(ErrorEvent event) {
        view.showErrorMessage(event.message());
    }
}
