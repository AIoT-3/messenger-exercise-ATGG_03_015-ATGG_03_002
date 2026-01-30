package com.nhnacademy.messenger.common.util.writer;

import com.nhnacademy.messenger.common.message.Message;

public interface MessageWriter {
    void writeMessage(Message message);
}
