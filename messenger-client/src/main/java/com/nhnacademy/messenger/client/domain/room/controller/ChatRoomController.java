package com.nhnacademy.messenger.client.domain.room.controller;

import com.nhnacademy.messenger.client.domain.room.service.ChatRoomClientService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomClientService chatRoomClientService;

    public void requestCreateRoom(String roomName) {
        chatRoomClientService.createRoom(roomName);
    }

    public void requestRoomList() {
        chatRoomClientService.getRoomList();
    }

    public void requestEnterRoom(long roomId) {
        chatRoomClientService.enterRoom(roomId);
    }
}
