package com.nhnacademy.messenger.server.chat.handler;

import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.message.MessageBuilder;
import com.nhnacademy.messenger.common.message.data.chat.PrivateChatRequest;
import com.nhnacademy.messenger.common.message.data.chat.PrivateChatResponse;
import com.nhnacademy.messenger.common.message.data.error.ErrorCode;
import com.nhnacademy.messenger.common.message.data.push.PushMessageType;
import com.nhnacademy.messenger.common.message.data.push.PushNewMessage;
import com.nhnacademy.messenger.common.message.header.MessageType;
import com.nhnacademy.messenger.common.util.converter.MessageConverter;
import com.nhnacademy.messenger.server.network.RequestHandler;
import com.nhnacademy.messenger.server.session.domain.Session;
import com.nhnacademy.messenger.server.session.manager.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@RequiredArgsConstructor
public class PrivateChatRequestHandler implements RequestHandler {

    private final SessionManager sessionManager;
    private final AtomicLong messageIdCounter = new AtomicLong(System.currentTimeMillis());

    @Override
    public void handle(Session session, Message message) {
        PrivateChatRequest request = (PrivateChatRequest) MessageConverter.toData(message);
        
        String senderId = session.getUser().getUserId();
        String receiverId = request.receiverId();
        String content = request.message();
        
        // 메시지 ID 생성 (저장은 안하지만 ID는 발급)
        long messageId = messageIdCounter.incrementAndGet();

        log.debug("귓속말 요청: sender={} receiver={}", senderId, receiverId);

        // 1. 수신자 세션 조회
        Optional<Session> receiverSessionOpt = sessionManager.getSessionByUserId(receiverId);

        if (receiverSessionOpt.isPresent()) {
            Session receiverSession = receiverSessionOpt.get();

            // PRIVATE_MESSAGE는 push 전송 할 때, roomId = -1로 메세지를 전송하도록 구현
            PushNewMessage pushData = new PushNewMessage(
                    -1L,
                    messageId,
                    senderId,
                    content,
                    PushMessageType.TEXT,
                    null,
                    0L
            );

            Message pushMessage = MessageBuilder.with(MessageType.PUSH_NEW_MESSAGE)
                    .success(true)
                    .data(pushData)
                    .build();
            
            receiverSession.sendMessage(pushMessage);
        } else {
            session.sendError(ErrorCode.USER_NOT_FOUND, "수신자를 찾을 수 없습니다.");
            return; // 전송 중단
        }

        // 3. 발신자에게 성공 응답 전송

        PrivateChatResponse response = new PrivateChatResponse(
                senderId,
                receiverId,
                "귓속말이 전송되었습니다.",
                messageId
        );

        session.sendMessage(MessageBuilder.with(MessageType.PRIVATE_MESSAGE_SUCCESS)
                .success(true)
                .data(response)
                .build());
    }
}
