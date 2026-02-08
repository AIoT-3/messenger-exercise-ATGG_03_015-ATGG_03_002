package com.nhnacademy.messenger.server.chat.handler;

import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.message.data.file.FileTransferRequest;
import com.nhnacademy.messenger.common.message.data.file.FileTransferResponse;
import com.nhnacademy.messenger.common.message.data.push.PushNewMessage;
import com.nhnacademy.messenger.common.message.header.ResponseHeader;
import com.nhnacademy.messenger.common.util.converter.MessageConverter;
import com.nhnacademy.messenger.server.chat.domain.Chat;
import com.nhnacademy.messenger.server.chat.service.ChatService;
import com.nhnacademy.messenger.server.network.RequestHandler;
import com.nhnacademy.messenger.server.room.domain.ChatRoom;
import com.nhnacademy.messenger.server.room.service.ChatRoomService;
import com.nhnacademy.messenger.server.session.domain.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.nhnacademy.messenger.common.message.header.MessageType.FILE_TRANSFER_SUCCESS;
import static com.nhnacademy.messenger.common.message.header.MessageType.PUSH_NEW_MESSAGE;

@Slf4j
@RequiredArgsConstructor
public class FileTransferRequestHandler implements RequestHandler {

    private final ChatService chatService;
    private final ChatRoomService chatRoomService;

    @Override
    public void handle(Session session, Message message) {
        // 1. 메시지 파싱
        FileTransferRequest request = (FileTransferRequest) MessageConverter.toData(message);
        Long roomId = request.roomId();
        String senderId = session.getUser().getUserId();
        
        log.debug("파일 전송 요청: roomId={}, fileName={}, size={}", roomId, request.fileName(), request.fileSize());

        // 2. 메시지 저장 (Base64 데이터를 content에 저장)
        Chat chat = chatService.saveFileMessage(roomId, senderId, request.fileName(), request.fileSize(), request.fileData());

        // 3. 브로드캐스트
        ChatRoom room = chatRoomService.getChatRoomById(roomId);
        
        PushNewMessage pushData = new PushNewMessage(
                chat.getRoomId(),
                chat.getMessageId(),
                chat.getSenderId(),
                chat.getContent(), // Base64 data
                chat.getType(),    // PushMessageType.FILE
                chat.getFileName(),
                chat.getFileSize()
        );

        Message pushMessage = new Message(
                ResponseHeader.success(PUSH_NEW_MESSAGE),
                MessageConverter.toJsonNode(pushData)
        );

        room.broadcast(pushMessage);

        // 4. 응답 전송
        FileTransferResponse responseData = new FileTransferResponse(
                roomId,
                chat.getMessageId(),
                chat.getFileName()
        );

        session.sendMessage(new Message(
                ResponseHeader.success(FILE_TRANSFER_SUCCESS),
                MessageConverter.toJsonNode(responseData)
        ));
        
        log.info("파일 전송 완료: roomId={}, messageId={}", roomId, chat.getMessageId());
    }
}
