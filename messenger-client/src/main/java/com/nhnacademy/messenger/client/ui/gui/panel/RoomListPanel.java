package com.nhnacademy.messenger.client.ui.gui.panel;

import com.nhnacademy.messenger.client.config.AppConstant;
import com.nhnacademy.messenger.client.domain.room.listener.RefreshListener;
import com.nhnacademy.messenger.client.domain.room.listener.CreateRoomListener;
import com.nhnacademy.messenger.client.domain.room.listener.EnterRoomListener;
import com.nhnacademy.messenger.client.domain.room.service.ChatRoomClientService;
import com.nhnacademy.messenger.client.domain.user.service.UserClientService;
import com.nhnacademy.messenger.client.domain.user.listener.LogoutListener;
import com.nhnacademy.messenger.common.message.data.room.RoomInfo;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
    private static final int SPACING_SMALL = 10;
    private static final int SPACING_MEDIUM = 15;
    
    // Fonts
    private static final Font FONT_TITLE = new Font("Dialog", Font.BOLD, 18);
    private static final Font FONT_SUBTITLE = new Font("Dialog", Font.BOLD, 14);

    // UI Components
    private JPanel roomListContainer;
    private JPanel userListContainer;

    private final ChatRoomClientService chatRoomClientService;
    private final UserClientService userClientService;

    private final Map<Long, RoomListItem> roomListMap = new HashMap<>();

    public RoomListPanel(UserClientService userClientService, ChatRoomClientService chatRoomClientService) {
        super(TITLE_TEXT);
        this.chatRoomClientService = chatRoomClientService;
        this.userClientService = userClientService;
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
        refreshButton.addActionListener(new RefreshListener(chatRoomClientService));
        panel.add(refreshButton, BorderLayout.EAST);
        
        return panel;
    }

    private JPanel createEastBottomPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 0, SPACING_SMALL));
        panel.setBackground(AppConstant.PRIMARY_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(SPACING_MEDIUM, 0, 0, 0));

        JButton createBtn = new JButton(TEXT_CREATE_ROOM);
        createBtn.addActionListener(new CreateRoomListener(chatRoomClientService));
        
        JButton logoutBtn = new JButton(TEXT_LOGOUT);
        logoutBtn.addActionListener(new LogoutListener(getContentPane(), userClientService));

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

    public void updateRoomList(List<RoomInfo> rooms) {
        if (rooms == null) return;

        Set<Long> currentRoomIds = rooms.stream()
                .map(RoomInfo::roomId)
                .collect(Collectors.toSet());

        roomListMap.keySet().removeIf(roomId -> !currentRoomIds.contains(roomId));

        for (RoomInfo info : rooms) {
            if (roomListMap.containsKey(info.roomId())) {
                roomListMap.get(info.roomId()).updateInfo(info);
            } else {
                roomListMap.put(info.roomId(), new RoomListItem(info, chatRoomController));
            }
        }

        roomListContainer.removeAll();
        for (RoomInfo info : rooms) {
            RoomListItem item = roomListMap.get(info.roomId());
            if (item != null) {
                roomListContainer.add(item);
                roomListContainer.add(Box.createRigidArea(new Dimension(0, SPACING_SMALL)));
            }
        }

        refresh();
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
    }

    /**
     * 새로운 roomList와 userList를 불러오기 전에 UI list들을 초기화합니다.
     */
    public void clearLists() {
        roomListContainer.removeAll();
        roomListMap.clear();

        userListContainer.removeAll();
    }

    public void refresh() {
        roomListContainer.revalidate();
        roomListContainer.repaint();

        userListContainer.revalidate();
        userListContainer.repaint();
    }
}
