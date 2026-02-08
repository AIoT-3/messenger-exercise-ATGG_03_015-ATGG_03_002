package com.nhnacademy.messenger.client.domain.room.ui.gui;

import com.nhnacademy.messenger.client.domain.room.event.*;
import com.nhnacademy.messenger.client.ui.gui.GuiView;
import com.nhnacademy.messenger.common.event.EventListener;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RoomGuiListener {

    private final GuiView view;

    @EventListener
    public void onRoomCreated(CreateRoomSuccessEvent event) {
        view.showSystemMessage("채팅방이 생성되었습니다: " + event.roomName() + " (ID: " + event.roomId() + ")");
    }

    @EventListener
    public void onRoomListReceived(ListRoomSuccessEvent event) {
        view.updateRoomList(event.roomList());
    }

    @EventListener
    public void onRoomEnter(EnterRoomSuccessEvent event) {
        String roomName = view.getRoomName(event.roomId());
        view.openChatRoom(event.roomId(), roomName);
    }

    @EventListener
    public void onRoomExit(ExitRoomSuccessEvent event) {
        view.closeChatRoom(event.roomId());
        view.showSystemMessage("채팅방(ID:" + event.roomId() + ")에서 퇴장했습니다.");
    }

    @EventListener
    public void onPushRoomEnter(PushRoomEnterEvent event) {
        view.appendMessage(event.roomId(), "시스템", event.userName() + "(" + event.userId() + ") 님이 입장했습니다.");
    }

    @EventListener
    public void onPushRoomExit(PushRoomExitEvent event) {
        view.appendMessage(event.roomId(), "시스템", event.userId() + " 님이 퇴장했습니다.");
    }
}
