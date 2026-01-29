package com.nhnacademy.messenger.common.util.reader.bio;

import com.nhnacademy.messenger.common.exception.MessageConvertException;
import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.message.header.RequestHeader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class StreamMessageReaderTest {

    @Test
    @DisplayName("정상 읽기: 올바른 헤더와 바디가 주어졌을 때 Message 객체 반환")
    void read_Success() throws IOException {
        // Given
        String jsonBody = "{\"header\":{\"type\":\"LOGIN\",\"timestamp\":\"2024-01-01T12:00:00\"},\"data\":{\"userId\":\"test\",\"password\":\"1234\"}}";
        byte[] bodyBytes = jsonBody.getBytes(StandardCharsets.UTF_8);
        String packetString = "message-length: " + bodyBytes.length + "\n" + jsonBody;

        ByteArrayInputStream inputStream = new ByteArrayInputStream(packetString.getBytes(StandardCharsets.UTF_8));
        StreamMessageReader reader = new StreamMessageReader(inputStream);

        // When
        Message message = reader.read();

        // Then
        assertNotNull(message);
        assertInstanceOf(RequestHeader.class, message.header());
    }

    @Test
    @DisplayName("예외: 헤더 길이가 제한(100자)을 초과하면 MessageConvertException 발생")
    void read_HeaderTooLong() {
        // Given: \n 없이 101글자의 더미 헤더
        String longHeader = "a".repeat(101);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(longHeader.getBytes(StandardCharsets.UTF_8));
        StreamMessageReader reader = new StreamMessageReader(inputStream);

        // When & Then
        assertThrows(MessageConvertException.class, reader::read, "헤더 길이가 제한을 초과했습니다.");
    }

    @Test
    @DisplayName("예외: 헤더 형식이 'message-length: '로 시작하지 않으면 MessageConvertException 발생")
    void read_InvalidHeaderPrefix() {
        // Given
        String invalidPacket = "invalid-header: 10\n{}";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(invalidPacket.getBytes(StandardCharsets.UTF_8));
        StreamMessageReader reader = new StreamMessageReader(inputStream);

        // When & Then
        assertThrows(MessageConvertException.class, reader::read, "유효하지 않은 프로토콜 헤더입니다");
    }

    @Test
    @DisplayName("예외: 헤더의 길이가 숫자가 아니면 MessageConvertException 발생")
    void read_InvalidHeaderLength() {
        // Given
        String invalidPacket = "message-length: abc\n{}";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(invalidPacket.getBytes(StandardCharsets.UTF_8));
        StreamMessageReader reader = new StreamMessageReader(inputStream);

        // When & Then
        assertThrows(MessageConvertException.class, reader::read, "메시지 길이를 파싱할 수 없습니다");
    }

    @Test
    @DisplayName("예외: 읽을 데이터가 없는 상태(EOF)에서 read 호출 시 EOFException 발생")
    void read_EOF_BeforeHeader() {
        // Given: 빈 스트림
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        StreamMessageReader reader = new StreamMessageReader(inputStream);

        // When & Then
        assertThrows(EOFException.class, reader::read);
    }

    @Test
    @DisplayName("예외: 헤더를 읽는 도중에 스트림이 끊기면 EOFException 발생")
    void read_EOF_DuringHeader() {
        // Given: \n 없이 끊김
        String incompleteHeader = "message-length: 10";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(incompleteHeader.getBytes(StandardCharsets.UTF_8));
        StreamMessageReader reader = new StreamMessageReader(inputStream);

        // When & Then
        assertThrows(EOFException.class, reader::read, "헤더를 읽는 도중 연결이 종료되었습니다.");
    }

    @Test
    @DisplayName("예외: 바디를 읽는 도중(길이 부족)에 스트림이 끊기면 EOFException 발생")
    void read_EOF_DuringBody() {
        // Given: 길이는 10인데 실제 데이터는 5바이트뿐
        String packet = "message-length: 10\n12345";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(packet.getBytes(StandardCharsets.UTF_8));
        StreamMessageReader reader = new StreamMessageReader(inputStream);

        // When & Then
        assertThrows(EOFException.class, reader::read, "메시지 본문을 읽는 도중 연결이 종료되었습니다.");
    }

    @Test
    @DisplayName("예외: 헤더와 길이는 맞지만 JSON 형식이 잘못되었을 때 MessageConvertException 발생")
    void read_InvalidJson() {
        // Given
        String brokenJson = "{\"header\": ... broken ... }";
        byte[] bodyBytes = brokenJson.getBytes(StandardCharsets.UTF_8);
        String packet = "message-length: " + bodyBytes.length + "\n" + brokenJson;

        ByteArrayInputStream inputStream = new ByteArrayInputStream(packet.getBytes(StandardCharsets.UTF_8));
        StreamMessageReader reader = new StreamMessageReader(inputStream);

        // When & Then
        assertThrows(MessageConvertException.class, reader::read, "역직렬화 실패");
    }
}