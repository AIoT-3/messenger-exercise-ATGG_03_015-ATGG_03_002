package com.nhnacademy.messenger.common.exception;

import com.nhnacademy.messenger.common.data.common.ErrorCode;
import lombok.Getter;

public class MessengerException extends RuntimeException {

    @Getter
    private final ErrorCode errorCode;

    public MessengerException(ErrorCode code) {
        super(code.name());
        this.errorCode = code;
    }

    public MessengerException(ErrorCode code, String message) {
        super(message);
        this.errorCode = code;
    }
}
