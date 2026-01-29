package com.nhnacademy.messenger.common.message.data.error;
import com.nhnacademy.messenger.common.message.data.MessageData;

public record ErrorResponse(
        ErrorCode code,
        String message
) implements MessageData {
}
