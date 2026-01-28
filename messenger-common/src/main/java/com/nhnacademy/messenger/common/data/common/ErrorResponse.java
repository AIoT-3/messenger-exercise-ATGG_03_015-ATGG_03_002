package com.nhnacademy.messenger.common.data.common;

public record ErrorResponse(
        ErrorCode code,
        String message
) {
}
