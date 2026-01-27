package com.nhnacademy.messenger.common.payload.user.list;

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
