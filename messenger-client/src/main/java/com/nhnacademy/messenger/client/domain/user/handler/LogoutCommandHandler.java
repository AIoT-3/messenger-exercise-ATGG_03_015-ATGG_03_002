package com.nhnacademy.messenger.client.domain.user.handler;

import com.nhnacademy.messenger.client.domain.user.controller.UserController;
import com.nhnacademy.messenger.client.ui.cli.Command;
import com.nhnacademy.messenger.client.ui.cli.ConsoleView;
import com.nhnacademy.messenger.client.ui.cli.dispatcher.CommandExecutable;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LogoutCommandHandler implements CommandExecutable {

    private final UserController userController;
    public final static String COMMAND = "/logout";

    @Override
    public void execute(Command command, ConsoleView view) {
        if (!command.args().isEmpty()) {
            view.showErrorMessage("사용법: /logout");
            return;
        }
        userController.logout();
    }
}
