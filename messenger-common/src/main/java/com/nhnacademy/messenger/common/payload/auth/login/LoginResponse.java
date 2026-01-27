package com.nhnacademy.messenger.common.payload.auth.login;

public record LoginResponse(
        String userId,
        String sessionId,
        String message
) {
}
