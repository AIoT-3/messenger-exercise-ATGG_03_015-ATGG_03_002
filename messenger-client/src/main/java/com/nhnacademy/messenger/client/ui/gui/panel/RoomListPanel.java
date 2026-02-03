package com.nhnacademy.messenger.client.ui.gui.panel;

import com.nhnacademy.messenger.client.config.AppConstant;
import com.nhnacademy.messenger.client.domain.room.listener.RefreshListener;
import com.nhnacademy.messenger.client.domain.room.listener.RoomCreateListener;
import com.nhnacademy.messenger.client.domain.room.listener.RoomEnterListener;
import com.nhnacademy.messenger.client.domain.user.controller.UserController;
import com.nhnacademy.messenger.client.domain.user.listener.LogoutListener;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;

@Slf4j
public class RoomListPanel extends JFrame {

    // Constants for UI Texts
    private static final String TITLE_TEXT = "메신저 로비";
    private static final String TEXT_ROOM_LIST = "채팅방 목록";
    private static final String TEXT_USER_LIST = "접속자 목록";
    private static final String TEXT_REFRESH = "↻";
    private static final String TOOLTIP_REFRESH = "새로고침";
    private static final String TEXT_CREATE_ROOM = "방 생성";
    private static final String TEXT_LOGOUT = "로그아웃";

    // Constants for Dimensions & Layout
    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 600;
    private static final int MIN_WINDOW_WIDTH = 500;
    private static final int MIN_WINDOW_HEIGHT = 400;
    
    private static final int USER_LIST_PANEL_WIDTH = 200;
    private static final int ROOM_BUTTON_HEIGHT = 50;
    private static final int SPACING_SMALL = 10;
    private static final int SPACING_MEDIUM = 15;
    
    // Fonts
    private static final Font FONT_TITLE = new Font("Dialog", Font.BOLD, 18);
    private static final Font FONT_SUBTITLE = new Font("Dialog", Font.BOLD, 14);

    // UI Components
    private JPanel roomListContainer;
    private JPanel userListContainer;
    private final UserController userController;

    public RoomListPanel(UserController userController) {
        super(TITLE_TEXT);
        this.userController = userController;
        initWindow();
        initUI();
    }

    private void initWindow() {
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setMinimumSize(new Dimension(MIN_WINDOW_WIDTH, MIN_WINDOW_HEIGHT));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void initUI() {
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.setBackground(AppConstant.SECONDARY_COLOR);

        contentPane.add(createCenterPanel(), BorderLayout.CENTER);
        contentPane.add(createEastPanel(), BorderLayout.EAST);
    }

    // ===== Center Panel (Room List) =====
    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(AppConstant.TRANSPARENT_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(SPACING_MEDIUM, SPACING_MEDIUM, SPACING_MEDIUM, SPACING_MEDIUM));

        JLabel titleLabel = new JLabel(TEXT_ROOM_LIST);
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setForeground(AppConstant.TEXT_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, SPACING_MEDIUM, 0));
        panel.add(titleLabel, BorderLayout.NORTH);

