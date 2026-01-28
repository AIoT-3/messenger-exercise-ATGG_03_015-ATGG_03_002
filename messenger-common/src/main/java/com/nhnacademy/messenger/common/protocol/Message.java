package com.nhnacademy.messenger.common.protocol;

import com.fasterxml.jackson.databind.JsonNode;

public record Message(
        MessageHeader header,
        JsonNode data
) {
}
