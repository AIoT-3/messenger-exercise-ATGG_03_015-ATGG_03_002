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

    // TODO: label view 전환
    private void switchView() {

    }

    @Override
    public void showSystemMessage(String message) {
        log.info("[System] {}", message);
        SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(roomListPanel, message, "알림", JOptionPane.INFORMATION_MESSAGE)
        );
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
            loginPanel.setVisible(false);
            roomListPanel.setVisible(true);
        });
    }

    @Override
    public void showRoomList(List<String> rooms) {
        SwingUtilities.invokeLater(() -> {
            roomListPanel.clearLists();
            if (rooms != null) {
                for (String roomString : rooms) {
                    try {
                        // Format: "[roomId] roomName (count명)"
                        int idEnd = roomString.indexOf("]");
                        long roomId = Long.parseLong(roomString.substring(1, idEnd));

                        int nameEnd = roomString.lastIndexOf("(");
                        String roomName = roomString.substring(idEnd + 2, nameEnd).trim();

                        roomListPanel.addRoomItem(roomId, roomName);
                    } catch (Exception e) {
                        log.warn("방 정보 파싱 실패: {}", roomString);
                        roomListPanel.addRoomItem(0, roomString);
                    }
                }
            }
        });
    }

    @Override
    public void showRoomEnterSuccess(String roomName) {

    }

    @Override
    public void appendMessage(String sender, String content) {

    }
}
