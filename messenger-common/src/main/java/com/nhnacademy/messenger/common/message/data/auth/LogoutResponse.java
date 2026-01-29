package com.nhnacademy.messenger.common.message.data.auth;
import com.nhnacademy.messenger.common.message.data.MessageData;

public record LogoutResponse (
        String message
) implements MessageData {
}
