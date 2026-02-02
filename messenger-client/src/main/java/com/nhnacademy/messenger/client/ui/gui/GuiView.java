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
        SwingUtilities.invokeLater(() -> {
            loginPanel.setVisible(true);
        });
    }

    private void switchView() {

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
            // TODO: 로그인 했을 때 로그인 창 삭제 후 채팅방 리스트 창 띄우기
            // loginPanel.dispose();
        });
    }

    @Override
    public void showLoginFail() {
        SwingUtilities.invokeLater(() -> 
            JOptionPane.showMessageDialog(loginPanel, "Login Failed", "Error", JOptionPane.ERROR_MESSAGE)
        );
    }

    @Override
    public void showRoomList(List<String> rooms) {

    }

    @Override
    public void showRoomEnterSuccess(String roomName) {

    }

    @Override
    public void appendMessage(String sender, String content) {

    }
}
