package com.nhnacademy.messenger.common.util.writer.bio;

import com.nhnacademy.messenger.common.exception.MessageConvertException;
import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.util.converter.MessageConverter;
import com.nhnacademy.messenger.common.util.writer.MessageWriter;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class StreamMessageWriter implements MessageWriter {

    private final DataOutputStream out;

    public StreamMessageWriter(OutputStream outputStream) {
        this.out = new DataOutputStream(outputStream);
    }

    @Override
    public synchronized void writeMessage(Message message) {
        try {
            byte[] bytes = MessageConverter.toBytes(message);
            out.write(bytes);
            out.flush();
        } catch (IOException e) {
            throw new MessageConvertException("메시지 전송 실패", e);
        }
    }
}
