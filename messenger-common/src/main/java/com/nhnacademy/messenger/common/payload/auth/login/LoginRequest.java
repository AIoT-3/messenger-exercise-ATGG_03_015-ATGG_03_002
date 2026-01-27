package com.nhnacademy.messenger.common.payload.auth.login;

public record LoginRequest(
        String userId,
        String password
) {
}
