package com.nhnacademy.messenger.common.util.converter;

import com.nhnacademy.messenger.common.exception.MessageConvertException;
import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.message.MessageBuilder;
import com.nhnacademy.messenger.common.message.data.MessageData;
import com.nhnacademy.messenger.common.message.data.auth.LoginRequest;
import com.nhnacademy.messenger.common.message.data.auth.LoginResponse;
import com.nhnacademy.messenger.common.message.header.MessageType;
import com.nhnacademy.messenger.common.message.header.RequestHeader;
import com.nhnacademy.messenger.common.message.header.ResponseHeader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class MessageConverterTest {

    private Message loginRequestMessage;

    @BeforeEach
    void setUp() {
        loginRequestMessage = MessageBuilder.with(MessageType.LOGIN)
                .data(new LoginRequest("marco", "nhnacademy123"))
                .build();
    }

    @Test
    @DisplayName("직렬화 정상 동작 테스트: Length Line과 Payload가 포함되어야 함")
    void serialize_Success() {
        byte[] serialized = MessageConverter.toBytes(loginRequestMessage);
        String serializedStr = new String(serialized, StandardCharsets.UTF_8);

        assertAll(
                () -> assertTrue(serializedStr.startsWith("message-length: ")),
                () -> assertTrue(serializedStr.contains("\n")),
                () -> assertTrue(serializedStr.contains("LOGIN")),
                () -> assertTrue(serializedStr.contains("marco")),
                () -> assertTrue(serializedStr.contains("nhnacademy123"))
        );
    }

    @Test
    @DisplayName("왕복 테스트: 직렬화된 바이트 배열에서 바디만 추출하여 역직렬화하면 원본과 같아야 함")
    void roundTrip_Success() {
        byte[] serialized = MessageConverter.toBytes(loginRequestMessage);
        
        String serializedStr = new String(serialized, StandardCharsets.UTF_8);
        int lfIndex = serializedStr.indexOf('\n');
        String jsonBody = serializedStr.substring(lfIndex + 1);
        
        Message deserialized = MessageConverter.fromBytes(jsonBody.getBytes(StandardCharsets.UTF_8));

        assertAll(
                () -> assertInstanceOf(RequestHeader.class, deserialized.header()),
                () -> {
                    RequestHeader h = (RequestHeader) deserialized.header();
                    assertNull(h.sessionId());
                },
                () -> assertEquals(loginRequestMessage.header().type(), deserialized.header().type())
        );
    }

    @Test
    @DisplayName("Deduction 기반 역직렬화 테스트: ResponseHeader 필드 구성에 따라 자동으로 ResponseHeader로 변환되어야 함")
    void deserialize_ResponseHeader_Deduction() {
        // success 필드가 포함된 응답용 헤더 메시지 생성
        Message responseMessage = MessageBuilder.with(MessageType.LOGIN_SUCCESS)
                .success(true)
                .data(new LoginResponse("marco", UUID.randomUUID().toString(), "Welcome"))
                .build();

        byte[] serialized = MessageConverter.toBytes(responseMessage);
        String serializedStr = new String(serialized, StandardCharsets.UTF_8);
        String jsonBody = serializedStr.substring(serializedStr.indexOf('\n') + 1);
        
        Message deserialized = MessageConverter.fromBytes(jsonBody.getBytes(StandardCharsets.UTF_8));

        assertAll(
                () -> assertNotNull(deserialized),
                () -> assertInstanceOf(ResponseHeader.class, deserialized.header()),
                () -> assertTrue(((ResponseHeader) deserialized.header()).success())
        );
    }

    @Test
    @DisplayName("데이터 추출 테스트: Packet의 JsonNode를 실제 객체로 자동 변환 성공")
    void extractData_Success() {
        MessageData extracted = MessageConverter.toData(loginRequestMessage);

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

        assertThrows(MessageConvertException.class, () ->
                MessageConverter.fromBytes(brokenJson.getBytes(StandardCharsets.UTF_8))
        );
    }
}