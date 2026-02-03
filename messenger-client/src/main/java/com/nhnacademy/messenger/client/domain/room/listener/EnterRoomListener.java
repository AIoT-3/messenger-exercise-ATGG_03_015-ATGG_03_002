package com.nhnacademy.messenger.client.domain.room.listener;

import com.nhnacademy.messenger.client.domain.room.controller.ChatRoomController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Slf4j
@RequiredArgsConstructor
public class EnterRoomListener implements ActionListener {

    private final ChatRoomController controller;
    private final long roomId;

    @Override
    public void actionPerformed(ActionEvent e) {
        log.debug("채팅방 입장 요청: roomId={}", roomId);
        controller.requestEnterRoom(roomId);
    }
}
