package com.nhnacademy.messenger.common.exception;

public class MessageConvertException extends RuntimeException {

    public MessageConvertException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageConvertException(String message) {
        super(message);
    }
}
