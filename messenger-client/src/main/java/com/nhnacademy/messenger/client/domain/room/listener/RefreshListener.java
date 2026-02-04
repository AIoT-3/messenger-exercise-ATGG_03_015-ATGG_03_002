package com.nhnacademy.messenger.client.domain.room.listener;

import com.nhnacademy.messenger.client.domain.room.service.ChatRoomClientService;
import com.nhnacademy.messenger.client.domain.user.service.UserClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// USER-LIST와 CHAT-ROOM-LIST를 동시에 call하여 UI를 업데이트
@Slf4j
@RequiredArgsConstructor
public class RefreshListener implements ActionListener {

    private final ChatRoomClientService chatRoomClientService;
    private final UserClientService userClientService;
    private static final int COOLDOWN_MS = 3000;

    @Override
    public void actionPerformed(ActionEvent e) {

        // 응답 리퀘스트가 빈번해지는 것을 막기 위해 3초에 1번만 누를 수 있도록 변경
        Object source = e.getSource();
        if (source instanceof JButton button) {
            button.setEnabled(false);
            Timer timer = new Timer(COOLDOWN_MS, event -> button.setEnabled(true));
            timer.setRepeats(false);
            timer.start();
        }

        log.info("새로고침 요청");
        try {
            chatRoomClientService.getRoomList();
            userClientService.getUserList();
        } catch (Exception ex) {
            log.error("새로고침 실패", ex);
        }
    }
}


