package com.nhnacademy.messenger.common.util.writer.nio;

import com.nhnacademy.messenger.common.exception.MessageConvertException;
import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.util.converter.MessageConverter;
import com.nhnacademy.messenger.common.util.writer.MessageWriter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NioMessageWriter implements MessageWriter {

    private final SocketChannel channel;

    public NioMessageWriter(SocketChannel channel) {
        this.channel = channel;
    }

    @Override
    public void writeMessage(Message message) {
        try {
            // 1. 메시지를 헤더 포함 바이트 배열로 직렬화
            byte[] bytes = MessageConverter.toBytes(message);
            ByteBuffer buffer = ByteBuffer.wrap(bytes);

            // 2. 채널에 전송 (다 써질 때까지 반복)
            while (buffer.hasRemaining()) {
                channel.write(buffer);
            }
        } catch (IOException | RuntimeException e) {
            // 3. 일관된 예외 처리를 위해 공통 예외로 변환
            throw new MessageConvertException("메시지 전송 중 오류 발생", e);
        }
    }
}