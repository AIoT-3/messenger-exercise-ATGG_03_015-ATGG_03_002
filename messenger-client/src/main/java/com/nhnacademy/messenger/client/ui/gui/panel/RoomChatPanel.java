package com.nhnacademy.messenger.client.ui.gui.panel;

import com.nhnacademy.messenger.client.domain.room.service.ChatRoomClientService;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static com.nhnacademy.messenger.client.config.AppConstant.*;

@Slf4j
public class RoomChatPanel extends JFrame {

    // Constants
    private static final String TITLE_TEXT = "채팅방";
    private static final String TEXT_SEND = "전송";
    private static final String TEXT_EXIT = "나가기";
    private static final String TEXT_FILE = "파일";
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 500;
    private static final int TOP_HEIGHT = 40;
    private static final int INPUT_HEIGHT = 50;
    private static final int BUTTON_WIDTH = 80;
    private static final Font FONT_MESSAGE = new Font("Dialog", Font.PLAIN, 14);

    private JPanel messagePanel;
    private JTextField chatInputField;
    private JLabel roomTitleLabel;
    private JButton sendButton;
    private long roomId;

    private final ChatRoomClientService chatRoomClientService;

    public RoomChatPanel(long roomId, ChatRoomClientService chatRoomClientService) {
        super(TITLE_TEXT);
        this.chatRoomClientService = chatRoomClientService;
        this.roomId = roomId;

        initWindow();
        initUI();
    }

    private void initWindow() {
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                chatRoomClientService.exitRoom(roomId);
            }
        });
    }

    private void initUI() {
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.setBackground(SECONDARY_COLOR);

        contentPane.add(createTopPanel(), BorderLayout.NORTH);
        contentPane.add(createCenterPanel(), BorderLayout.CENTER);
        contentPane.add(createBottomPanel(), BorderLayout.SOUTH);
    }
    
    // 방 정보 업데이트 및 리셋
    public void updateRoomInfo(long roomId, String roomName) {
        this.roomId = roomId;
        setRoomTitle(roomName);
        
        // 메시지 영역 초기화
        if (messagePanel != null) {
            messagePanel.removeAll();
            messagePanel.revalidate();
            messagePanel.repaint();
        }
    }

    public void setRoomTitle(String title) {
        if (roomTitleLabel != null) {
            roomTitleLabel.setText(title);
        }
        setTitle(title);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PRIMARY_COLOR);

        JPanel leftSpacer = new JPanel();
        leftSpacer.setPreferredSize(new Dimension(BUTTON_WIDTH, TOP_HEIGHT));
        leftSpacer.setOpaque(false);
        panel.add(leftSpacer, BorderLayout.WEST);

        roomTitleLabel = new JLabel(TITLE_TEXT, SwingConstants.CENTER);
        roomTitleLabel.setFont(FONT_TITLE);
        roomTitleLabel.setForeground(TEXT_COLOR);
        panel.add(roomTitleLabel, BorderLayout.CENTER);

        JButton exitButton = new JButton(TEXT_EXIT);
        exitButton.setBackground(TRANSPARENT_COLOR);
        exitButton.setPreferredSize(new Dimension(BUTTON_WIDTH, TOP_HEIGHT));
        exitButton.addActionListener(e -> chatRoomClientService.exitRoom(roomId));

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightPanel.setOpaque(false);
        rightPanel.add(exitButton);
        panel.add(rightPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createCenterPanel() {
        JPanel chatPanel = new JPanel(new BorderLayout());
        chatPanel.setBackground(TRANSPARENT_COLOR);
        chatPanel.setOpaque(false);

        messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        messagePanel.setBackground(TRANSPARENT_COLOR);
        messagePanel.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(messagePanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(SECONDARY_COLOR);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        chatPanel.add(scrollPane, BorderLayout.CENTER);
        return chatPanel;
    }

    private JPanel createBottomPanel() {
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(SECONDARY_COLOR);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        chatInputField = new JTextField();
        chatInputField.setBackground(PRIMARY_COLOR);
        chatInputField.setForeground(TEXT_COLOR);
        chatInputField.setPreferredSize(new Dimension(0, INPUT_HEIGHT));
        chatInputField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // 엔터 입력 시 전송
        chatInputField.addActionListener(e -> sendMessage());

        JButton fileButton = new JButton(TEXT_FILE);
        fileButton.setBackground(TRANSPARENT_COLOR);
        fileButton.setForeground(SECONDARY_COLOR);
        fileButton.setPreferredSize(new Dimension(BUTTON_WIDTH, INPUT_HEIGHT));
        fileButton.addActionListener(e -> selectAndSendFile());

        sendButton = new JButton(TEXT_SEND);
        sendButton.setBackground(TRANSPARENT_COLOR);
        sendButton.setForeground(SECONDARY_COLOR);
        sendButton.setPreferredSize(new Dimension(BUTTON_WIDTH, INPUT_HEIGHT));
        sendButton.addActionListener(e -> sendMessage());

        inputPanel.add(fileButton, BorderLayout.WEST);
        inputPanel.add(chatInputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        
        return inputPanel;
    }

    private void selectAndSendFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("전송할 파일 선택");
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            try {
                chatRoomClientService.sendFile(this.roomId, filePath);
                JOptionPane.showMessageDialog(this, "파일 전송을 시작합니다: " + fileChooser.getSelectedFile().getName());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "파일 전송 실패: " + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void sendMessage() {
        String content = chatInputField.getText();
        if (content == null || content.trim().isEmpty()) return;

        chatRoomClientService.sendMessage(this.roomId, content);
        chatInputField.setText("");
    }

    public void addMessage(String userId, String text) {
        JLabel label;
        if ("시스템".equals(userId)) {
            label = new JLabel(text, SwingConstants.CENTER);
            label.setForeground(new Color(255, 255, 255, 180));
        } else {
            label = new JLabel(userId + ": " + text);
            label.setForeground(TEXT_COLOR);
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
        }
        label.setFont(FONT_MESSAGE);
        label.setBorder(BorderFactory.createEmptyBorder(4, 5, 4, 5));
        
        messagePanel.add(label);
        messagePanel.revalidate();
        messagePanel.repaint();

        SwingUtilities.invokeLater(() -> {
            if (messagePanel.getParent() != null && messagePanel.getParent().getParent() instanceof JScrollPane scrollPane) {
                JScrollBar vertical = scrollPane.getVerticalScrollBar();
                vertical.setValue(vertical.getMaximum());
            }
        });
    }
}

