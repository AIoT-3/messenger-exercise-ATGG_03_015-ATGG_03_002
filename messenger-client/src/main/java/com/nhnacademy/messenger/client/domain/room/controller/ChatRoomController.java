package com.nhnacademy.messenger.client.domain.room.controller;

import com.nhnacademy.messenger.client.domain.room.service.ChatRoomClientService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomClientService chatRoomClientService;

    public void createRoom(String roomName) {
        chatRoomClientService.createRoom(roomName);
    }
}
