package com.nhnacademy.messenger.client.domain.room.handler;

import com.nhnacademy.messenger.client.domain.room.controller.ChatRoomController;
import com.nhnacademy.messenger.client.ui.cli.Command;
import com.nhnacademy.messenger.client.ui.cli.ConsoleView;
import com.nhnacademy.messenger.client.ui.cli.dispatcher.CommandExecutable;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateRoomCommandHandler implements CommandExecutable {

    private static final String COMMAND = "/create";
    private static final String DESCRIPTION = "/create <roomName> - 채팅방을 생성합니다.";
    private final ChatRoomController chatRoomController;

    @Override
    public void execute(Command command, ConsoleView view) {
        if (command.args().isEmpty()) {
            view.showErrorMessage("사용법: " + DESCRIPTION);
            return;
        }
        String roomName = command.args().getFirst();
        chatRoomController.requestCreateRoom(roomName);
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
