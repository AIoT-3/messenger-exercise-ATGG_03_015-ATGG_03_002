package com.nhnacademy.messenger.common.message.data.chat;

import com.nhnacademy.messenger.common.message.data.MessageData;

import java.util.List;

public record ChatHistoryResponse(
        Long roomId,
        List<MessageInfo> messages,
        Boolean hasMore
) implements MessageData {
}
