package com.nhnacademy.messenger.common.message.data.auth;

import com.nhnacademy.messenger.common.message.data.MessageData;
import org.apache.commons.lang3.StringUtils;

public record LoginRequest(
        String userId,
        String password
) implements MessageData {

    public LoginRequest {
        if (StringUtils.isBlank(userId)) {
            throw new IllegalArgumentException("userId must not be null or empty");
        }
        if (StringUtils.isBlank(password)) {
            throw new IllegalArgumentException("password must not be null or empty");
        }
    }
}