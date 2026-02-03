package com.nhnacademy.messenger.client.ui.cli.handler;

import com.nhnacademy.messenger.client.ui.cli.Command;
import com.nhnacademy.messenger.client.ui.cli.ConsoleView;
import com.nhnacademy.messenger.client.ui.cli.dispatcher.CLICommandDispatcher;
import com.nhnacademy.messenger.client.ui.cli.dispatcher.CommandExecutable;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;

@RequiredArgsConstructor
public class HelpCommandHandler implements CommandExecutable {

    private static final String COMMAND = "/help";
    private static final String DESCRIPTION = "/help - 명령어 도움말을 확인합니다.";

    private final CLICommandDispatcher dispatcher;

    @Override
    public void execute(Command command, ConsoleView view) {
        StringBuilder sb = new StringBuilder();
        sb.append("============== 명령어 도움말 ===============\n");

        dispatcher.getHandlers().stream()
                .distinct()
                .sorted(Comparator.comparing(CommandExecutable::getCommand))
                .forEach(handler -> {
                    sb.append(String.format("- %s%n", handler.getDescription()));
                });

        sb.append("=========================================");
        view.showSystemMessage(sb.toString());
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
