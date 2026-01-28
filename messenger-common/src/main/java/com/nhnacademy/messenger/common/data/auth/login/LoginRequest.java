package com.nhnacademy.messenger.common.data.auth.login;

public record LoginRequest(
        String userId,
        String password
) {

}
