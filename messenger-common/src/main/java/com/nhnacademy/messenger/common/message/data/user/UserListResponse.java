package com.nhnacademy.messenger.common.message.data.user;
import com.nhnacademy.messenger.common.message.data.MessageData;

import java.util.List;

public record UserListResponse(
        List<UserInfo> users
) implements MessageData {
    public record UserInfo(
            String id,
            String name,
            Boolean online
    ) {}
}
