package com.nhnacademy.messenger.client.ui.gui.panel;

import com.nhnacademy.messenger.client.domain.room.service.ChatRoomClientService;
import com.nhnacademy.messenger.client.session.ClientSession;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;

import static com.nhnacademy.messenger.client.config.AppConstant.*;

@Slf4j
public class PrivateChatPanel extends JFrame {

    private static final String TEXT_SEND = "전송";
    private static final int WINDOW_WIDTH = 400;
    private static final int WINDOW_HEIGHT = 500;
    private static final int INPUT_HEIGHT = 50;
    private static final int BUTTON_WIDTH = 80;

    private JPanel messagePanel;
    private JTextField chatInputField;
    private JLabel titleLabel;
    
    private final String targetUserId;
    private final ChatRoomClientService chatRoomClientService;

    public PrivateChatPanel(String targetUserId, ChatRoomClientService chatRoomClientService) {
        super("1:1 대화 - " + targetUserId);
        this.targetUserId = targetUserId;
        this.chatRoomClientService = chatRoomClientService;

        initWindow();
        initUI();
    }

    private void initWindow() {
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
    }

    private void initUI() {
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.setBackground(SECONDARY_COLOR);

        contentPane.add(createTopPanel(), BorderLayout.NORTH);
        contentPane.add(createCenterPanel(), BorderLayout.CENTER);
        contentPane.add(createBottomPanel(), BorderLayout.SOUTH);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PRIMARY_COLOR);
        panel.setPreferredSize(new Dimension(0, 40));

        titleLabel = new JLabel(targetUserId + " 님과의 대화", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        titleLabel.setForeground(TEXT_COLOR);
        panel.add(titleLabel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(SECONDARY_COLOR);

        messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        messagePanel.setBackground(SECONDARY_COLOR);

        JScrollPane scrollPane = new JScrollPane(messagePanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(SECONDARY_COLOR);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(SECONDARY_COLOR);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        chatInputField = new JTextField();
        chatInputField.setBackground(PRIMARY_COLOR);
        chatInputField.setForeground(TEXT_COLOR);
        chatInputField.setPreferredSize(new Dimension(0, INPUT_HEIGHT));
        chatInputField.addActionListener(e -> sendMessage());

        JButton sendButton = new JButton(TEXT_SEND);
        sendButton.setPreferredSize(new Dimension(BUTTON_WIDTH, INPUT_HEIGHT));
        sendButton.addActionListener(e -> sendMessage());

        inputPanel.add(chatInputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        
        return inputPanel;
    }

    private void sendMessage() {
        String content = chatInputField.getText();
        if (content == null || content.trim().isEmpty()) return;

        chatRoomClientService.sendPrivateMessage(targetUserId, content);
        addMessage(ClientSession.INSTANCE.getUserId(), content); // 내가 보낸 건 바로 추가
        chatInputField.setText("");
    }

    public void addMessage(String senderId, String text) {
        JLabel label = new JLabel(senderId + ": " + text);
        label.setFont(FONT_MESSAGE);
        label.setForeground(TEXT_COLOR);
        label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
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
