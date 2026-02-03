package com.nhnacademy.messenger.client.ui.gui.panel;

import com.nhnacademy.messenger.client.config.AppConstant;
import com.nhnacademy.messenger.client.domain.chat.listener.ChatMessageListener;
import com.nhnacademy.messenger.client.domain.room.listener.ExitRoomListener;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;

import java.awt.event.ActionListener;

@Slf4j
public class RoomChatPanel extends JFrame {

    // Constants
    private static final String TITLE_TEXT = "채팅방";
    private static final String TEXT_SEND = "전송";
    private static final String TEXT_EXIT = "나가기";
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 500;
    private static final int TOP_HEIGHT = 40;
    private static final int INPUT_HEIGHT = 50;
    private static final int BUTTON_WIDTH = 80;
    private static final int SPACING = 10;
    private static final Font FONT_MESSAGE = new Font("Dialog", Font.PLAIN, 14);

    private JPanel messagePanel;
    private JTextField chatInputField;
    private JLabel roomTitleLabel;
    private JButton sendButton; // 필드로 승격
    private long roomId; // final 제거

    public RoomChatPanel(long roomId) {
        super(TITLE_TEXT);
        this.roomId = roomId;

        initWindow();
        initUI();
    }

    private void initWindow() {
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void initUI() {
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.setBackground(AppConstant.SECONDARY_COLOR);

        contentPane.add(createTopPanel(), BorderLayout.NORTH);
        contentPane.add(createCenterPanel(), BorderLayout.CENTER);
        contentPane.add(createBottomPanel(), BorderLayout.SOUTH);
    }
    
    // 방 정보 업데이트 및 리셋
    public void updateRoomInfo(long roomId) {
        this.roomId = roomId;
        setRoomTitle("채팅방 " + roomId);
        
        // 메시지 영역 초기화
        if (messagePanel != null) {
            messagePanel.removeAll();
            messagePanel.revalidate();
            messagePanel.repaint();
        }

        // 전송 버튼 리스너 교체
        if (sendButton != null) {
            for (ActionListener al : sendButton.getActionListeners()) {
                sendButton.removeActionListener(al);
            }
            sendButton.addActionListener(new ChatMessageListener(roomId, chatInputField));
        }
    }

    public void setRoomTitle(String title) {
        if (roomTitleLabel != null) {
            roomTitleLabel.setText(title);
        }
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(AppConstant.PRIMARY_COLOR);

        // Left spacer for balancing center alignment
        JPanel leftSpacer = new JPanel();
        leftSpacer.setPreferredSize(new Dimension(BUTTON_WIDTH, TOP_HEIGHT));
        leftSpacer.setOpaque(false);
        panel.add(leftSpacer, BorderLayout.WEST);

        // Title
        roomTitleLabel = new JLabel(TITLE_TEXT, SwingConstants.CENTER);
        roomTitleLabel.setFont(new Font("Dialog", Font.BOLD, 18));
        roomTitleLabel.setForeground(AppConstant.TEXT_COLOR);
        panel.add(roomTitleLabel, BorderLayout.CENTER);

        // Exit Button
        JButton exitButton = new JButton(TEXT_EXIT);
        exitButton.setBackground(AppConstant.TRANSPARENT_COLOR);
        exitButton.setPreferredSize(new Dimension(BUTTON_WIDTH, TOP_HEIGHT));
        exitButton.addActionListener(new ExitRoomListener(roomId, getContentPane()));

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightPanel.setOpaque(false);
        rightPanel.add(exitButton);
        panel.add(rightPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createCenterPanel() {
        JPanel chatPanel = new JPanel(new BorderLayout());
        chatPanel.setBackground(AppConstant.TRANSPARENT_COLOR);
        chatPanel.setOpaque(false);

        messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        messagePanel.setBackground(AppConstant.TRANSPARENT_COLOR);
        messagePanel.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(messagePanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(AppConstant.SECONDARY_COLOR);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        chatPanel.add(scrollPane, BorderLayout.CENTER);
        return chatPanel;
    }

    private JPanel createBottomPanel() {
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(AppConstant.SECONDARY_COLOR);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        chatInputField = new JTextField();
        chatInputField.setBackground(AppConstant.PRIMARY_COLOR);
        chatInputField.setForeground(AppConstant.TEXT_COLOR);
        chatInputField.setPreferredSize(new Dimension(0, INPUT_HEIGHT));
        chatInputField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        sendButton = new JButton(TEXT_SEND); // 필드 사용
        sendButton.setBackground(AppConstant.TRANSPARENT_COLOR);
        sendButton.setForeground(AppConstant.SECONDARY_COLOR);
        sendButton.setPreferredSize(new Dimension(BUTTON_WIDTH, INPUT_HEIGHT));
        sendButton.addActionListener(new ChatMessageListener(roomId, chatInputField));

        inputPanel.add(chatInputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        
        return inputPanel;
    }

    /**
     * 채팅에 새 메세지를 추가합니다.
     * @param userId 유저 ID
     * @param text 채팅 내용
     */
    public void addMessage(String userId, String text) {
        JLabel label = new JLabel(userId + ": " + text);
        label.setFont(FONT_MESSAGE);
        label.setForeground(AppConstant.TEXT_COLOR);
        label.setBorder(BorderFactory.createEmptyBorder(4, 5, 4, 5));
        
        messagePanel.add(label);
        messagePanel.revalidate();
        messagePanel.repaint();
        
        // Auto-scroll to bottom
        SwingUtilities.invokeLater(() -> {
            JScrollBar vertical = ((JScrollPane) messagePanel.getParent().getParent()).getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });
    }
}

