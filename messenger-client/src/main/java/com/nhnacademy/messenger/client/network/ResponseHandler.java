package com.nhnacademy.messenger.client.network;

import com.nhnacademy.messenger.common.message.Message;

// responsehandler를 철저하게 stateless를 유지해야 함.
public interface ResponseHandler {
    void handle(Message message);
}
