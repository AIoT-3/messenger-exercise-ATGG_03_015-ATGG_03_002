package com.nhnacademy.messenger.common.exception;

public class PacketSerializerException extends RuntimeException {

    public PacketSerializerException(String message, Throwable cause) {
        super(message, cause);
    }

    public PacketSerializerException(String message) {
        super(message);
    }
}
