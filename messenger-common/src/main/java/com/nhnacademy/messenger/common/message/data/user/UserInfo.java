package com.nhnacademy.messenger.common.message.data.user;

public record UserInfo(
        String id,
        String name,
        Boolean online
) {}
