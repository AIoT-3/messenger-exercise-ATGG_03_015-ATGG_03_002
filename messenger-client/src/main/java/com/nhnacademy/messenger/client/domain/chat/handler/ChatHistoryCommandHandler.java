package com.nhnacademy.messenger.client.domain.chat.handler;

import com.nhnacademy.messenger.client.domain.room.service.ChatRoomClientService;
import com.nhnacademy.messenger.client.ui.cli.Command;
import com.nhnacademy.messenger.client.ui.cli.ConsoleView;
import com.nhnacademy.messenger.client.ui.cli.dispatcher.CommandExecutable;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChatHistoryCommandHandler implements CommandExecutable {

    private static final String COMMAND = "/history";
    private static final String DESCRIPTION = "/history <roomId> <limit> <messageId> - 특정 방의 채팅 기록을 특정 메시지 이전부터 limit 개수만큼 조회합니다.";
    private final ChatRoomClientService chatRoomClientService;

    @Override
    public void execute(Command command, ConsoleView view) {
        if (command.args().isEmpty()) {
            view.showErrorMessage("사용법: " + DESCRIPTION);
            return;
        }

        try {
            Long roomId = Long.parseLong(command.args().get(0));
            Integer limit = (command.args().size() >= 2) ? Integer.parseInt(command.args().get(1)) : null;
            Long beforeMessageId = (command.args().size() >= 3) ? Long.parseLong(command.args().get(2)) : null;

            chatRoomClientService.getChatHistory(roomId, limit, beforeMessageId);
        } catch (NumberFormatException e) {
            view.showErrorMessage("인자값은 숫자여야 합니다.");
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
