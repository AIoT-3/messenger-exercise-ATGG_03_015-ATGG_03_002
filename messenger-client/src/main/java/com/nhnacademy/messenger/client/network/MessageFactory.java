package com.nhnacademy.messenger.client.network;

import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.message.data.chat.ChatRequest;
import com.nhnacademy.messenger.common.message.data.chat.PrivateChatRequest;
import com.nhnacademy.messenger.common.message.header.MessageType;
import com.nhnacademy.messenger.common.message.header.RequestHeader;
import com.nhnacademy.messenger.common.util.converter.MessageConverter;
import lombok.experimental.UtilityClass;

// TODO: common으로 옮겨서 request와 response 메시지를 만드는 팩토리로 분리 고려

@UtilityClass
public class MessageFactory {

    public static Message chat(String sessionId, Long roomId, String content) {
        ChatRequest data = new ChatRequest(roomId, content);
        return new Message(
                RequestHeader.of(MessageType.CHAT_MESSAGE, sessionId),
                MessageConverter.toJsonNode(data)
        );
    }

    public static Message privateChat(String sessionId, String senderId, String receiverId, String content) {
        PrivateChatRequest data = new PrivateChatRequest(senderId, receiverId, content);
        return new Message(
                RequestHeader.of(MessageType.PRIVATE_MESSAGE, sessionId),
                MessageConverter.toJsonNode(data)
        );
    }
}
