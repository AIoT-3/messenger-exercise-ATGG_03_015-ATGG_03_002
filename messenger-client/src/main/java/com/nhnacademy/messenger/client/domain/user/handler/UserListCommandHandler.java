package com.nhnacademy.messenger.client.domain.user.handler;

import com.nhnacademy.messenger.client.domain.user.service.UserClientService;
import com.nhnacademy.messenger.client.ui.cli.Command;
import com.nhnacademy.messenger.client.ui.cli.ConsoleView;
import com.nhnacademy.messenger.client.ui.cli.dispatcher.CommandExecutable;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserListCommandHandler implements CommandExecutable {

    private final UserClientService userClientService;
    private static final String COMMAND = "/users";
    private static final String DESCRIPTION = "/users - 전체 사용자 목록을 조회합니다.";

    @Override
    public void execute(Command command, ConsoleView view) {
        userClientService.getUserList();
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
