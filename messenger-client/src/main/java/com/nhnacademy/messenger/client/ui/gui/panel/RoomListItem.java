package com.nhnacademy.messenger.client.ui.gui.panel;

import com.nhnacademy.messenger.client.config.AppConstant;
import com.nhnacademy.messenger.client.domain.room.listener.EnterRoomListener;
import com.nhnacademy.messenger.client.domain.room.service.ChatRoomClientService;
import com.nhnacademy.messenger.common.message.data.room.RoomInfo;

import javax.swing.*;
import java.awt.*;

public class RoomListItem extends JButton {
    private static final int ROOM_BUTTON_HEIGHT = 50;
    
    public RoomListItem(RoomInfo info, ChatRoomClientService chatRoomClientService) {
        updateInfo(info);
        initStyle();
        
        // Fixed Size Logic
        Dimension size = new Dimension(Integer.MAX_VALUE, ROOM_BUTTON_HEIGHT);
        this.setMaximumSize(size);
        this.setMinimumSize(new Dimension(0, ROOM_BUTTON_HEIGHT));
        this.setPreferredSize(new Dimension(0, ROOM_BUTTON_HEIGHT));

        // CHAT-ROOM-ENTER 전송을 위한 리스너
        this.addActionListener(new EnterRoomListener(chatRoomClientService, info.roomId()));
    }

    public void updateInfo(RoomInfo info) {
        String displayName = String.format("%s (%d명)", info.roomName(), info.userCount());
        this.setText(displayName);
    }

    private void initStyle() {
        this.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.setHorizontalAlignment(SwingConstants.LEFT);
        this.setOpaque(true);
        this.setBorderPainted(false);
        this.setBackground(AppConstant.PRIMARY_COLOR);
        this.setForeground(AppConstant.TEXT_COLOR);
        this.setFocusPainted(false);
        this.setRolloverEnabled(true);

        this.addChangeListener(e -> {
            ButtonModel model = this.getModel();
            if (model.isPressed()) {
                this.setBackground(AppConstant.PRIMARY_COLOR.darker());
            } else if (model.isRollover()) {
                this.setBackground(AppConstant.PRIMARY_COLOR.brighter());
            } else {
                this.setBackground(AppConstant.PRIMARY_COLOR);
            }
        });
    }
}
