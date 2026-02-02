package com.nhnacademy.messenger.client.ui.cli.dispatcher;

import com.nhnacademy.messenger.client.ui.cli.Command;
import com.nhnacademy.messenger.client.ui.cli.ConsoleView;

public interface CommandExecutable {
    void execute(Command command, ConsoleView view);
}
