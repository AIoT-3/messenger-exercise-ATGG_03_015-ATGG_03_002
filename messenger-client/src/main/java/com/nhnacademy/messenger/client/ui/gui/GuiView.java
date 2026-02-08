package com.nhnacademy.messenger.client.ui.gui;

import com.nhnacademy.messenger.client.ui.View;
import com.nhnacademy.messenger.client.ui.gui.manager.PrivateChatManager;
import com.nhnacademy.messenger.client.ui.gui.manager.RoomChatManager;
import com.nhnacademy.messenger.client.ui.gui.panel.LoginPanel;
import com.nhnacademy.messenger.client.ui.gui.panel.RoomListPanel;
import com.nhnacademy.messenger.common.message.data.chat.MessageInfo;
import com.nhnacademy.messenger.common.message.data.room.RoomInfo;
import com.nhnacademy.messenger.common.message.data.user.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class GuiView implements View {

    private final LoginPanel loginPanel;
    private final RoomListPanel roomListPanel;
    private final RoomChatManager roomChatManager;
    private final PrivateChatManager privateChatManager;

    public void start() {
        SwingUtilities.invokeLater(() ->
            loginPanel.setVisible(true)
        );
    }

    public void switchView(JFrame targetFrame) {
        SwingUtilities.invokeLater(() -> {
            loginPanel.setVisible(false);
            roomListPanel.setVisible(false);

            if (targetFrame != null) {
                targetFrame.setVisible(true);
                targetFrame.toFront();
            }
        });
    }

    // --- User UI Methods ---

    public void showLoginSuccessDialog(String userName) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(loginPanel, "환영합니다, " + userName + "님!", "로그인 성공", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    public void clearLoginFields() {
        SwingUtilities.invokeLater(loginPanel::clearFields);
    }
    
    public void requestRoomListInitialData() {
        SwingUtilities.invokeLater(roomListPanel::requestInitialData);
    }

    public void showLogoutSuccessDialog() {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null, "성공적으로 로그아웃 되었습니다.");
        });
    }

    public void clearAllData() {
        SwingUtilities.invokeLater(() -> {
            roomListPanel.clearLists();
            roomChatManager.closeAll();
            privateChatManager.closeAll();
        });
    }
    
    public void updateUserList(List<UserInfo> users) {
        SwingUtilities.invokeLater(() -> roomListPanel.updateUserList(users));
    }
    
    public void showLoginPanel() {
        switchView(loginPanel);
    }
    
    public void showRoomListPanel() {
        switchView(roomListPanel);
    }

    // --- Room UI Methods ---
    
    public void updateRoomList(List<RoomInfo> rooms) {
        SwingUtilities.invokeLater(() -> roomListPanel.updateRoomList(rooms));
    }
    
    public String getRoomName(Long roomId) {
        return roomListPanel.getRoomName(roomId);
    }
    
    public void openChatRoom(Long roomId, String roomName) {
        SwingUtilities.invokeLater(() -> roomChatManager.openRoom(roomId, roomName));
    }
    
    public void closeChatRoom(Long roomId) {
        SwingUtilities.invokeLater(() -> roomChatManager.closeRoom(roomId));
    }

    // --- Chat UI Methods ---

    public void appendMessage(Long roomId, String sender, String content) {
        SwingUtilities.invokeLater(() -> roomChatManager.appendMessage(roomId, sender, content));
    }
    
    public boolean appendPrivateMessage(String senderId, String content) {
        return privateChatManager.receiveMessage(senderId, content);
    }
    
    public void setUserNotification(String userId, boolean active) {
        SwingUtilities.invokeLater(() -> roomListPanel.setUserNotification(userId, active));
    }
    
    public void appendHistory(Long roomId, List<MessageInfo> messages) {
        SwingUtilities.invokeLater(() -> {
            for (MessageInfo msg : messages) {
                roomChatManager.appendMessage(roomId, msg.senderName(), msg.content());
            }
        });
    }


    // --- Common ---

    @Override
    public void showSystemMessage(String message) {
        log.info("[System] {}", message);
        SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(roomListPanel, message, "시스템", JOptionPane.INFORMATION_MESSAGE)
        );
    }

    @Override
    public void showErrorMessage(String message) {
        log.info("[Error] {}", message);
        SwingUtilities.invokeLater(() -> 
            JOptionPane.showMessageDialog(loginPanel, message, "오류", JOptionPane.ERROR_MESSAGE)
        );
    }

    @Override
    public String readInput() {
        return "";
    }
}
