package com.nhnacademy.messenger.client.ui.gui.manager;

import com.nhnacademy.messenger.client.domain.room.service.ChatRoomClientService;
import com.nhnacademy.messenger.client.ui.gui.panel.PrivateChatPanel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
public class PrivateChatManager {
    private final Map<String, PrivateChatPanel> chatPanels = new ConcurrentHashMap<>();
    private final ChatRoomClientService chatRoomClientService;

    public void openChat(String targetUserId) {
        PrivateChatPanel panel = chatPanels.computeIfAbsent(targetUserId, 
            id -> new PrivateChatPanel(id, chatRoomClientService));
        
        if (!panel.isVisible()) {
            panel.setVisible(true);
        }
        panel.toFront();
    }

    public boolean receiveMessage(String senderId, String content) {
        PrivateChatPanel panel = chatPanels.computeIfAbsent(senderId, 
            id -> new PrivateChatPanel(id, chatRoomClientService));
        
        panel.addMessage(senderId, content);
        
        return panel.isVisible();
    }

    public boolean isChatOpen(String userId) {
        PrivateChatPanel panel = chatPanels.get(userId);
        return panel != null && panel.isVisible();
    }

    public void closeAll() {
        chatPanels.values().forEach(PrivateChatPanel::dispose);
        chatPanels.clear();
    }
}
