package com.nhnacademy.messenger.common.message.data.user;

import com.fasterxml.jackson.annotation.JsonValue;
import java.util.List;

public record UserListResponse(
        @JsonValue
        List<UserInfo> data
) {
    public record UserInfo(
            String id,
            String name,
            Boolean online
    ) {}
}
