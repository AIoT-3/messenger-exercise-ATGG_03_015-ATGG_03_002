package com.nhnacademy.messenger.common.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.messenger.common.exception.MessageConvertException;
import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.message.data.MessageData;
import com.nhnacademy.messenger.common.message.data.auth.LoginRequest;
import com.nhnacademy.messenger.common.message.data.auth.LoginResponse;
import com.nhnacademy.messenger.common.message.header.MessageType;
import com.nhnacademy.messenger.common.message.header.RequestHeader;
import com.nhnacademy.messenger.common.message.header.ResponseHeader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MessageConverterTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private Message loginRequestMessage;

    @BeforeEach
    void setUp() {
        // JSON 포맷(yyyy-MM-dd'T'HH:mm:ss)에 맞춰 나노초 단위 절삭
        RequestHeader header = new RequestHeader(
                MessageType.LOGIN,
                java.time.LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS),
                null
        );
        JsonNode data = objectMapper.valueToTree(new LoginRequest("marco", "nhnacademy123"));
        loginRequestMessage = new Message(header, data);
    }

    @Test
    @DisplayName("직렬화 정상 동작 테스트: Length Line과 Payload가 포함되어야 함")
    void serialize_Success() {
        String serialized = MessageConverter.serializeMessage(loginRequestMessage);

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
        String serialized = MessageConverter.serializeMessage(loginRequestMessage);
        
        String jsonBody = serialized.substring(serialized.indexOf('\n') + 1);
        Message deserialized = MessageConverter.deserializeMessage(jsonBody);

        assertAll(
                () -> assertEquals(loginRequestMessage, deserialized),
                () -> assertInstanceOf(RequestHeader.class, deserialized.header()),
                () -> {
                    RequestHeader h = (RequestHeader) deserialized.header();
                    assertNull(h.sessionId());
                },
                () -> assertEquals(serialized, MessageConverter.serializeMessage(deserialized))
        );
    }

    @Test
    @DisplayName("일반 요청 테스트: LOGIN이 아닌데 sessionId가 없으면 예외 발생")
    void constructor_Fail_MissingSessionId() {
        assertThrows(IllegalArgumentException.class, () -> 
            new RequestHeader(MessageType.CHAT_MESSAGE, java.time.LocalDateTime.now(), null)
        );
    }

    @Test
    @DisplayName("Deduction 기반 역직렬화 테스트: ResponseHeader 필드 구성에 따라 자동으로 ResponseHeader로 변환되어야 함")
    void deserialize_ResponseHeader_Deduction() {
        // success 필드가 포함된 응답용 헤더 메시지 생성
        ResponseHeader responseHeader = ResponseHeader.success(MessageType.LOGIN_SUCCESS);
        JsonNode responseData = objectMapper.valueToTree(new LoginResponse("marco", UUID.randomUUID().toString(), "Welcome"));
        Message responseMessage = new Message(responseHeader, responseData);

        String serialized = MessageConverter.serializeMessage(responseMessage);
        String jsonBody = serialized.substring(serialized.indexOf('\n') + 1);
        
        Message deserialized = MessageConverter.deserializeMessage(jsonBody);

        assertAll(
                () -> assertNotNull(deserialized),
                () -> assertInstanceOf(ResponseHeader.class, deserialized.header()),
                () -> assertTrue(((ResponseHeader) deserialized.header()).success())
        );
    }

    @Test
    @DisplayName("데이터 추출 테스트: Packet의 JsonNode를 실제 객체로 자동 변환 성공")
    void extractData_Success() {
        MessageData extracted = MessageConverter.extractData(loginRequestMessage);

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
                MessageConverter.deserializeMessage(brokenJson)
        );
    }
}