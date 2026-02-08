package com.nhnacademy.messenger.common.message;

import com.nhnacademy.messenger.common.message.header.Header;
import com.nhnacademy.messenger.common.message.header.MessageType;
import com.nhnacademy.messenger.common.message.header.RequestHeader;
import com.nhnacademy.messenger.common.message.header.ResponseHeader;
import com.nhnacademy.messenger.common.util.converter.MessageConverter;
import com.nhnacademy.messenger.common.message.data.MessageData;
import com.fasterxml.jackson.databind.JsonNode;

public class MessageBuilder {
    private MessageType type;
    private String sessionId;
    private Boolean success;
    private MessageData data;

    private MessageBuilder(MessageType type) {
        this.type = type;
    }

    public static MessageBuilder with(MessageType type) {
        return new MessageBuilder(type);
    }

    public MessageBuilder sessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public MessageBuilder success(boolean success) {
        this.success = success;
        return this;
    }

    public MessageBuilder data(MessageData data) {
        this.data = data;
        return this;
    }

    public Message build() {
        Header header;
        // success 여부가 세팅되어 있으면 ResponseHeader, 아니면 RequestHeader
        if (success != null) {
            header = success ? ResponseHeader.success(type) : ResponseHeader.fail(type);
        } else {
            header = RequestHeader.of(type, sessionId);
        }

        JsonNode body = (data == null) ? null : MessageConverter.toJsonNode(data);
        return new Message(header, body);
    }
}