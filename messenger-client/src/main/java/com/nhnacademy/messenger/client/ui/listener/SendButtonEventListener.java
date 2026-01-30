package com.nhnacademy.messenger.client.ui.listener;

import com.nhnacademy.messenger.common.message.data.chat.ChatMessageRequest;
import lombok.AllArgsConstructor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@AllArgsConstructor
public class SendButtonEventListener implements ActionListener {
    // TODO: roomId를 가져오는 메서드 추가
    private long roomId;
    private JTextField chatInputField;

    @Override
    public void actionPerformed(ActionEvent e) {
        String text = chatInputField.getText();
        // TODO: 임시 roodId, 추가 수정 필요함!
        ChatMessageRequest chatMessageRequest = new ChatMessageRequest(roomId, text);
        // TODO: ChatMessageRequest 전송
    }
}
