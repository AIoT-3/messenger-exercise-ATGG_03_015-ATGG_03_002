package com.nhnacademy.messenger.common.payload.common;

public record ErrorResponse(
        ErrorCode code,
        String message
) {
}
