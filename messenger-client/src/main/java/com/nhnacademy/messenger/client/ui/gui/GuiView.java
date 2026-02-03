package com.nhnacademy.messenger.client.ui.gui;

import com.nhnacademy.messenger.client.ui.View;
import com.nhnacademy.messenger.client.ui.gui.panel.LoginPanel;
import com.nhnacademy.messenger.client.ui.gui.panel.RoomChatPanel;
import com.nhnacademy.messenger.client.ui.gui.panel.RoomListPanel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class GuiView implements View {

    private final LoginPanel loginPanel;
    private final RoomListPanel roomListPanel;
    private final RoomChatPanel roomChatPanel;

    public void start() {
        SwingUtilities.invokeLater(() ->
            loginPanel.setVisible(true)
        );
    }

    private void switchView(JFrame targetFrame) {
        loginPanel.setVisible(false);
        roomListPanel.setVisible(false);
        roomChatPanel.setVisible(false);

        if (targetFrame != null) {
            targetFrame.setVisible(true);
            targetFrame.toFront();
        }
    }

    @Override
    public void showSystemMessage(String message) {
        log.info("[System] {}", message);
    }

    @Override
    public void showErrorMessage(String message) {
        SwingUtilities.invokeLater(() -> 
            JOptionPane.showMessageDialog(loginPanel, message, "Error", JOptionPane.ERROR_MESSAGE)
        );
    }

    @Override
    public void showLoginSuccess(String userName) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(loginPanel, "환영합니다, " + userName + "님!", "Login Success", JOptionPane.INFORMATION_MESSAGE);
            switchView(roomListPanel);
        });
    }

    @Override
    public void showLogoutSuccess() {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null, "성공적으로 로그아웃 되었습니다.");
            roomListPanel.clearLists(); // 로그아웃 시 목록 초기화
            switchView(loginPanel);
        });
    }

    @Override
    public void showRoomList(List<String> rooms) {

    }

    @Override
    public void showRoomEnterSuccess(String roomName) {
        SwingUtilities.invokeLater(() -> {
            roomChatPanel.setRoomTitle(roomName);
            switchView(roomChatPanel);
        });
    }

    @Override
    public void appendMessage(String sender, String content) {

    }
}
