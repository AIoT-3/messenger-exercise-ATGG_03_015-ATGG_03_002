package com.nhnacademy.messenger.common.message;

import com.fasterxml.jackson.databind.JsonNode;
import com.nhnacademy.messenger.common.message.header.MessageHeader;

public record Message(
        MessageHeader header,
        JsonNode data
) {
}
