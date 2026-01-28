package com.nhnacademy.messenger.common.data.auth.login;

public record LoginResponse(
        String userId,
        String sessionId,
        String message
) {
}
