package com.nhnacademy.messenger.client.domain.user.handler;

import com.nhnacademy.messenger.client.domain.user.controller.UserController;
import com.nhnacademy.messenger.client.ui.cli.Command;
import com.nhnacademy.messenger.client.ui.cli.ConsoleView;
import com.nhnacademy.messenger.client.ui.cli.dispatcher.CommandExecutable;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoginCommandHandler implements CommandExecutable {

    private final UserController userController;
    private static final String COMMAND = "/login";
    private static final String DESCRIPTION = "/login <id> <pw> - 로그인합니다.";

    @Override
    public void execute(Command command, ConsoleView view) {
        if (command.args().size() < 2) {
            view.showErrorMessage("사용법: " + DESCRIPTION);
            return;
        }
        String userId = command.args().get(0);
        String password = command.args().get(1);
        userController.login(userId, password);
    }

    @Override
    public String getCommand() {
        return COMMAND;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }
}