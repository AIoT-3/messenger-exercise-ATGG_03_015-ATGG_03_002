package com.nhnacademy.messenger.client.domain.room.listener;

import com.nhnacademy.messenger.client.domain.room.controller.ChatRoomController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Slf4j
@RequiredArgsConstructor
public class CreateRoomListener implements ActionListener {

    private final ChatRoomController chatRoomController;

    @Override
    public void actionPerformed(ActionEvent e) {
        String roomName = JOptionPane.showInputDialog(null, "생성할 채팅방 이름을 입력하세요:", "방 생성", JOptionPane.QUESTION_MESSAGE);

        if (StringUtils.isBlank(roomName)) {
            return;
        }

        try {
            chatRoomController.requestCreateRoom(roomName.trim());
        } catch (Exception ex) {
            log.error("방 생성 요청 실패", ex);
            JOptionPane.showMessageDialog(null, "방 생성 요청 중 오류가 발생했습니다: " + ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
}
