package com.nhnacademy.messenger.common.exception;

import com.nhnacademy.messenger.common.message.data.error.ErrorCode;
import lombok.Getter;

/**
 * MessengerException
 * 역할: 도메인/정책 예외
 */
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
