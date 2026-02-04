package com.nhnacademy.messenger.client.domain.chat.handler;

import com.nhnacademy.messenger.client.domain.room.service.ChatRoomClientService;
import com.nhnacademy.messenger.client.ui.cli.Command;
import com.nhnacademy.messenger.client.ui.cli.ConsoleView;
import com.nhnacademy.messenger.client.ui.cli.dispatcher.CommandExecutable;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PrivateChatCommandHandler implements CommandExecutable {

    private static final String COMMAND = "/whisper";
    private static final String DESCRIPTION = "/whisper <userId> <message> - 특정 사용자에게 귓속말을 보냅니다.";
    
    private final ChatRoomClientService chatRoomClientService;

    @Override
    public void execute(Command command, ConsoleView view) {
        if (command.args().size() < 2) {
            view.showSystemMessage("사용법: " + DESCRIPTION);
            return;
        }

        String targetUserId = command.args().get(0);
        String message = String.join(" ", command.args().subList(1, command.args().size()));

        try {
            chatRoomClientService.sendPrivateMessage(targetUserId, message);
            view.showSystemMessage("[나 -> " + targetUserId + "] " + message);
        } catch (Exception e) {
            view.showErrorMessage("귓속말 전송 실패: " + e.getMessage());
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
