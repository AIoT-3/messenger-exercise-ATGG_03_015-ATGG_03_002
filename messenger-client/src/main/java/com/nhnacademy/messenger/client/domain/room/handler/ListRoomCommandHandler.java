package com.nhnacademy.messenger.client.domain.room.handler;

import com.nhnacademy.messenger.client.domain.room.controller.ChatRoomController;
import com.nhnacademy.messenger.client.ui.cli.Command;
import com.nhnacademy.messenger.client.ui.cli.ConsoleView;
import com.nhnacademy.messenger.client.ui.cli.dispatcher.CommandExecutable;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ListRoomCommandHandler implements CommandExecutable {

    public static final String COMMAND = "/list";
    private final ChatRoomController controller;

    @Override
    public void execute(Command command, ConsoleView view) {
        controller.requestRoomList();
    }
}
