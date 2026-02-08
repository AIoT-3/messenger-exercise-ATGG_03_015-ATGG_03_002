package com.nhnacademy.messenger.common.util.reader.nio;

import com.nhnacademy.messenger.common.config.AppConstant;
import com.nhnacademy.messenger.common.exception.MessageConvertException;
import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.util.converter.MessageConverter;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class NioMessageReader {

    private final ByteBuffer buffer;
    private int targetLength = -1;
    private static final int MAX_HEADER_LENGTH = 100; // 헤더 무한 읽기 방지

    public NioMessageReader() {
        // OS 커널 버퍼 직접 사용으로 효율 증대
        this.buffer = ByteBuffer.allocateDirect(1024 * 16);
    }

    public Message read(SocketChannel channel) throws IOException {
        int readBytes = channel.read(buffer);
        if (readBytes == -1) {
            throw new EOFException("연결 종료");
        }
        if (readBytes == 0) {
            return null;
        }

        buffer.flip(); // 읽기 모드 전환
        try {
            // 1. 헤더 파싱 (아직 바디 길이를 모를 때)
            if (targetLength == -1) {
                int lineEnd = findLineEnd();
                
                if (lineEnd == -1) {
                    // 개행 없이 계속 들어오면 메모리 보호를 위해 예외 발생
                    if (buffer.remaining() > MAX_HEADER_LENGTH) {
                        throw new MessageConvertException("헤더 길이가 제한을 초과했습니다.");
                    }
                    return null;
                }

                // 헤더 텍스트 추출
                int oldLimit = buffer.limit();
                buffer.limit(lineEnd);
                byte[] headerBytes = new byte[lineEnd - buffer.position()];
                buffer.get(headerBytes);
                buffer.limit(oldLimit);
                
                buffer.position(lineEnd + 1); // \n 문자 소비

                String header = new String(headerBytes, StandardCharsets.UTF_8);
                if (!header.startsWith(AppConstant.MESSAGE_LENGTH)) {
                    throw new MessageConvertException("유효하지 않은 프로토콜 헤더: " + header);
                }

                try {
                    String lengthStr = header.substring(AppConstant.MESSAGE_LENGTH.length()).trim();
                    targetLength = Integer.parseInt(lengthStr);
                } catch (NumberFormatException e) {
                    throw new MessageConvertException("메시지 길이 파싱 실패: " + header);
                }
            }

            // 2. 바디 파싱 (데이터가 충분히 모였을 때)
            if (targetLength != -1 && buffer.remaining() >= targetLength) {
                byte[] body = new byte[targetLength];
                buffer.get(body);
                targetLength = -1; // 다음 패킷 처리를 위해 상태 초기화
                return MessageConverter.fromBytes(body);
            }

            return null;
        } finally {
            buffer.compact(); // 읽지 않은 데이터를 앞으로 당기고 다시 쓰기 모드 준비
        }
    }

    private int findLineEnd() {
        for (int i = buffer.position(); i < buffer.limit(); i++) {
            if (buffer.get(i) == '\n') {
                return i;
            }
        }
        return -1;
    }
}