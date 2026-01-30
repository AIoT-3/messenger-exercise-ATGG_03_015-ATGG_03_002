package com.nhnacademy.messenger.common.exception;

/**
 * MessageConvertException
 * 역할: 파싱/포맷 예외
 */
public class MessageConvertException extends RuntimeException {

    public MessageConvertException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageConvertException(String message) {
        super(message);
    }
}
