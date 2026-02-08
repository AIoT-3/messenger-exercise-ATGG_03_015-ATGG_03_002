package com.nhnacademy.messenger.client.ui.cli.handler;

import com.nhnacademy.messenger.client.session.ClientSession;
import com.nhnacademy.messenger.client.ui.cli.Command;
import com.nhnacademy.messenger.client.ui.cli.ConsoleView;
import com.nhnacademy.messenger.client.ui.cli.dispatcher.CommandExecutable;

public class ExitCommandHandler implements CommandExecutable {

    private static final String COMMAND = "/exit";
    private static final String DESCRIPTION = "/exit (또는 /quit) - 프로그램을 종료합니다.";

    @Override
    public void execute(Command command, ConsoleView view) {
        ClientSession.INSTANCE.stop();
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
