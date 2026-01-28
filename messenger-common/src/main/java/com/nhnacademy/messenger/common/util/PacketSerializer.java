package com.nhnacademy.messenger.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nhnacademy.messenger.common.exception.PacketSerializerException;
import com.nhnacademy.messenger.common.protocol.Packet;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;

@UtilityClass
public class PacketSerializer {

    private static final String MESSAGE_PREFIX = "message-length: ";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.registerModule(new JavaTimeModule());
    }

    public static String serializePacket(Packet packet) {
        try {
            // 1. json 변환
            String value = objectMapper.writeValueAsString(packet);

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

    public static Packet deserializePacket(String data) {
        try {
            // 1. prefix, payload 분리
            String prefix = StringUtils.substringBefore(data, "\n");
            String payload = StringUtils.substringAfter(data, "\n");

            // 2. 길이 추출
            int length = Integer.parseInt(StringUtils.substringAfter(prefix, MESSAGE_PREFIX));

            // 3. payload 읽기
            if (length != payload.getBytes(StandardCharsets.UTF_8).length) {
                throw new PacketSerializerException("패킷 길이 불일치");
            }

            // 4. 객체 변환
            return objectMapper.readValue(payload, Packet.class);

        } catch (NumberFormatException e) {
            throw new PacketSerializerException("역직렬화 실패 - 길이 변환 불가", e);
        } catch (JsonProcessingException e) {
            throw new PacketSerializerException("역직렬화 실패", e);
        }
    }

}
