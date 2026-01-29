package com.nhnacademy.messenger.common.message;

import com.fasterxml.jackson.databind.JsonNode;
import com.nhnacademy.messenger.common.message.header.Header;

public record Message(
        Header header,
        JsonNode data
) {
}