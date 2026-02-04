package com.nhnacademy.messenger.client.ui.gui.panel;

import com.nhnacademy.messenger.common.message.data.user.UserInfo;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;

import static com.nhnacademy.messenger.client.config.AppConstant.*;

public class UserListItem extends JButton {
    private static final int ITEM_HEIGHT = 30;
    
    @Getter
    private final String userId;
    @Getter
    private final String userName;
    private boolean hasNewMessage = false;
    private boolean online;

    private final JLabel nameLabel;
    private final JLabel badgeLabel;

    public UserListItem(UserInfo userInfo) {
        this.userId = userInfo.id();
        this.userName = userInfo.name();
        this.online = userInfo.online() != null && userInfo.online();
        
        // 레이아웃 및 컴포넌트 설정
        this.setLayout(new BorderLayout(SPACING_SMALL, 0));
        this.setBorder(BorderFactory.createEmptyBorder(0, SPACING_SMALL, 0, SPACING_SMALL));

        nameLabel = new JLabel();
        badgeLabel = new JLabel();
        
        this.add(nameLabel, BorderLayout.CENTER);
        this.add(badgeLabel, BorderLayout.EAST);

        updateDisplay();
        initStyle();
        
        Dimension size = new Dimension(Integer.MAX_VALUE, ITEM_HEIGHT);
        this.setMaximumSize(size);
        this.setMinimumSize(new Dimension(0, ITEM_HEIGHT));
        this.setPreferredSize(new Dimension(0, ITEM_HEIGHT));
    }

    public void updateStatus(boolean online) {
        this.online = online;
        this.setEnabled(online);
        updateDisplay();
    }

    public void setNotification(boolean hasNew) {
        this.hasNewMessage = hasNew;
        updateDisplay();
    }

    private void updateDisplay() {
        String statusIcon = online ? "🟢 " : "🔴 ";
        nameLabel.setText(statusIcon + userId + " (" + userName + ")");
        nameLabel.setForeground(online ? TEXT_COLOR : Color.GRAY);

        if (hasNewMessage) {
            badgeLabel.setText("❗ ");
            badgeLabel.setForeground(Color.YELLOW);
        } else {
            badgeLabel.setText("");
        }
    }

    private void initStyle() {
        this.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.setHorizontalAlignment(SwingConstants.LEFT);
        this.setOpaque(true);
        this.setBorderPainted(false);
        this.setBackground(PRIMARY_COLOR);
        this.setFocusPainted(false);
        this.setRolloverEnabled(true);

        this.addChangeListener(e -> {
            if (!isEnabled()) return;
            ButtonModel model = this.getModel();
            if (model.isPressed()) {
                this.setBackground(PRIMARY_COLOR.darker());
            } else if (model.isRollover()) {
                this.setBackground(PRIMARY_COLOR.brighter());
            } else {
                this.setBackground(PRIMARY_COLOR);
            }
        });
    }
}
