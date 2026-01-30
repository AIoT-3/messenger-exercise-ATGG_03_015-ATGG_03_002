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
            // TODO: 호출자에서 복구/연결종료 등을 처리할 수 있게 메시지에 연결 정보를 담아 던지도록 변경 고려하기
            throw new MessageConvertException("메시지 전송 실패", e);
        }
    }
}
