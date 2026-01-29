package com.nhnacademy.messenger.common.util.reader.bio;

import com.nhnacademy.messenger.common.exception.MessageConvertException;
import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.util.converter.MessageConverter;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import static com.nhnacademy.messenger.common.config.AppConstant.MESSAGE_LENGTH;

public class StreamMessageReader {

    private final InputStream inputStream;
    private static final int MAX_HEADER_LENGTH = 100; // 헤더 무한 읽기 방어

    public StreamMessageReader(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public Message read() throws IOException {
        // 1. 헤더 읽기 (1바이트씩 읽어서 \n까지)
        StringBuilder headerBuilder = new StringBuilder();
        int b;
        while ((b = inputStream.read()) != -1) {
            char c = (char) b;
            if (c == '\n') {
                break;
            }
            headerBuilder.append(c);
            
            if (headerBuilder.length() > MAX_HEADER_LENGTH) {
                throw new MessageConvertException("헤더 길이가 제한을 초과했습니다.");
            }
        }

        // 스트림이 헤더를 다 읽지 못했거나, 아무것도 읽지 못한 경우 EOF 처리
        if (b == -1) {
            if (headerBuilder.isEmpty()) {
                throw new EOFException();
            }
            throw new EOFException("헤더를 읽는 도중 연결이 종료되었습니다.");
        }

        // 2. 길이 파싱
        String headerLine = headerBuilder.toString().trim();
        if (!headerLine.startsWith(MESSAGE_LENGTH)) {
            throw new MessageConvertException("유효하지 않은 프로토콜 헤더입니다: " + headerLine);
        }

        int length;
        try {
            String lengthStr = headerLine.substring(MESSAGE_LENGTH.length());
            length = Integer.parseInt(lengthStr);
        } catch (NumberFormatException e) {
            throw new MessageConvertException("메시지 길이를 파싱할 수 없습니다: " + headerLine, e);
        }

        // 3. 바디 읽기
        byte[] bytes = inputStream.readNBytes(length);
        if (bytes.length < length) {
            throw new EOFException("메시지 본문을 읽는 도중 연결이 종료되었습니다.");
        }

        // 4. 변환
        return MessageConverter.fromBytes(bytes);
    }
}
