package com.nhnacademy.messenger.common.message.data.common;

public record ErrorResponse(
        ErrorCode code,
        String message
) {
}
