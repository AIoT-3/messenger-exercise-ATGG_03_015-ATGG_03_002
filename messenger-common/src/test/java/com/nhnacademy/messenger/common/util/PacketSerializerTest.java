package com.nhnacademy.messenger.common.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.messenger.common.exception.PacketSerializerException;
import com.nhnacademy.messenger.common.protocol.MessageHeader;
import com.nhnacademy.messenger.common.protocol.MessageType;
import com.nhnacademy.messenger.common.protocol.Packet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PacketSerializerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private Packet originalPacket;

    @BeforeEach
    void setUp() {
        String sessionId = UUID.randomUUID().toString();

        MessageHeader header = new MessageHeader(
                MessageType.LOGIN,
                true,
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS),
                sessionId
        );

        JsonNode data = objectMapper.createObjectNode()
                .put("userId", "marco")
                .put("password", "nhnacademy123");

        originalPacket = new Packet(header, data);
    }

    @Test
    @DisplayName("직렬화 정상 동작 테스트: 헤더 접두사와 JSON 본문이 포함되어야 함")
    void serialize_Success() {
        String serialized = PacketSerializer.serializePacket(originalPacket);

        assertAll(
                () -> assertTrue(serialized.startsWith("message-length: ")),
                () -> assertTrue(serialized.contains("\n")),
                () -> assertTrue(serialized.contains("LOGIN")),
                () -> assertTrue(serialized.contains("marco")),
                () -> assertTrue(serialized.contains("nhnacademy123"))
        );
    }

    @Test
    @DisplayName("왕복 테스트: 원본 패킷과 역직렬화된 패킷이 동일해야 함")
    void roundTrip_Success() {
        String serialized = PacketSerializer.serializePacket(originalPacket);
        Packet deserialized = PacketSerializer.deserializePacket(serialized);

        assertAll(
                () -> assertEquals(originalPacket, deserialized),
                () -> assertEquals(serialized, PacketSerializer.serializePacket(deserialized))
        );
    }

    @Test
    @DisplayName("역직렬화 실패: 헤더의 길이 정보와 실제 데이터 길이가 다르면 예외 발생")
    void deserialize_Failure_LengthMismatch() {
        String malformedData = "message-length: 5\n{\"header\":{},\"data\":{}}";

        assertThrows(PacketSerializerException.class, () ->
                PacketSerializer.deserializePacket(malformedData)
        );
    }

    @Test
    @DisplayName("역직렬화 실패: 올바르지 않은 JSON 형식인 경우 예외 발생")
    void deserialize_Failure_InvalidJson() {
        String brokenJson = "message-length: 15\n{\"header\":{\"type\"";

        assertThrows(PacketSerializerException.class, () ->
                PacketSerializer.deserializePacket(brokenJson)
        );
    }

    @Test
    @DisplayName("역직렬화 실패: 헤더 포맷(숫자 변환)이 잘못된 경우 예외 발생")
    void deserialize_Failure_InvalidHeaderFormat() {
        String invalidHeader = "message-length: ABC\n{}";

        assertThrows(PacketSerializerException.class, () ->
                PacketSerializer.deserializePacket(invalidHeader)
        );
    }
}
