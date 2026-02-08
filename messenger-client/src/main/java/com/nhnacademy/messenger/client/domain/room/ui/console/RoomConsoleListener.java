package com.nhnacademy.messenger.client.domain.room.ui.console;

import com.nhnacademy.messenger.client.domain.room.event.*;
import com.nhnacademy.messenger.client.session.ClientSession;
import com.nhnacademy.messenger.client.ui.cli.ConsoleView;
import com.nhnacademy.messenger.common.event.EventListener;
import com.nhnacademy.messenger.common.message.data.room.RoomInfo;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class RoomConsoleListener {

    private final ConsoleView view;

    @EventListener
    public void onRoomCreated(CreateRoomSuccessEvent event) {
        view.showSystemMessage("채팅방이 생성되었습니다: " + event.roomName() + " (ID: " + event.roomId() + ")");
    }

    @EventListener
    public void onRoomListReceived(ListRoomSuccessEvent event) {
        List<RoomInfo> rooms = event.roomList();
        StringBuilder sb = new StringBuilder();
        sb.append("============== 채팅방 목록 ==============\n");
        if (rooms == null || rooms.isEmpty()) {
            sb.append("(채팅방이 없습니다)\n");
        } else {
            for (RoomInfo info : rooms) {
                ClientSession.INSTANCE.addRoomName(info.roomId(), info.roomName());
                sb.append(String.format("- [%d] %s (인원: %d명)%n",
                        info.roomId(), info.roomName(), info.userCount()));
            }
        }
        sb.append("=======================================");
        view.println(sb.toString());
    }

    @EventListener
    public void onRoomEnter(EnterRoomSuccessEvent event) {
        String roomName = ClientSession.INSTANCE.getRoomName(event.roomId());
        String msg = ">> [" + roomName + "] 방에 입장했습니다.\n참여자: " + String.join(", ", event.users());
        view.println(msg);
    }

    @EventListener
    public void onRoomExit(ExitRoomSuccessEvent event) {
        view.println(">> [" + event.roomId() + "] 번 방에서 퇴장했습니다.");
    }

    @EventListener
    public void onPushRoomEnter(PushRoomEnterEvent event) {
        String roomName = ClientSession.INSTANCE.getRoomName(event.roomId());
        view.println(">> [" + roomName + "] 방에 " + event.userName() + "(" + event.userId() + ") 님이 입장했습니다.");
    }

    @EventListener
    public void onPushRoomExit(PushRoomExitEvent event) {
        String roomName = ClientSession.INSTANCE.getRoomName(event.roomId());
        view.println(">> [" + roomName + "] 방에서 " + event.userId() + " 님이 퇴장했습니다.");
    }
}