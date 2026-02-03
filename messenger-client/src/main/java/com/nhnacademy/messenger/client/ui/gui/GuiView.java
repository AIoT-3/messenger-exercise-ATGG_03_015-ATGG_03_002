package com.nhnacademy.messenger.client.ui.gui;

import com.nhnacademy.messenger.client.ui.View;
import com.nhnacademy.messenger.client.ui.gui.panel.LoginPanel;
import com.nhnacademy.messenger.client.ui.gui.panel.RoomChatPanel;
import com.nhnacademy.messenger.client.ui.gui.panel.RoomListPanel;
import com.nhnacademy.messenger.common.message.data.room.RoomInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class GuiView implements View {

    // 나중에 Panel 전용 Manager로 관리하기
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
            loginPanel.clearFields();
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
    public void showRoomList(List<RoomInfo> rooms) {
        SwingUtilities.invokeLater(() -> {
            roomListPanel.clearLists();
            if (rooms != null) {
                for (RoomInfo info : rooms) {
                    String displayName = String.format("%s (%d명)", info.roomName(), info.userCount());
                    roomListPanel.addRoomItem(info.roomId(), displayName);
                }
            }
        });
    }

    @Override
    public void showRoomEnterSuccess(Long roomId, List<String> users) {
        SwingUtilities.invokeLater(() -> {
            roomChatPanel.updateRoomInfo(roomId);
            switchView(roomChatPanel);
        });
    }

    @Override
    public void appendMessage(String sender, String content) {

    }
}
