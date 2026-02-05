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

    private void switchView(JFrame targetFrame) {
        loginPanel.setVisible(false);
        roomListPanel.setVisible(false);

        if (targetFrame != null) {
            targetFrame.setVisible(true);
            targetFrame.toFront();
        }
    }

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
    public void showLoginSuccess(String userName) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(loginPanel, "환영합니다, " + userName + "님!", "로그인 성공", JOptionPane.INFORMATION_MESSAGE);
            loginPanel.clearFields();
            switchView(roomListPanel);
            roomListPanel.requestInitialData();
        });
    }

    @Override
    public void showLogoutSuccess() {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null, "성공적으로 로그아웃 되었습니다.");
            roomListPanel.clearLists(); // 로그아웃 시 목록 초기화
            roomChatManager.closeAll();
            privateChatManager.closeAll();
            switchView(loginPanel);
        });
    }

    public void appendPrivateMessage(String senderId, String content) {
        SwingUtilities.invokeLater(() -> {
            boolean isChatVisible = privateChatManager.receiveMessage(senderId, content);
            
            if (!isChatVisible) {
                roomListPanel.setUserNotification(senderId, true);
            }
        });
    }


    @Override
    public void showUserList(List<UserInfo> users) {
        SwingUtilities.invokeLater(() -> {
            roomListPanel.updateUserList(users);
        });
    }

    @Override
    public void showRoomList(List<RoomInfo> rooms) {
        SwingUtilities.invokeLater(() -> {
            roomListPanel.updateRoomList(rooms);
        });
    }

    @Override
    public void showRoomEnterSuccess(Long roomId, List<String> users) {
        SwingUtilities.invokeLater(() -> {
            String roomName = roomListPanel.getRoomName(roomId);
            roomChatManager.openRoom(roomId, roomName);
        });
    }

    @Override
    public void showRoomExitSuccess(Long roomId) {
        SwingUtilities.invokeLater(() -> {
            roomChatManager.closeRoom(roomId);
            showSystemMessage("채팅방(ID:" + roomId + ")에서 퇴장했습니다.");
        });
    }

    @Override
    public void showPushRoomEnter(Long roomId, String userId, String userName) {
        SwingUtilities.invokeLater(() -> {
            roomChatManager.appendMessage(roomId, "시스템", userName + "(" + userId + ") 님이 입장했습니다.");
        });
    }

    @Override
    public void showPushRoomExit(Long roomId, String userId) {
        SwingUtilities.invokeLater(() -> {
            roomChatManager.appendMessage(roomId, "시스템", userId + " 님이 퇴장했습니다.");
        });
    }

    @Override
    public void appendMessage(Long roomId, Long messageId, String sender, String content) {
        SwingUtilities.invokeLater(() -> {
            roomChatManager.appendMessage(roomId, sender, content);
        });
    }

    @Override
    public void showChatHistory(Long roomId, List<MessageInfo> messages, boolean hasMore) {
        SwingUtilities.invokeLater(() -> {
            for (MessageInfo msg : messages) {
                roomChatManager.appendMessage(roomId, msg.senderName(), msg.content());
            }
        });
    }
}
