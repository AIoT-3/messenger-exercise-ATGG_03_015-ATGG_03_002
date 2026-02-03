package com.nhnacademy.messenger.client.domain.user.handler;

import com.nhnacademy.messenger.client.domain.user.service.UserClientService;
import com.nhnacademy.messenger.client.ui.cli.Command;
import com.nhnacademy.messenger.client.ui.cli.ConsoleView;
import com.nhnacademy.messenger.client.ui.cli.dispatcher.CommandExecutable;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LogoutCommandHandler implements CommandExecutable {

    private final UserClientService userClientService;
    private static final String COMMAND = "/logout";
    private static final String DESCRIPTION = "/logout - 로그아웃합니다.";

    @Override
    public void execute(Command command, ConsoleView view) {
        if (!command.args().isEmpty()) {
            view.showErrorMessage("사용법: " + DESCRIPTION);
            return;
        }
        userClientService.logout();
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
