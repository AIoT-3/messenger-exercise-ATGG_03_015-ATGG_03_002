package com.nhnacademy.messenger.client.domain.chat.handler;

import com.nhnacademy.messenger.client.domain.room.controller.ChatRoomController;
import com.nhnacademy.messenger.client.ui.cli.Command;
import com.nhnacademy.messenger.client.ui.cli.ConsoleView;
import com.nhnacademy.messenger.client.ui.cli.dispatcher.CommandExecutable;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChatCommandHandler implements CommandExecutable {

    private static final String COMMAND = "/chat";
    private static final String DESCRIPTION = "/chat <roomId> <message> - 특정 방에 메시지를 전송합니다.";
    private final ChatRoomController controller;

    @Override
    public void execute(Command command, ConsoleView view) {
        if (command.args().size() < 2) {
            view.showSystemMessage("사용법: " + DESCRIPTION);
            return;
        }

        try {
            Long roomId = Long.parseLong(command.args().get(0));
            String message = String.join(" ", command.args().subList(1, command.args().size()));
            
            controller.requestSendMessage(roomId, message);
        } catch (NumberFormatException e) {
            view.showErrorMessage("방 ID는 숫자여야 합니다.");
        }
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
