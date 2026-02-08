package com.nhnacademy.messenger.client.domain.chat.ui.gui;

import com.nhnacademy.messenger.client.domain.chat.event.ChatHistoryResponseEvent;
import com.nhnacademy.messenger.client.domain.chat.event.ReceiveFileMessageEvent;
import com.nhnacademy.messenger.client.domain.chat.event.ReceiveMessageEvent;
import com.nhnacademy.messenger.client.domain.chat.event.ReceivePrivateMessageEvent;
import com.nhnacademy.messenger.client.ui.gui.GuiView;
import com.nhnacademy.messenger.common.event.EventListener;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChatGuiListener {

    private final GuiView view;

    @EventListener
    public void onMessageReceived(ReceiveMessageEvent event) {
        view.appendMessage(event.roomId(), event.senderId(), event.content());
    }

    @EventListener
    public void onPrivateMessageReceived(ReceivePrivateMessageEvent event) {
        boolean isChatVisible = view.appendPrivateMessage(event.senderId(), event.content());
        if (!isChatVisible) {
            view.setUserNotification(event.senderId(), true);
        }
    }
    
    @EventListener
    public void onFileMessageReceived(ReceiveFileMessageEvent event) {
        String message = String.format("[파일] %s (%.2f KB)", event.fileName(), event.fileSize() / 1024.0);
        view.appendMessage(event.roomId(), event.senderId(), message);
    }

    @EventListener
    public void onChatHistoryReceived(ChatHistoryResponseEvent event) {
        view.appendHistory(event.roomId(), event.messages());
    }
}
