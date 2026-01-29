package com.nhnacademy.messenger.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nhnacademy.messenger.common.exception.PacketSerializerException;
import com.nhnacademy.messenger.common.message.Message;
import lombok.experimental.UtilityClass;

import java.nio.charset.StandardCharsets;

@UtilityClass
public class JsonSerializer {

    private static final String MESSAGE_PREFIX = "message-length: ";

    public static final ObjectMapper objectMapper = new ObjectMapper();
    static {
        objectMapper.registerModule(new JavaTimeModule());
    }

    public static String serializeMessage(Message message) {
        try {
            // 1. json 변환
            String value = objectMapper.writeValueAsString(message);

            // 2. 길이 계산
            int length = value.getBytes(StandardCharsets.UTF_8).length;

            // 3. 헤더 생성
            String messagePrefix = MESSAGE_PREFIX + length + "\n";

            // 4. 최종 결합
            return messagePrefix + value;

        } catch (JsonProcessingException e) {
            throw new PacketSerializerException("직렬화 실패", e);
        }
    }

    public static Message deserializeMessage(String jsonBody) {
        try {
            return objectMapper.readValue(jsonBody, Message.class);

        } catch (JsonProcessingException e) {
            throw new PacketSerializerException("역직렬화 실패", e);
        }
    }

    public static <T> T extractData(Message message, Class<T> clazz) {
        try {
            return objectMapper.treeToValue(message.data(), clazz);

        } catch (JsonProcessingException e) {
            throw new PacketSerializerException("데이터 변환 실패", e);
        } catch (IllegalArgumentException e) {
            throw new PacketSerializerException("데이터 변환 실패 - 호환되지 않는 타입", e);
        }
    }
}
