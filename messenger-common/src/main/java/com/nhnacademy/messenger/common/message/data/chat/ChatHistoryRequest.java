package com.nhnacademy.messenger.common.message.data.chat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nhnacademy.messenger.common.message.data.MessageData;

public record ChatHistoryRequest(
        Long roomId,
        Integer limit,
        Long beforeMessageId
) implements MessageData {
    public static final int DEFAULT_LIMIT = 50;
    public static final int MAX_LIMIT = 100;

    @JsonIgnore
    public int getLimitOrDefault() {
        return (limit == null) ? DEFAULT_LIMIT : limit;
    }
}
