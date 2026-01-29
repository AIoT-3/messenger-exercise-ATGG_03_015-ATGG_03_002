package com.nhnacademy.messenger.common.message.data.auth;
import com.nhnacademy.messenger.common.message.data.MessageData;

public record LoginResponse(
        String userId,
        String sessionId,
        String message
) implements MessageData {
}
