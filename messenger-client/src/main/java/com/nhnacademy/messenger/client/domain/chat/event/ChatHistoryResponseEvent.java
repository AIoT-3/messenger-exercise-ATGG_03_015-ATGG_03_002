package com.nhnacademy.messenger.client.domain.chat.event;

import com.nhnacademy.messenger.common.message.data.chat.MessageInfo;
import java.util.List;

public record ChatHistoryResponseEvent(
        Long roomId,
        List<MessageInfo> messages,
        boolean hasMore
) {}
