package com.nhnacademy.messenger.client.domain.room.handler;

import com.nhnacademy.messenger.client.domain.room.controller.ChatRoomController;
import com.nhnacademy.messenger.client.ui.cli.Command;
import com.nhnacademy.messenger.client.ui.cli.ConsoleView;
import com.nhnacademy.messenger.client.ui.cli.dispatcher.CommandExecutable;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateRoomCommandHandler implements CommandExecutable {

    private final ChatRoomController chatRoomController;
    public final static String COMMAND = "/create";

    @Override
    public void execute(Command command, ConsoleView view) {
        if (command.args().isEmpty()) {
            view.showErrorMessage("사용법: /create [방이름]");
            return;
        }
        String roomName = command.args().getFirst();
        chatRoomController.createRoom(roomName);
    }
}
