package com.nhnacademy.messenger.client.ui.gui.manager;

import com.nhnacademy.messenger.client.domain.room.service.ChatRoomClientService;
import com.nhnacademy.messenger.client.ui.gui.panel.RoomChatPanel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
public class RoomChatManager {
    private final Map<Long, RoomChatPanel> chatRooms = new ConcurrentHashMap<>();
    private final ChatRoomClientService chatRoomClientService;

    public void openRoom(long roomId, String roomName) {
        if (chatRooms.containsKey(roomId)) {
            RoomChatPanel panel = chatRooms.get(roomId);
            if (!panel.isVisible()) {
                panel.setVisible(true);
            }
            panel.toFront();
        } else {
            RoomChatPanel panel = new RoomChatPanel(roomId, chatRoomClientService);
            panel.setRoomTitle(roomName);
            panel.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            // Remove from map when closed

            panel.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    chatRooms.remove(roomId);
                    log.info("Chat room {} closed and removed from manager.", roomId);
                }
            });

            chatRooms.put(roomId, panel);
            panel.setVisible(true);
        }
    }
    
    public void closeRoom(long roomId) {
        RoomChatPanel panel = chatRooms.remove(roomId);
        if (panel != null) {
            panel.dispose();
        }
    }

    public void appendMessage(long roomId, String sender, String content) {
        RoomChatPanel panel = chatRooms.get(roomId);
        if (panel != null) {
            panel.addMessage(sender, content);
        } else {
            log.debug("Received message for closed or non-existent room: {}", roomId);
        }
    }
    
    public boolean isRoomOpen(long roomId) {
        return chatRooms.containsKey(roomId);
    }
    
    public void closeAll() {
        for (RoomChatPanel panel : chatRooms.values()) {
            panel.dispose();
        }
        chatRooms.clear();
    }
}
