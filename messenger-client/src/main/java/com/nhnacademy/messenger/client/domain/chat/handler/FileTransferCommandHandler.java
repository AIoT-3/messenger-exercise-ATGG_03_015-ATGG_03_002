package com.nhnacademy.messenger.client.domain.chat.handler;

import com.nhnacademy.messenger.client.domain.room.service.ChatRoomClientService;
import com.nhnacademy.messenger.client.session.ClientSession;
import com.nhnacademy.messenger.client.ui.cli.Command;
import com.nhnacademy.messenger.client.ui.cli.ConsoleView;
import com.nhnacademy.messenger.client.ui.cli.dispatcher.CommandExecutable;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FileTransferCommandHandler implements CommandExecutable {

    private static final String COMMAND = "/sendfile";
    private static final String DESCRIPTION = "/sendfile <filePath> - 현재 방에 파일을 전송합니다.";
    private final ChatRoomClientService chatRoomClientService;

    @Override
    public void execute(Command command, ConsoleView view) {
        if (command.args().isEmpty()) {
            view.showSystemMessage("사용법: " + DESCRIPTION);
            return;
        }

        Long currentRoomId = ClientSession.INSTANCE.getCurrentRoomId();
        if (currentRoomId == null) {
            view.showErrorMessage("채팅방에 입장한 상태여야 합니다.");
            return;
        }

        String filePath = command.args().get(0);
        try {
            chatRoomClientService.sendFile(currentRoomId, filePath);
            view.showSystemMessage("파일 전송을 시작했습니다: " + filePath);
        } catch (IllegalArgumentException e) {
            view.showErrorMessage(e.getMessage());
        } catch (Exception e) {
            view.showErrorMessage("파일 전송 실패: " + e.getMessage());
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
