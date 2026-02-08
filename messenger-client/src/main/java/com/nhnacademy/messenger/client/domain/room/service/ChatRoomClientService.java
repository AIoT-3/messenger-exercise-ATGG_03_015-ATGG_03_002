package com.nhnacademy.messenger.client.domain.room.service;

import com.nhnacademy.messenger.client.network.MessageClient;
import com.nhnacademy.messenger.client.network.MessageFactory;
import com.nhnacademy.messenger.client.session.ClientSession;
import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.message.data.chat.ChatHistoryRequest;
import com.nhnacademy.messenger.common.message.data.room.CreateRoomRequest;
import com.nhnacademy.messenger.common.message.data.room.EnterRoomRequest;
import com.nhnacademy.messenger.common.message.data.room.ExitRoomRequest;
import com.nhnacademy.messenger.common.message.header.MessageType;
import com.nhnacademy.messenger.common.message.header.RequestHeader;
import com.nhnacademy.messenger.common.util.converter.MessageConverter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import com.nhnacademy.messenger.common.message.data.file.FileTransferRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Objects;

@RequiredArgsConstructor
public class ChatRoomClientService {

    private final MessageClient messageClient;

    public void createRoom(String roomName) {
        String sessionId = ClientSession.INSTANCE.getSessionId();

        CreateRoomRequest data = new CreateRoomRequest(roomName);
        RequestHeader header = RequestHeader.of(MessageType.CHAT_ROOM_CREATE, sessionId);
        Message message = new Message(header, MessageConverter.toJsonNode(data));

        messageClient.send(message);
    }

    public void getRoomList() {
        String sessionId = ClientSession.INSTANCE.getSessionId();

        // 요청 데이터는 없음
        RequestHeader header = RequestHeader.of(MessageType.CHAT_ROOM_LIST, sessionId);
        Message message = new Message(header, null);

        messageClient.send(message);
    }

    public void enterRoom(long roomId) {
        String sessionId = ClientSession.INSTANCE.getSessionId();

        EnterRoomRequest data = new EnterRoomRequest(roomId);
        RequestHeader header = RequestHeader.of(MessageType.CHAT_ROOM_ENTER, sessionId);
        Message message = new Message(header, MessageConverter.toJsonNode(data));

        messageClient.send(message);
    }

    public void exitRoom(long roomId) {
        String sessionId = ClientSession.INSTANCE.getSessionId();

        ExitRoomRequest data = new ExitRoomRequest(roomId);
        RequestHeader header = RequestHeader.of(MessageType.CHAT_ROOM_EXIT, sessionId);
        Message message = new Message(header, MessageConverter.toJsonNode(data));

        messageClient.send(message);
    }

    public void sendMessage(Long roomId, String content) {
        if (Objects.isNull(roomId) || StringUtils.isBlank(content)) {
            return;
        }

        String sessionId = ClientSession.INSTANCE.getSessionId();
        messageClient.send(MessageFactory.chat(sessionId, roomId, content));
    }

    public void sendPrivateMessage(String receiverId, String content) {
        if (StringUtils.isBlank(receiverId) || StringUtils.isBlank(content)) {
            return;
        }

        String sessionId = ClientSession.INSTANCE.getSessionId();
        String senderId = ClientSession.INSTANCE.getUserId();
        messageClient.send(MessageFactory.privateChat(sessionId, senderId, receiverId, content));
    }

    public void getChatHistory(Long roomId, Integer limit, Long beforeMessageId) {
        String sessionId = ClientSession.INSTANCE.getSessionId();

        ChatHistoryRequest data = new ChatHistoryRequest(roomId, limit, beforeMessageId);
        RequestHeader header = RequestHeader.of(MessageType.CHAT_MESSAGE_HISTORY, sessionId);
        Message message = new Message(header, MessageConverter.toJsonNode(data));

        messageClient.send(message);
    }

    public void sendFile(Long roomId, String filePath) {
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            throw new IllegalArgumentException("파일을 찾을 수 없습니다: " + filePath);
        }

        long fileSize = file.length();
        if (fileSize > 10 * 1024 * 1024) { // 10MB
            throw new IllegalArgumentException("파일 크기는 10MB를 초과할 수 없습니다.");
        }

        try {
            byte[] fileContent = Files.readAllBytes(file.toPath());
            String encodedString = Base64.getEncoder().encodeToString(fileContent);

            String sessionId = ClientSession.INSTANCE.getSessionId();
            FileTransferRequest data = new FileTransferRequest(roomId, file.getName(), fileSize, encodedString);
            
            RequestHeader header = RequestHeader.of(MessageType.FILE_TRANSFER, sessionId);
            Message message = new Message(header, MessageConverter.toJsonNode(data));

            messageClient.send(message);

        } catch (IOException e) {
            throw new RuntimeException("파일 읽기 실패: " + e.getMessage(), e);
        }
    }
}
