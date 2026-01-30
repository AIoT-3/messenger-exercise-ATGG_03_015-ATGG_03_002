package com.nhnacademy.messenger.client.ui.listener;

import com.nhnacademy.messenger.common.message.data.room.ChatRoomExitRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Slf4j
@AllArgsConstructor
public class RoomExitButtonEventListener implements ActionListener {
    private long roomId;
    private Container contentPane;


    @Override
    public void actionPerformed(ActionEvent e) {
        int choice = JOptionPane.showConfirmDialog(
                contentPane,
                "정말로 나가시겠습니까?",
                "나가기 확인",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        // '예(Yes)'를 눌렀을 때의 동작
        if (choice == JOptionPane.YES_OPTION) {
            log.info("채팅방을 나갑니다.");
            ChatRoomExitRequest chatRoomExitRequest = new ChatRoomExitRequest(roomId);
        }
    }
}
