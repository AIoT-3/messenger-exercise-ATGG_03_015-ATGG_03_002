package com.nhnacademy.messenger.common.message.data.auth;

public record LoginResponse(
        String userId,
        String sessionId,
        String message
) {
}
