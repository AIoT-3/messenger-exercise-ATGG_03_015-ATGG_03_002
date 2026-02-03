package com.nhnacademy.messenger.common.message.data.user;
import com.nhnacademy.messenger.common.message.data.MessageData;

import java.util.List;

public record UserListResponse(
        // api 스펙에는 users없이 data에 바로 배열이 오는데, 일관성도 떨어지고 그래서 users 사용
        List<UserInfo> users
) implements MessageData {

    // 재사용 필요하면 분리해주세요
    public record UserInfo(
            String id,
            String name,
            Boolean online
    ) {}
}
