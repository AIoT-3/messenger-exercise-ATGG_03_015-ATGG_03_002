package com.nhnacademy.messenger.client.domain.chat.ui.console;

import com.nhnacademy.messenger.client.domain.chat.event.ChatHistoryResponseEvent;
import com.nhnacademy.messenger.client.domain.chat.event.ReceiveFileMessageEvent;
import com.nhnacademy.messenger.client.domain.chat.event.ReceiveMessageEvent;
import com.nhnacademy.messenger.client.domain.chat.event.ReceivePrivateMessageEvent;
import com.nhnacademy.messenger.client.ui.cli.ConsoleView;
import com.nhnacademy.messenger.common.event.EventListener;
import com.nhnacademy.messenger.common.message.data.chat.MessageInfo;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ChatConsoleListener {

    private final ConsoleView view;

    @EventListener
    public void onMessageReceived(ReceiveMessageEvent event) {
        view.println(String.format("[%d번 방] [msg: %d] [%s]: %s", 
                event.roomId(), event.messageId(), event.senderId(), event.content()));
    }

    @EventListener
    public void onPrivateMessageReceived(ReceivePrivateMessageEvent event) {
        view.println("[귓속말] " + event.senderId() + ": " + event.content());
    }
    
    @EventListener
    public void onFileMessageReceived(ReceiveFileMessageEvent event) {
        view.println(String.format("[%d번 방] [파일] %s님이 파일을 전송했습니다: %s (%.2f KB)", 
                event.roomId(), event.senderId(), event.fileName(), event.fileSize() / 1024.0));
    }

    @EventListener
    public void onChatHistoryReceived(ChatHistoryResponseEvent event) {
        List<MessageInfo> messages = event.messages();
        StringBuilder sb = new StringBuilder();
        sb.append("\n----------- 과거 채팅 기록 -----------").append("\n");
        for (MessageInfo msg : messages) {
            sb.append(String.format("[msg: %d] [%s]: %s", msg.messageId(), msg.senderName(), msg.content())).append("\n");
        }
        if (event.hasMore()) {
            sb.append("(이전 기록이 더 존재합니다. /history <roomId> <limit> <beforeMessageId> 명령어로 더 볼 수 있습니다.)").append("\n");
        }
        sb.append("-----------------------------------");
        view.println(sb.toString());
    }
}