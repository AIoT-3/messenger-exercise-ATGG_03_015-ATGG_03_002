package com.nhnacademy.messenger.client.domain.room.handler;

import com.nhnacademy.messenger.client.domain.room.controller.ChatRoomController;
import com.nhnacademy.messenger.client.ui.cli.Command;
import com.nhnacademy.messenger.client.ui.cli.ConsoleView;
import com.nhnacademy.messenger.client.ui.cli.dispatcher.CommandExecutable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ExitRoomCommandHandler implements CommandExecutable {

    public static final String COMMAND = "/exit";
    private final ChatRoomController controller;

    @Override
    public void execute(Command command, ConsoleView view) {
        if (command.args().isEmpty()) {
            view.showSystemMessage("사용법: /exit <roomId>");
            return;
        }

        try {
            long roomId = Long.parseLong(command.args().getFirst());
            controller.requestExitRoom(roomId);
        } catch (NumberFormatException e) {
            view.showErrorMessage("방 ID는 숫자여야 합니다.");
        }
    }
}
