package com.nhnacademy.messenger.client.domain.room.handler;

import com.nhnacademy.messenger.client.domain.room.service.ChatRoomClientService;
import com.nhnacademy.messenger.client.ui.cli.Command;
import com.nhnacademy.messenger.client.ui.cli.ConsoleView;
import com.nhnacademy.messenger.client.ui.cli.dispatcher.CommandExecutable;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ListRoomCommandHandler implements CommandExecutable {

    private static final String COMMAND = "/list";
    private static final String DESCRIPTION = "/list - 채팅방 목록을 조회합니다.";
    private final ChatRoomClientService chatRoomClientService;

    @Override
    public void execute(Command command, ConsoleView view) {
        chatRoomClientService.getRoomList();
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
