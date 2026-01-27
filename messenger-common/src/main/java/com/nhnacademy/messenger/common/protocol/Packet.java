package com.nhnacademy.messenger.common.protocol;

import com.fasterxml.jackson.databind.JsonNode;

public record Packet(
        MessageHeader header,
        JsonNode data
) {
}
