package com.nhnacademy.messenger.client.domain.room.listener;

import com.nhnacademy.messenger.client.domain.room.controller.ChatRoomController;
import com.nhnacademy.messenger.common.message.data.room.ExitRoomRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Slf4j
@AllArgsConstructor
public class ExitRoomListener implements ActionListener {
    private long roomId;
    private Container contentPane;
    private ChatRoomController chatRoomController; // Add Controller

    // Keep existing constructor for compatibility or update usage
    public ExitRoomListener(long roomId, Container contentPane) {
        this.roomId = roomId;
        this.contentPane = contentPane;
    }

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
            log.info("채팅방을 나갑니다: {}", roomId);
            if (chatRoomController != null) {
                chatRoomController.requestExitRoom(roomId);
            } else {
                log.error("ChatRoomController is null!");
            }
        }
    }
}
