package com.nhnacademy.messenger.common.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.messenger.common.exception.PacketSerializerException;
import com.nhnacademy.messenger.common.message.data.MessageData;
import com.nhnacademy.messenger.common.message.data.auth.LoginRequest;
import com.nhnacademy.messenger.common.message.header.MessageHeader;
import com.nhnacademy.messenger.common.message.header.MessageType;
import com.nhnacademy.messenger.common.message.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MessageConverterTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private Message originalMessage;

    @BeforeEach
    void setUp() {
        // 테스트용 LoginRequest용 Message 생성
        MessageHeader header = new MessageHeader(
                MessageType.LOGIN,
                true,
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS),
                UUID.randomUUID().toString()
        );
        JsonNode data = objectMapper.valueToTree(new LoginRequest("marco", "nhnacademy123"));

        originalMessage = new Message(header, data);
    }

    @Test
    @DisplayName("직렬화 정상 동작 테스트: Length Line과 Payload가 포함되어야 함")
    void serialize_Success() {
        String serialized = MessageConverter.serializeMessage(originalMessage);

        assertAll(
                () -> assertTrue(serialized.startsWith("message-length: ")),
                () -> assertTrue(serialized.contains("\n")),
                () -> assertTrue(serialized.contains("LOGIN")),
                () -> assertTrue(serialized.contains("marco")),
                () -> assertTrue(serialized.contains("nhnacademy123"))
        );
    }

    @Test
    @DisplayName("왕복 테스트: 직렬화된 문자열에서 바디만 추출하여 역직렬화하면 원본과 같아야 함")
    void roundTrip_Success() {
        String serialized = MessageConverter.serializeMessage(originalMessage);
        
        String jsonBody = serialized.substring(serialized.indexOf('\n') + 1);
        Message deserialized = MessageConverter.deserializeMessage(jsonBody);

        assertAll(
                () -> assertEquals(originalMessage, deserialized),
                () -> assertEquals(serialized, MessageConverter.serializeMessage(deserialized))
        );
    }

    @Test
    @DisplayName("데이터 추출 테스트: Packet의 JsonNode를 실제 객체로 자동 변환 성공")
    void extractData_Success() {
        MessageData extracted = MessageConverter.extractData(originalMessage);

        assertAll(
                () -> assertNotNull(extracted),
                () -> assertInstanceOf(LoginRequest.class, extracted),
                () -> assertEquals("marco", ((LoginRequest) extracted).userId()),
                () -> assertEquals("nhnacademy123", ((LoginRequest) extracted).password())
        );
    }

    @Test
    @DisplayName("역직렬화 실패: 올바르지 않은 JSON 형식인 경우 예외 발생")
    void deserialize_Failure_InvalidJson() {
        String brokenJson = "{\"header\":{\"type\"";

        assertThrows(PacketSerializerException.class, () ->
                MessageConverter.deserializeMessage(brokenJson)
        );
    }
}