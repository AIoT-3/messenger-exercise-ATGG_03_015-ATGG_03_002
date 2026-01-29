package com.nhnacademy.messenger.common.message.data.auth;

public record LoginRequest(
        String userId,
        String password
){

}
