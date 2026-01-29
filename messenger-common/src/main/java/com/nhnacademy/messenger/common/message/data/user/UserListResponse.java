package com.nhnacademy.messenger.common.message.data.user;
import com.nhnacademy.messenger.common.message.data.MessageData;

import java.util.List;

public record UserListResponse(
        List<UserInfo> users
) implements MessageData {

    // 재사용 필요하면 분리해주세요
    public record UserInfo(
            String id,
            String name,
            Boolean online
    ) {}
}