        roomListContainer = createListContainer();
        JScrollPane scrollPane = createScrollPane(roomListContainer);
        scrollPane.setBorder(BorderFactory.createLineBorder(AppConstant.PRIMARY_COLOR));

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    // ===== East Panel (User List & Buttons) =====
    private JPanel createEastPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(USER_LIST_PANEL_WIDTH, 0));
        panel.setBackground(AppConstant.PRIMARY_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(SPACING_MEDIUM, SPACING_MEDIUM, SPACING_MEDIUM, SPACING_MEDIUM));

        panel.add(createEastTopPanel(), BorderLayout.NORTH);
        
        userListContainer = createListContainer();
        userListContainer.setBackground(AppConstant.PRIMARY_COLOR); // Override background
        JScrollPane scrollPane = createScrollPane(userListContainer);
        scrollPane.getViewport().setBackground(AppConstant.PRIMARY_COLOR);
        panel.add(scrollPane, BorderLayout.CENTER);

        panel.add(createEastBottomPanel(), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createEastTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(AppConstant.PRIMARY_COLOR);
        
        JLabel title = new JLabel(TEXT_USER_LIST);
        title.setForeground(AppConstant.TEXT_COLOR);
        title.setFont(FONT_SUBTITLE);
        panel.add(title, BorderLayout.CENTER);

        JButton refreshButton = new JButton(TEXT_REFRESH);
        refreshButton.setToolTipText(TOOLTIP_REFRESH);
        refreshButton.addActionListener(new RefreshListener());
        panel.add(refreshButton, BorderLayout.EAST);
        
        return panel;
    }

    private JPanel createEastBottomPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 0, SPACING_SMALL));
        panel.setBackground(AppConstant.PRIMARY_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(SPACING_MEDIUM, 0, 0, 0));

        JButton createBtn = new JButton(TEXT_CREATE_ROOM);
        createBtn.addActionListener(new RoomCreateListener());
        
        JButton logoutBtn = new JButton(TEXT_LOGOUT);
        logoutBtn.addActionListener(new LogoutListener(getContentPane(), userController));

        panel.add(createBtn);
        panel.add(logoutBtn);
        return panel;
    }

    // ===== Component Helpers =====
    private JPanel createListContainer() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(AppConstant.SECONDARY_COLOR);
        return container;
    }

    private JScrollPane createScrollPane(Component view) {
        JScrollPane scrollPane = new JScrollPane(view);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(AppConstant.SECONDARY_COLOR);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        return scrollPane;
    }

    // ===== Public Methods =====

    /**
     * RoomListUI의 roomList에 room을 추가합니다.
     * @param roomId 고유 방 번호
     * @param roomName 표시 될 방 이름
     */
    public void addRoomItem(long roomId, String roomName) {
        JButton roomButton = new JButton(roomName);
        styleButton(roomButton);
        
        // Fixed Size Logic
        Dimension size = new Dimension(Integer.MAX_VALUE, ROOM_BUTTON_HEIGHT);
        roomButton.setMaximumSize(size);
        roomButton.setMinimumSize(new Dimension(0, ROOM_BUTTON_HEIGHT));
        roomButton.setPreferredSize(new Dimension(0, ROOM_BUTTON_HEIGHT));

        // CHAT-ROOM-ENTER 전송을 위한 리스너
        roomButton.addActionListener(new RoomEnterListener());

        roomListContainer.add(roomButton);
        roomListContainer.add(Box.createRigidArea(new Dimension(0, SPACING_SMALL)));
        refreshContainer(roomListContainer);
    }

    /**
     * RoomListUI의 userList에 user를 추가합니다.
     * @param userId 표시 될 유저 ID
     */
    public void addUserItem(String userId) {
        JLabel userLabel = new JLabel(userId);
        userLabel.setForeground(AppConstant.TEXT_COLOR);
        userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        userLabel.setBorder(BorderFactory.createEmptyBorder(SPACING_SMALL, SPACING_SMALL, SPACING_SMALL, SPACING_SMALL));
        
        userListContainer.add(userLabel);
        refreshContainer(userListContainer);
    }

    /**
     * 새로운 roomList와 userList를 불러오기 전에 UI list들을 초기화합니다.
     */
    public void clearLists() {
        roomListContainer.removeAll();
        userListContainer.removeAll();
        refreshContainer(roomListContainer);
        refreshContainer(userListContainer);
    }

    private void styleButton(JButton button) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setBackground(AppConstant.PRIMARY_COLOR);
        button.setForeground(AppConstant.TEXT_COLOR);
        button.setFocusPainted(false);
        button.setRolloverEnabled(true);

        button.addChangeListener(e -> {
            ButtonModel model = button.getModel();
            if (model.isPressed()) {
                button.setBackground(AppConstant.PRIMARY_COLOR.darker());
            } else if (model.isRollover()) {
                button.setBackground(AppConstant.PRIMARY_COLOR.brighter());
            } else {
                button.setBackground(AppConstant.PRIMARY_COLOR);
            }
        });
    }
    
    private void refreshContainer(JPanel container) {
        container.revalidate();
        container.repaint();
    }
}
