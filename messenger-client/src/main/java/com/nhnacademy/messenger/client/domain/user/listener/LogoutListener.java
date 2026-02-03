package com.nhnacademy.messenger.client.domain.user.listener;

import com.nhnacademy.messenger.client.domain.user.controller.UserController;
import com.nhnacademy.messenger.common.message.data.room.ExitRoomRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Slf4j
@AllArgsConstructor
public class LogoutListener implements ActionListener {
    private final Container contentPane;
    private final UserController userController;

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
            log.info("리스너가 로그아웃을 호출");
            userController.logout();
        }
    }
}
