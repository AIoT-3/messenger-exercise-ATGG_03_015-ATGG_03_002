package com.nhnacademy.messenger.client.network;

import com.nhnacademy.messenger.common.message.header.MessageType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MessageHandler {
    MessageType value();
}
