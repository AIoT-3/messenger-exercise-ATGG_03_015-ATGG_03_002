package com.nhnacademy.messenger.client.domain.room.listener;

import com.nhnacademy.messenger.client.domain.room.controller.ChatRoomController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// USER-LIST와 CHAT-ROOM-LIST를 동시에 call하여 UI를 업데이트
@Slf4j
@RequiredArgsConstructor
public class RefreshListener implements ActionListener {

    private final ChatRoomController controller;

    @Override
    public void actionPerformed(ActionEvent e) {
        log.info("새로고침 요청");
        try {
            controller.requestRoomList();
        } catch (Exception ex) {
            log.error("새로고침 실패", ex);
        }
    }
}


