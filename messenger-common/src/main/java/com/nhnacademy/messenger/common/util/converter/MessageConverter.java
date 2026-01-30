package com.nhnacademy.messenger.common.util.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nhnacademy.messenger.common.exception.MessageConvertException;
import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.message.data.MessageData;
import lombok.experimental.UtilityClass;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static com.nhnacademy.messenger.common.config.AppConstant.MESSAGE_LENGTH;

@UtilityClass
public class MessageConverter {

    public static final ObjectMapper objectMapper = new ObjectMapper();
    static {
        objectMapper.registerModule(new JavaTimeModule());
    }

    public static byte[] toBytes(Message message) {
        try {
            // 1. Message -> byte[] 변환
            byte[] jsonBytes = objectMapper.writeValueAsBytes(message);

            // 2. 헤더 생성
            String prefix = MESSAGE_LENGTH + jsonBytes.length + "\n";
            byte[] prefixBytes = prefix.getBytes(StandardCharsets.UTF_8);

            // 3. 최종 byte 배열 합치기
            byte[] result = new byte[prefixBytes.length + jsonBytes.length];
            System.arraycopy(prefixBytes, 0, result, 0, prefixBytes.length);
            System.arraycopy(jsonBytes, 0, result, prefixBytes.length, jsonBytes.length);

            return result;

        } catch (JsonProcessingException e) {
            throw new MessageConvertException("직렬화 실패", e);
        }
    }

    public static Message fromBytes(byte[] jsonBytes) {
        try {
            // 1. byte[] -> Message 객체 변환
            return objectMapper.readValue(jsonBytes, Message.class);

        } catch (java.io.IOException e) {
            throw new MessageConvertException("역직렬화 실패", e);
        }
    }

    public static MessageData toData(Message message) {
        // 1. 메시지 타입에 해당하는 데이터 클래스 조회
        Class<? extends MessageData> clazz = message.header().type().getDataClass();
        if (Objects.isNull(clazz)) {
            return null;
        }

        // 2. JsonNode -> 데이터 클래스 객체 변환
        try {
            return objectMapper.treeToValue(message.data(), clazz);
        } catch (JsonProcessingException e) {
            throw new MessageConvertException("데이터 변환 실패", e);
        } catch (IllegalArgumentException e) {
            throw new MessageConvertException("데이터 변환 실패 - 호환되지 않는 타입", e);
        }
    }
}
