package com.nhnacademy.messenger.client.ui.panel;

import com.nhnacademy.messenger.client.config.ClientConstant;
import com.nhnacademy.messenger.client.ui.listener.RoomExitButtonEventListener;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Slf4j
public class RoomChatPanel extends JFrame {
    private static final int USERLIST_PANEL_WIDTH = 200;

    private JPanel userListPanel;
    private JPanel messagePanel;

    public RoomChatPanel() {
        super("Test Text");

        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        getContentPane().setBackground(ClientConstant.SECONDARY_COLOR);
        createUIComponents();
    }

    private void createUIComponents() {
        // TODO: UI 구성 코드 작성
        addUserListUI();
        addChatUI();
    }

    private void addUserListUI() {
        // 1. userListPanel 설정 (전체 틀: BorderLayout)
        userListPanel = new JPanel();
        userListPanel.setLayout(new BorderLayout()); // 상/중/하 분리를 위해 변경
        userListPanel.setBackground(ClientConstant.PRIMARY_COLOR);
        userListPanel.setPreferredSize(new Dimension(USERLIST_PANEL_WIDTH, getHeight()));

        JLabel titleLabel = new JLabel("유저 목록", SwingConstants.CENTER); // 텍스트 중앙 정렬
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 16)); // 폰트 설정
        titleLabel.setForeground(ClientConstant.TEXT_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        userListPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS)); // 목록은 세로로 쌓임
        listContainer.setBackground(ClientConstant.PRIMARY_COLOR);

        JScrollPane scrollPane = new JScrollPane(listContainer);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(ClientConstant.PRIMARY_COLOR); // 배경색 통일

        userListPanel.add(scrollPane, BorderLayout.CENTER);

        JButton exitButton = new JButton("채팅방 나가기");

        exitButton.setBackground(ClientConstant.TRANSPARENT_COLOR);
        exitButton.setForeground(ClientConstant.SECONDARY_COLOR);
        exitButton.setPreferredSize(new Dimension(USERLIST_PANEL_WIDTH, 50)); // 버튼 높이 지정

        // TODO: 액션 리스너 분리
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(
                        getContentPane(),
                        "정말로 나가시겠습니까?",
                        "나가기 확인",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

                // '예(Yes)'를 눌렀을 때의 동작
                if (choice == JOptionPane.YES_OPTION) {
                    log.info("채팅방을 나갑니다.");
                }
            }
        });

        userListPanel.add(exitButton, BorderLayout.SOUTH);

        add(userListPanel, BorderLayout.WEST);
    }

    private void addChatUI() {
        JPanel chatPanel = new JPanel();
        chatPanel.setLayout(new BorderLayout());
        chatPanel.setBackground(ClientConstant.TRANSPARENT_COLOR);

        messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        messagePanel.setBackground(ClientConstant.TRANSPARENT_COLOR);
        chatPanel.add(messagePanel, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.setBackground(ClientConstant.SECONDARY_COLOR);

        JTextField inputField = new JTextField();
        inputField.setBackground(ClientConstant.PRIMARY_COLOR);
        inputField.setForeground(ClientConstant.TEXT_COLOR);

        inputField.setPreferredSize(new Dimension(0, 50));

        JButton sendButton = new JButton("전송");
        sendButton.setBackground(ClientConstant.TRANSPARENT_COLOR);
        sendButton.setForeground(ClientConstant.SECONDARY_COLOR);
        sendButton.addActionListener(e -> {
            // TODO: 채팅 전송 이벤트
        });


        sendButton.setPreferredSize(new Dimension(80, 50));

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        chatPanel.add(inputPanel, BorderLayout.SOUTH);
        add(chatPanel, BorderLayout.CENTER);
    }
}
