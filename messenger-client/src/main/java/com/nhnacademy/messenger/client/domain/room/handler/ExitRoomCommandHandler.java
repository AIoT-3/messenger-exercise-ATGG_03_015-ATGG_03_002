package com.nhnacademy.messenger.client.domain.room.handler;

import com.nhnacademy.messenger.client.domain.room.service.ChatRoomClientService;
import com.nhnacademy.messenger.client.ui.cli.Command;
import com.nhnacademy.messenger.client.ui.cli.ConsoleView;
import com.nhnacademy.messenger.client.ui.cli.dispatcher.CommandExecutable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ExitRoomCommandHandler implements CommandExecutable {

    private static final String COMMAND = "/exit";
    private static final String DESCRIPTION = "/exit <roomId> - 종료합니다.";
    private final ChatRoomClientService service;

    @Override
    public void execute(Command command, ConsoleView view) {
        if (command.args().isEmpty()) {
            view.showSystemMessage("사용법: /exit <roomId>");
            return;
        }

        try {
            long roomId = Long.parseLong(command.args().getFirst());
            service.exitRoom(roomId);
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
