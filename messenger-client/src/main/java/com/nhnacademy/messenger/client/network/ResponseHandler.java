package com.nhnacademy.messenger.client.network;

import com.nhnacademy.messenger.common.message.Message;

public interface ResponseHandler {
    void handle(Message message);
}
